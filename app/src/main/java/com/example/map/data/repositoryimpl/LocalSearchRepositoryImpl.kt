package com.example.map.data.repositoryimpl

import com.example.map.data.remote.api.LocalSearchApi
import com.example.map.data.remote.model.LocalSearchResult
import com.example.map.domain.repository.LocalSearchRepository
import com.example.map.domain.request.SearchByAddressRequest
import com.example.map.domain.request.SearchByCategoryRequest
import com.example.map.domain.request.SearchByKeywordRequest
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Consumer
import javax.inject.Inject

@ViewModelScoped
class LocalSearchRepositoryImpl @Inject constructor(
    private val localSearchApi: LocalSearchApi
) : LocalSearchRepository {

    override fun searchByAddress(
        request: SearchByAddressRequest,
        onSuccess: Consumer<LocalSearchResult>,
        onFailure: Consumer<Throwable>
    ) {
        localSearchApi.searchByAddress(request.query).enqueue(object : Callback<LocalSearchResult> {
            override fun onResponse(
                call: Call<LocalSearchResult>, response: Response<LocalSearchResult>
            ) {
                onSuccess.accept(response.body()!!)
            }

            override fun onFailure(call: Call<LocalSearchResult>, t: Throwable) {
                onFailure.accept(t)
            }
        })
    }

    override fun searchByCategory(
        request: SearchByCategoryRequest,
        onSuccess: Consumer<LocalSearchResult>,
        onFailure: Consumer<Throwable>
    ) {
        localSearchApi.searchByCategory(
            request.categoryGroupCode, request.x, request.y, request.radius
        ).enqueue(object : Callback<LocalSearchResult> {
            override fun onResponse(
                call: Call<LocalSearchResult>, response: Response<LocalSearchResult>
            ) {
                onSuccess.accept(response.body()!!)
            }

            override fun onFailure(call: Call<LocalSearchResult>, t: Throwable) {
                onFailure.accept(t)
            }
        })
    }

    override fun searchByKeyword(
        request: SearchByKeywordRequest,
        onSuccess: Consumer<LocalSearchResult>,
        onFailure: Consumer<Throwable>
    ) {
        localSearchApi.searchByKeyword(
            request.query, request.x, request.y, request.radius
        ).enqueue(object : Callback<LocalSearchResult> {
            override fun onResponse(
                call: Call<LocalSearchResult>, response: Response<LocalSearchResult>
            ) {
                onSuccess.accept(response.body()!!)
            }

            override fun onFailure(call: Call<LocalSearchResult>, t: Throwable) {
                onFailure.accept(t)
            }
        })
    }
}
