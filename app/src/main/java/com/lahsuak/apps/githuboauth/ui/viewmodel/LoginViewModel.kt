package com.lahsuak.apps.githuboauth.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lahsuak.apps.githuboauth.model.AccessToken
import com.lahsuak.apps.githuboauth.network.GithubApi
import com.lahsuak.apps.githuboauth.utils.Constants.TAG
import com.lahsuak.apps.githuboauth.utils.Constants.clientID
import com.lahsuak.apps.githuboauth.utils.Constants.clientSecret
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val githubApi: GithubApi
): ViewModel() {

    private val _accessToken = MutableLiveData<AccessToken>()
    val accessToken: LiveData<AccessToken>
        get() = _accessToken

    fun getAccessToken(code: String) {
        viewModelScope.launch {
            try {
                _accessToken.value = githubApi.getAccessToken(clientID, clientSecret, code)
                Log.d(TAG, "AccessToken: ${_accessToken.value?.accessToken}")
            } catch (e: Exception) {
                Log.d(TAG, "getAccessToken: error $e")
            }
        }
    }
}