package com.example.map.presentation.view.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.map.base.BaseViewModel;
import com.example.map.data.repositoryimpl.LocalSearchRepositoryImpl;
import com.example.map.domain.repository.LocalSearchRepository;
import com.example.map.domain.request.SearchByAddressRequest;
import com.example.map.presentation.model.SearchResult;

import java.util.Collections;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class SearchViewModel extends BaseViewModel {
    private final SavedStateHandle handle;
    private final LocalSearchRepository localSearchRepository = new LocalSearchRepositoryImpl();
    public MutableLiveData<List<SearchResult>> searchResultLiveData = new MutableLiveData<>(Collections.emptyList());

    public SearchViewModel(SavedStateHandle handle) {
        this.handle = handle;
    }
    public void search(String query) {
        SearchByAddressRequest request = new SearchByAddressRequest(query);
        localSearchRepository.searchByAddress(request, localSearchResult -> {
            List<SearchResult> searchResultList = CollectionsKt.map(localSearchResult.getDocuments(), SearchResult::fromDocumentResult);
            searchResultLiveData.setValue(searchResultList);
        }, throwable -> {

        });
    }
}
