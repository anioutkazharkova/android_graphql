package com.azharkova.photoram.data

import com.example.CommentsQuery
import com.example.CreateCommentMutation
import java.util.*

class CommentItem {
        var uuid = UUID.randomUUID().toString()
        var text: String = ""
        var date: Date? = null
        var userId: String = ""
        var userName: String = ""
        var postId: String = ""
}

fun CommentsQuery.Comment.toComment(): CommentItem {
      val comment =  this.fragments.comment
        val commentItem = CommentItem()
        commentItem.uuid = (comment.comment_id as? String).orEmpty()
        commentItem.text = comment.comment_text
        commentItem.userId = (comment.user_id as? String).orEmpty()
       commentItem.userName = comment.user_name.orEmpty()
        commentItem.postId = (comment.post_id as? String).orEmpty()
        return commentItem
}

fun CreateCommentMutation.Insert_comments_one.toComment(): CommentItem {
    val comment =  this.fragments.comment
    val commentItem = CommentItem()
    commentItem.uuid = (comment.comment_id as? String).orEmpty()
    commentItem.text = comment.comment_text
    commentItem.userId = (comment.user_id as? String).orEmpty()
    commentItem.userName = comment.user_name.orEmpty()
    commentItem.postId = (comment.post_id as? String).orEmpty()
    return commentItem
}