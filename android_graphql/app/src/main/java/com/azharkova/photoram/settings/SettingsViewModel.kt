package com.azharkova.photoram.settings

import androidx.lifecycle.ViewModel
import com.azharkova.photoram.AuthRepository
import com.azharkova.photoram.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel : ViewModel() {

    val userData: MutableStateFlow<UserData?> =  MutableStateFlow(null)

    fun loadUser() {
        userData.value =  AuthRepository.instance.currentUser//FirebaseAuthHelper.instance.getUser()
    }


    fun logout() {
        //FirebaseAuthHelper.instance.logout()
    }
}