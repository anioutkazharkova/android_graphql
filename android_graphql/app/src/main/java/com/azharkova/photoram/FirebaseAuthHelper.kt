package com.azharkova.photoram

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.azharkova.photoram.data.QueryAdapter
import com.azharkova.photoram.data.UserData
import com.azharkova.photoram.data.toUserData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.azharkova.photoram.util.Result
import com.example.CreateUserMutation
import com.example.UserQuery
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import java.lang.Exception

class AuthRepository {
    private  val apolloClient = HerasuClient.instance.apolloClient
    var currentUser: UserData? = null

    companion object {
        val instance = AuthRepository()
    }

    suspend  fun loginUser(email: String, password: String): Result<UserData> {
       val response = apolloClient.query(QueryAdapter.instance.loginUserQuery(email, password)).await()
        response.data?.users?.firstOrNull()?.let {
            currentUser = it.toUserData()
        }
        val error = response.errors?.firstOrNull()?.message
        if (currentUser != null) {
            return Result.Success(currentUser!!)
        } else {
            return Result.Error(Exception(error))
        }
    }

    suspend fun createUser(user: UserData): Result<UserData> {
        val newUser = QueryAdapter.instance.createUser(user)
        val response =  apolloClient.mutate(newUser).await()
        val userData = response.data?.insert_users_one?.fragments?.toUserData()
        val error = response.errors?.firstOrNull()?.message
        if (userData != null) {
            return Result.Success(userData)
        } else {
            return Result.Error(Exception(error))
        }
    }
}