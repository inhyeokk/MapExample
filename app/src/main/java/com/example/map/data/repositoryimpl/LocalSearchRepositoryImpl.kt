package com.example.map.data.repositoryimpl

import com.example.map.data.remote.api.LocalSearchApi
import com.example.map.data.remote.model.LocalSearchResult
import com.example.map.domain.repository.LocalSearchRepository
import com.example.map.domain.request.SearchByAddressRequest
import com.example.map.domain.request.SearchByCategoryRequest
import com.example.map.domain.request.SearchByKeywordRequest
import com.example.map.extension.execute
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

    override suspend fun searchByAddress(request: SearchByAddressRequest): Result<LocalSearchResult> {
        return execute { localSearchApi.searchByAddress(request.query) }
    }

    override suspend fun searchByCategory(request: SearchByCategoryRequest): Result<LocalSearchResult> {
        return execute {
            localSearchApi.searchByCategory(
                request.categoryGroupCode, request.x, request.y, request.radius
            )
        }
    }

    override suspend fun searchByKeyword(request: SearchByKeywordRequest): Result<LocalSearchResult> {
        return execute {
            localSearchApi.searchByKeyword(
                request.query, request.x, request.y, request.radius
            )
        }
    }
}
