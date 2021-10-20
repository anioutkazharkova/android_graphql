package com.azharkova.photoram.data

import com.example.CreateUserMutation
import com.example.UserQuery
import java.util.*

data class UserData (var uid: UUID = UUID.randomUUID(),
    var name: String = "",
    var email: String = "",
var password: String = "")

fun UserQuery.User.toUserData():UserData {
    val user = this.fragments.user
    val uuid =  (user.user_id as? String)?.let { UUID.fromString(it) } ?: UUID.randomUUID()
    val name = user.user_name
    val email = user.user_email
    return UserData(uuid,name,email,"")
}

fun CreateUserMutation.Insert_users_one.Fragments.toUserData():UserData {
    val user = this.user
    val uuid = (user.user_id as? String)?.let { UUID.fromString(it) } ?: UUID.randomUUID()
    val name = user.user_name
    val email = user.user_email
    return UserData(uuid,name,email,"")
}