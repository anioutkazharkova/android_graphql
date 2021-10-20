package com.azharkova.photoram.data

import com.apollographql.apollo.api.Input
import com.example.*
import com.example.fragment.LikeForPost
import java.util.*

class QueryAdapter {
    companion object {
        val instance = QueryAdapter()
    }

    fun loginUserQuery(email: String, password: String): UserQuery {
        return UserQuery(Input.optional(email), Input.optional(password))
    }

    fun loginUserQuery(userData: UserData): UserQuery {
        return UserQuery(Input.optional(userData.email), Input.optional(userData.password))
    }

    fun createUser(userData: UserData):CreateUserMutation {
       return CreateUserMutation(name = Input.optional(userData.name), id = Input.optional(UUID.randomUUID()), email = Input.optional(userData.email), password = Input.optional(userData.password))
    }

    fun createPost(postItem: PostItem):AddPostMutation {
        return AddPostMutation(
            postId = Input.optional(UUID.fromString(postItem.uuid)),
            text = Input.optional(postItem.postText),
            user = Input.optional(postItem.userName),
            userId = Input.optional(UUID.fromString(postItem.userId)),
            date = Input.optional(Date()),
            image = Input.optional(postItem.imageLink.orEmpty())
        )
    }

    fun changeLike(postItem: PostItem):ChangeLikeMutation {
        val likes = postItem.mapLikes()
        return  ChangeLikeMutation(postId = Input.optional(UUID.fromString(postItem.uuid)), likes = Input.optional(likes))
    }

    fun changePost(postItem: PostItem):ChangePostMutation {
        return ChangePostMutation(postId = Input.optional(UUID.fromString(postItem.uuid)), postText = Input.optional(postItem.postText),imageLink = Input.optional(postItem.imageLink))
    }

    fun deletePost(postItem: PostItem):DeletePostMutation {
        return DeletePostMutation(Input.optional(postItem.uuid))
    }

    fun createComment(commentItem: CommentItem):CreateCommentMutation {
        return CreateCommentMutation(postId = Input.optional(UUID.fromString(commentItem.postId)), userId = Input.optional(UUID.fromString(commentItem.userId)),commentText = Input.optional(commentItem.text), id = Input.optional(UUID.randomUUID()),userName = Input.optional(commentItem.userName))
    }

}