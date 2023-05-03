package com.example.map.presentation.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.map.base.BaseViewModel
import com.example.map.domain.repository.LocalSearchRepository
import com.example.map.domain.request.SearchByAddressRequest
import com.example.map.presentation.model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val localSearchRepository: LocalSearchRepository
) : BaseViewModel() {
    var searchResultLiveData = MutableLiveData(emptyList<SearchResult>())
    fun search(query: String) = viewModelScope.launch {
        val request = SearchByAddressRequest(query)
        localSearchRepository.searchByAddress(request).onSuccess {
            val searchResultList = it.documents.map { result ->
                SearchResult.fromDocumentResult(result)
            }
            searchResultLiveData.setValue(searchResultList)
        }
    }
}
