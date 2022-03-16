package com.lahsuak.apps.githuboauth.network

import com.lahsuak.apps.githuboauth.model.AccessToken
import com.lahsuak.apps.githuboauth.model.Item
import com.lahsuak.apps.githuboauth.model.User
import com.lahsuak.apps.githuboauth.utils.Constants
import retrofit2.http.*


interface GithubApi {

    companion object{
        const val BASE_URL = Constants.apiURL
    }

    @Headers("Accept: application/json")
    @POST(Constants.domainURL + "login/oauth/access_token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): AccessToken

//    @Headers("Accept: application/json")
//    @GET("user/repos")
//    suspend fun getRepositories(
//    @Header("authorization") token: String
//    ): List<Repository>

    @Headers("Accept: application/json")
    @GET("user")
    suspend fun getUserData(
        @Header("authorization") token: String
    ): User

//    @Headers("Accept: application/json")
//    @GET("search/repositories")
//    suspend fun getTrendingRepository(
//        @Header("authorization") token: String
//    ): List<Repository>

//    @Headers("Accept: application/json")
//    @GET("search/repositories")
//    suspend fun fetchRepositories(
//        @Header("authorization") token: String,
//        @Query("q") q: String?,
//        @Query("sort") sort: String?,
//        @Query("order") order: String?,
//    ): List<GitHubRepo>

    @Headers("Accept: application/json")
    @GET("/search/repositories")
    suspend fun getSearchResult(
        @Query("q") query: String,
        @Query("") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort:String,
        @Query("order") order: String
    ): Item

}