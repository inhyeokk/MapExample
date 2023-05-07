package com.example.map.presentation.screen.search

import androidx.lifecycle.LiveData
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
    private val _searchResultLiveData = MutableLiveData(emptyList<SearchResult>())
    val searchResultLiveData: LiveData<List<SearchResult>> = _searchResultLiveData
    fun search(query: String) = viewModelScope.launch {
        val request = SearchByAddressRequest(query)
        localSearchRepository.searchByAddress(request).onSuccess {
            _searchResultLiveData.value = it.documents.map { result ->
                SearchResult.fromDocumentResult(result)
            }
        }
    }
}
