package com.azharkova.photoram

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.data.QueryAdapter
import com.azharkova.photoram.data.UserData
import com.example.CreateUserMutation
import com.example.UsersQuery
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val apolloClient = HerasuClient.instance.apolloClient

    companion object {
        val instance = UserRepository()
    }

    suspend fun createUser(user: UserData): Response<CreateUserMutation.Data> {
        val newUser = QueryAdapter.instance.createUser(user)
       return  apolloClient.mutate(newUser).await()
    }
}