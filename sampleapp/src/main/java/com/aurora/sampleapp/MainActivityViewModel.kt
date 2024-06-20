package com.aurora.sampleapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aurora.gplayapi.DeviceManager
import com.aurora.gplayapi.data.models.AuthData
import com.aurora.gplayapi.helpers.AuthHelper
import java.util.Properties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val _authData: MutableStateFlow<AuthData?> = MutableStateFlow(null)
    val authData = _authData.asStateFlow()

    fun buildAuthData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val properties = Properties()
            context.resources.openRawResource(com.aurora.gplayapi.R.raw.gplayapi_px_7a).use {
                properties.load(it)
            }

            _authData.value = AuthHelper.build(
                BuildConfig.GPLAY_API_EMAIL,
                BuildConfig.GPLAY_API_TOKEN,
                properties
            )
        }
    }

    fun doSomething(context: Context) {
        // Run the thing you want to test here!
    }
}
