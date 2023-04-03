package com.example.githubuser

import com.example.githubuser.model.DetailUserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun searchUsername(
        @Header("Authorization") token: String,
        @Query("q") q: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

}