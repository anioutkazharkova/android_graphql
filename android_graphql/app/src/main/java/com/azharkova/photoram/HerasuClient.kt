package com.azharkova.photoram

import android.content.Context
import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class HerasuClient {
     val apolloClient: ApolloClient by lazy {
        setupApollo()
    }
    companion object {
        val instance = HerasuClient()
    }

    private class AuthorizationInterceptor(): Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("x-hasura-admin-secret", "42vUA4kUsBVgYMOS9M9LT68zI30lPhQE4VWqbTQ1Yu5tpop1PKvcQwkhHfzVlZqB")
                .build()

            return chain.proceed(request)
        }
    }


    private fun setupApollo(): ApolloClient {
        return ApolloClient.builder()
            .serverUrl("https://star-hen-48.hasura.app/v1/graphql")
            .okHttpClient(OkHttpClient.Builder()
                .addInterceptor(AuthorizationInterceptor())
                .build()
            )
            .build()
    }
}