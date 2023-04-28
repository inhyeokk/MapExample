package com.example.map.domain.repository

import com.example.map.data.remote.model.LocalSearchResult
import com.example.map.domain.request.SearchByAddressRequest
import com.example.map.domain.request.SearchByCategoryRequest
import com.example.map.domain.request.SearchByKeywordRequest
import java.util.function.Consumer

interface LocalSearchRepository {
    fun searchByAddress(
        request: SearchByAddressRequest,
        onSuccess: Consumer<LocalSearchResult>,
        onFailure: Consumer<Throwable>
    )

    fun searchByCategory(
        request: SearchByCategoryRequest,
        onSuccess: Consumer<LocalSearchResult>,
        onFailure: Consumer<Throwable>
    )

    fun searchByKeyword(
        request: SearchByKeywordRequest,
        onSuccess: Consumer<LocalSearchResult>,
        onFailure: Consumer<Throwable>
    )
}
