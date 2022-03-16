package com.lahsuak.apps.githuboauth.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.lahsuak.apps.githuboauth.network.GithubApi
import com.lahsuak.apps.githuboauth.network.GithubApi.Companion.BASE_URL
import com.lahsuak.apps.githuboauth.utils.Constants
import com.lahsuak.apps.githuboauth.utils.Constants.ACCESS_TOKEN
import com.lahsuak.apps.githuboauth.utils.Constants.SORT_ORDER
import com.lahsuak.apps.githuboauth.utils.Constants.USER_STATUS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context) =
        context.getSharedPreferences(Constants.SETTING_DATA, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideSortOrder(sharedPref: SharedPreferences) = sharedPref.getInt(SORT_ORDER, 0)

    @Singleton
    @Provides
    fun provideStatus(sharedPref: SharedPreferences) = sharedPref.getBoolean(USER_STATUS, false)

    @Singleton
    @Provides
    fun provideAccessToken(sharedPref: SharedPreferences) = sharedPref.getString(ACCESS_TOKEN, null)
}