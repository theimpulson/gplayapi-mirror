package com.aurora.sampleapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.helpers.AuthHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val _authData: MutableStateFlow<AuthData?> = MutableStateFlow(null)
    val authData = _authData.asStateFlow()

    fun buildAuthData() {
        viewModelScope.launch(Dispatchers.IO) {
            _authData.value = AuthHelper.build(
                BuildConfig.GPLAY_API_EMAIL,
                BuildConfig.GPLAY_API_TOKEN,
                AuthHelper.Token.AAS
            )
        }
    }

    fun doSomething(context: Context) {
        // Run the thing you want to test here!
    }
}
