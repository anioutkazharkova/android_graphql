package com.azharkova.photoram.data

import com.azharkova.photoram.util.format
import com.example.AddPostMutation
import com.example.GetPostQuery
import com.example.PostsQuery
import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.ArrayList

class PostItem{
    var uuid: String = UUID.randomUUID().toString()
    var imageLink: String = ""
    var postText: String = ""
    var date:String = ""
    var userId: String = ""
    var userName: String = ""
    var likeItems: ArrayList<LikeItem> = arrayListOf()
    var editedTime: String? = null
    var editor: String? = null

    //var comments: ArrayList<CommentItem> = arrayListOf()

    var timeStamp: Date? = null

    @Exclude
    @com.google.firebase.firestore.Exclude
    var hasLike: Boolean = false
}

fun PostItem.mapLikes():String {
    val likeString = likeItems.map { it.userId }
    return likeString.joinToString(", ")
}

fun PostsQuery.Post.toPost():PostItem{
    val post = this.fragments.post
    var postItem = PostItem()
    postItem.uuid = (post.post_id as? String).orEmpty()
    postItem.userName = post.user_name.orEmpty()
    postItem.userId = (post.user_id as? String).orEmpty()
    postItem.date = (post.date as? Date)?.format("HH:mm dd.MM.yyyy").orEmpty()
    postItem.postText = post.post_text.orEmpty()
    if (post.likes != null) {
        postItem.likeItems = (post.likes as? String)?.split(",")?.orEmpty()?.map {
            val like = LikeItem()
            like.userId = it
            like.postId = post.post_id.toString()
            like
        } as ArrayList<LikeItem>
    } else {
        postItem.likeItems = arrayListOf()
    }
    postItem.imageLink = post.image_link.orEmpty()
    return postItem
}

fun GetPostQuery.Post.toPost():PostItem{
    val post = this.fragments.post
    var postItem = PostItem()
    postItem.uuid = (post.post_id as? String).orEmpty()
    postItem.userName = post.user_name.orEmpty()
    postItem.userId = (post.user_id as? String).orEmpty()
    postItem.date = (post.date as? Date)?.format("HH:mm dd.MM.yyyy").orEmpty()
    postItem.postText = post.post_text.orEmpty()
    if (post.likes != null) {
        postItem.likeItems = (post.likes as? String)?.split(",")?.orEmpty()?.map {
            val like = LikeItem()
            like.userId = it
            like.postId = post.post_id.toString()
            like
        }.orEmpty() as ArrayList<LikeItem>
    } else {
        postItem.likeItems = arrayListOf()
    }
    postItem.imageLink = post.image_link.orEmpty()
    return postItem
}

fun AddPostMutation.Insert_posts_one.Fragments.toPost():PostItem{
    val post = this.post
    var postItem = PostItem()
    postItem.uuid = (post.post_id as? String).orEmpty()
    postItem.userName = post.user_name.orEmpty()
    postItem.userId = (post.user_id as? String).orEmpty()
    postItem.date = (post.date as? Date)?.format("HH:mm dd.MM.yyyy").orEmpty()
    postItem.postText = post.post_text.orEmpty()
    postItem.likeItems = arrayListOf()
    postItem.imageLink = post.image_link.orEmpty()
    return postItem
}