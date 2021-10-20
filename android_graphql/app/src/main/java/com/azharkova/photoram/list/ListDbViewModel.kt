package com.azharkova.photoram.list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.PostDbRepository
import com.azharkova.photoram.PostsRepository
import com.azharkova.photoram.data.PostItem
import com.azharkova.photoram.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListDbViewModel : BaseViewModel() {
    val posts: MutableStateFlow<MutableList<PostItem>> = MutableStateFlow(mutableListOf())

    val selectedPost: MutableStateFlow<String> = MutableStateFlow("")


    fun listenPosts() {
        modelScope.launch {
           val response = withContext(ioDispatcher) { PostDbRepository.instance.loadPosts()}
               when (response) {
                   is Result.Success<List<PostItem>> -> posts.value = response.data.toMutableList()
                   is Result.Error -> Log.d("POSTS",response.exception.toString())
            }
        }
    }

    fun changeLike(index: Int) {
        val post = posts.value.get(index)
        modelScope.launch {
            val response = withContext(ioDispatcher) { PostDbRepository.instance.changeLike(post) }
            when (response) {
                is Result.Success<*> -> {
                    post.hasLike = !post.hasLike
                  listenPosts()
                }
                is Result.Error -> Log.d("LIKE",response.exception.toString())
            }

        }
    }


    fun selectPost(index: Int):String {
        return posts.value.get(index).uuid
    }

}