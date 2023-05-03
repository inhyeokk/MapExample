package com.example.map.data.remote.api

import com.example.map.data.remote.model.LocalSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocalSearchApi {
    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord
    @GET("/v2/local/search/address.json")
    suspend fun searchByAddress(
        @Query("query") query: String
    ): Response<LocalSearchResult>

    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category
    @GET("/v2/local/search/category.json")
    suspend fun searchByCategory(
        @Query("category_group_code") category_group_code: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int
    ): Response<LocalSearchResult>

    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword
    @GET("/v2/local/search/keyword.json")
    suspend fun searchByKeyword(
        @Query("query") query: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int
    ): Response<LocalSearchResult>
}
