package ru.nifontbus.lc5_redditpaging.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nifontbus.lc5_redditpaging.model.RedditApiResponse

interface RedditApi {

    @GET("r/aww/hot.json")
    suspend fun fetchPosts(
        @Query("limit") loadSize: Int = 0,
        @Query("after") after: String? = null,
    ): RedditApiResponse
}