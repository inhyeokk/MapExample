package com.example.map.presentation.view.search

import androidx.lifecycle.MutableLiveData
import com.example.map.base.BaseViewModel
import com.example.map.data.repositoryimpl.LocalSearchRepositoryImpl
import com.example.map.domain.repository.LocalSearchRepository
import com.example.map.domain.request.SearchByAddressRequest
import com.example.map.presentation.model.SearchResult

class SearchViewModel : BaseViewModel() {
    private val localSearchRepository: LocalSearchRepository = LocalSearchRepositoryImpl()
    var searchResultLiveData = MutableLiveData(emptyList<SearchResult>())
    fun search(query: String) {
        val request = SearchByAddressRequest(query)
        localSearchRepository.searchByAddress(request, {
            val searchResultList = it.documents.map { result ->
                SearchResult.fromDocumentResult(result)
            }
            searchResultLiveData.setValue(searchResultList)
        }) {
            // do nothing
        }
    }
}
