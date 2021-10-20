package com.azharkova.photoram

import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.azharkova.photoram.data.*
import com.azharkova.photoram.util.Result
import com.azharkova.photoram.util.awaitsSingle
import com.azharkova.photoram.util.observeListData
import com.example.*
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*
import javax.xml.transform.sax.TransformerHandler
import kotlin.collections.ArrayList

class PostDbRepository {
    private val apolloClient = HerasuClient.instance.apolloClient

    companion object {
        val instance = PostDbRepository()
    }

    suspend fun loadPosts(): Result<List<PostItem>> {
        val response = apolloClient.query(PostsQuery()).await()
        val posts = response.data?.posts?.map { it.toPost() }
        val error = response.errors?.firstOrNull()?.message.orEmpty()
        if (posts != null) {
            return Result.Success(checkLiked(posts))
        } else {
            return Result.Error(java.lang.Exception(error))
        }
    }

    fun checkLiked(posts: List<PostItem>): List<PostItem> {
        val currentUser = AuthRepository.instance.currentUser
        if (currentUser != null) {
            for (item in posts) {
                item.hasLike = item.likeItems.any { it.userId == currentUser.uid.toString() }
            }
        }
        return posts
    }

    suspend fun loadPost(id: String): Result<PostItem?> {
        val postResponse =
            apolloClient.query(GetPostQuery(postId = Input.optional(UUID.fromString(id)))).await()
        val posts = postResponse.data?.posts?.map { it.toPost() }
        val error = postResponse.errors?.firstOrNull()?.message.orEmpty()
        if (posts != null) {
            return Result.Success(posts.firstOrNull())
        } else {
            return Result.Error(java.lang.Exception(error))
        }
    }


    suspend fun createPost(postItem: PostItem): Result<PostItem> {
        val currentUser = AuthRepository.instance.currentUser
        postItem.userId = currentUser?.uid.toString().orEmpty()
        postItem.userName = currentUser?.name.orEmpty()

        val response = apolloClient.mutate(QueryAdapter.instance.createPost(postItem)).await()
        val error = response.errors?.firstOrNull()?.message.orEmpty()
        val created = response.data?.insert_posts_one?.fragments?.toPost()
        if (created != null) {
            return Result.Success(created)
        } else {
            return Result.Error(java.lang.Exception(error))
        }
    }

   suspend fun changeLike(postItem: PostItem):Result<Boolean> {
        val currentUser = AuthRepository.instance.currentUser
        if (currentUser != null) {
            val uuid = currentUser.uid
            val found = postItem.likeItems.filter { it.userId == uuid.toString() }
            if (!found.isNullOrEmpty()) {
                postItem.likeItems =
                    postItem.likeItems.filter { it.userId != uuid.toString() } as ArrayList<LikeItem>
            } else {
                val likeItem = LikeItem()
                likeItem.postId = postItem.uuid
                likeItem.userId = uuid.toString()
                postItem.likeItems.add(likeItem)
            }
          val response =  apolloClient.mutate(
               QueryAdapter.instance.changeLike(postItem)
            ).await()

            val error = response.errors?.firstOrNull()?.message
            if (response.data != null) {
                return Result.Success(true)
            } else {
                return Result.Success(false)
            }
        } else {
            return Result.Success(false)
        }
    }

    suspend fun editPost(postItem: PostItem):Result<PostItem> {
        val currentUser = AuthRepository.instance.currentUser
        if (postItem.userId == currentUser?.uid.toString()) {
            val response = apolloClient.mutate(QueryAdapter.instance.changePost(postItem)).await()
            val error = response.errors?.firstOrNull()?.message
            if (response.data != null) {
                return Result.Success(postItem)
            } else {
                return Result.Error(Exception(error))
            }
        } else {
            return Result.Error(Exception("wrong user"))
        }
    }

   suspend fun deletePost(postItem: PostItem):Result<Boolean> {
        val currentUser = AuthRepository.instance.currentUser
        if (postItem.userId == currentUser?.uid.toString()) {
            val response = apolloClient.mutate(QueryAdapter.instance.deletePost(postItem)).await()
            val error = response.errors?.firstOrNull()?.message
            if (response.data != null) {
                return Result.Success(true)
            } else {
                return Result.Error(Exception(error))
            }

        } else {
            return Result.Error(Exception("wrong user"))
        }
    }

   suspend fun loadComments(postId: String): Result<List<CommentItem>> {
       val response =
           apolloClient.query(CommentsQuery(commentId = Input.optional(UUID.fromString(postId))))
               .await()
       val comments = response.data?.comments?.map { it.toComment() }
       val error = response.errors?.firstOrNull()?.message
       if (comments != null) {
           return Result.Success(comments)
       } else {
           return Result.Error(Exception(error))
       }
   }

    suspend fun sendComment(commentItem: CommentItem):Result<CommentItem> {
        val currentUser = AuthRepository.instance.currentUser
        commentItem.userId = currentUser?.uid.toString().orEmpty()
        commentItem.userName = currentUser?.name.orEmpty()
       val response = apolloClient.mutate(QueryAdapter.instance.createComment(commentItem)).await()
        val comment = response.data?.insert_comments_one?.toComment()
        val error = response.errors?.firstOrNull()?.message
        if (comment != null) {
            return Result.Success(comment)
        } else {
            return Result.Error(Exception(error))
        }
    }
}