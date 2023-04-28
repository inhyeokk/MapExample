package com.example.map.domain.repository;

import com.example.map.data.remote.model.LocalSearchResult;
import com.example.map.domain.request.SearchByAddressRequest;
import com.example.map.domain.request.SearchByCategoryRequest;
import com.example.map.domain.request.SearchByKeywordRequest;

import java.util.function.Consumer;

public interface LocalSearchRepository {
    void searchByAddress(SearchByAddressRequest request, Consumer<LocalSearchResult> onSuccess, Consumer<Throwable> onFailure);
    void searchByCategory(SearchByCategoryRequest request, Consumer<LocalSearchResult> onSuccess, Consumer<Throwable> onFailure);
    void searchByKeyword(SearchByKeywordRequest request, Consumer<LocalSearchResult> onSuccess, Consumer<Throwable> onFailure);
}
