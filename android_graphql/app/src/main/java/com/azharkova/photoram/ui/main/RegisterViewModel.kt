package com.azharkova.photoram.ui.main

import androidx.lifecycle.ViewModel
import com.azharkova.photoram.AuthRepository
import com.azharkova.photoram.BaseViewModel
import com.azharkova.photoram.data.UserData
import com.azharkova.photoram.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {
    val newUser: MutableStateFlow<UserData?> = MutableStateFlow(null)

    fun register(name: String, email: String, password: String) {
        modelScope.launch {
           val result = AuthRepository.instance.createUser(UserData(name = name, email = email, password = password))
            when (result) {
                is Result.Success<*> -> newUser.value = result.data as? UserData
                else -> {

                }
            }
        }
    }
}