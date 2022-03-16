package com.lahsuak.apps.githuboauth.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lahsuak.apps.githuboauth.model.Repository
import com.lahsuak.apps.githuboauth.model.User
import com.lahsuak.apps.githuboauth.network.GithubApi
import com.lahsuak.apps.githuboauth.utils.Constants.SORT_ASC
import com.lahsuak.apps.githuboauth.utils.Constants.SORT_TYPE
import com.lahsuak.apps.githuboauth.utils.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val githubApi: GithubApi
) : ViewModel() {

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User>
        get() = _userData

    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>>
        get() = _repositories


    fun getUserData(token: String) {
        viewModelScope.launch {
            try {
                _userData.value = githubApi.getUserData("bearer $token")
            } catch (e: Exception) {
                Log.d(TAG, "getUserData: error $e")
            }
        }
    }

    fun getData(query: String) {
        viewModelScope.launch {
            try {
                _repositories.value = githubApi.getSearchResult(query, 1, 100, SORT_TYPE, SORT_ASC).items
            } catch (e: Exception) {
                Log.d(TAG, "getData: ${e.message}")
            }
        }
    }

}