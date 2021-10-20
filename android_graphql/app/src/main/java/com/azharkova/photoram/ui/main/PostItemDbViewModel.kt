package com.azharkova.photoram.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.PostDbRepository
import com.azharkova.photoram.PostsRepository
import com.azharkova.photoram.data.CommentItem
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class PostItemDbViewModel : BaseViewModel() {
    val isDeleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var id: String = ""
    val post: MutableStateFlow<PostItem?> = MutableStateFlow(null)
    val comments: MutableStateFlow<MutableList<CommentItem>> = MutableStateFlow(mutableListOf())

    val errorb = MutableStateFlow("")

    fun loadPost() {
        if (id.isNotEmpty()) {
            modelScope.launch {
                try {
                    val postResponse =
                        withContext(ioDispatcher) { PostDbRepository.instance.loadPost(id) }
                    when (postResponse) {
                        is Result.Success<PostItem?> -> post.value = postResponse.data
                    }
                    listenComments()
                } catch (e: Exception) {

                }
            }
        }
    }

    fun listenComments() {
        modelScope.launch {
            val response = withContext(ioDispatcher) {
                PostDbRepository.instance.loadComments(id)
            }
            when (response) {
                is Result.Success<List<CommentItem>> -> comments.value =
                    response.data.toMutableList()
            }
        }
    }

    fun deletePost() {
        post.value?.let { post ->
            modelScope.launch {
                val response = withContext(ioDispatcher) {
                    PostDbRepository.instance.deletePost(post)
                }
                when (response) {
                    is Result<Boolean> -> isDeleted.value = true
                    is Result.Error -> errorb.tryEmit(response.exception.message.toString())
                }
            }
        }
    }


    fun sendComment(text: String) {
        val commentItem = CommentItem()
        commentItem.date = Date()
        commentItem.postId = id
        commentItem.text = text
        modelScope.launch {
            val response = withContext(ioDispatcher) {
                PostDbRepository.instance.sendComment(commentItem)
            }
            when (response) {
                is Result.Success<*> -> {
                    listenComments()
                }
            }
        }
    }
}

