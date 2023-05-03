package com.example.map.domain.repository

import com.example.map.data.remote.model.LocalSearchResult
import com.example.map.domain.request.SearchByAddressRequest
import com.example.map.domain.request.SearchByCategoryRequest
import com.example.map.domain.request.SearchByKeywordRequest

interface LocalSearchRepository {
    suspend fun searchByAddress(request: SearchByAddressRequest): Result<LocalSearchResult>

    suspend fun searchByCategory(request: SearchByCategoryRequest): Result<LocalSearchResult>

    suspend fun searchByKeyword(request: SearchByKeywordRequest): Result<LocalSearchResult>
}
