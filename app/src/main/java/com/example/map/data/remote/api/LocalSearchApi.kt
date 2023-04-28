package com.example.map.data.remote.api;

import com.example.map.data.remote.model.LocalSearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocalSearchApi {
    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord
    @GET("/v2/local/search/address.json")
    Call<LocalSearchResult> searchByAddress(
        @Query("query") String query
    );

    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-category
    @GET("/v2/local/search/category.json")
    Call<LocalSearchResult> searchByCategory(
        @Query("category_group_code") String category_group_code,
        @Query("x") String x,
        @Query("y") String y,
        @Query("radius") int radius
    );

    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword
    @GET("/v2/local/search/keyword.json")
    Call<LocalSearchResult> searchByKeyword(
        @Query("query") String query,
        @Query("x") String x,
        @Query("y") String y,
        @Query("radius") int radius
    );

}
