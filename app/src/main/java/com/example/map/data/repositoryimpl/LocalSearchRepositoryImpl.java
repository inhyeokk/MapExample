package com.example.map.data.repositoryimpl;

import androidx.annotation.NonNull;

import com.example.map.data.remote.api.KakaoLocalServiceFactory;
import com.example.map.data.remote.api.LocalSearchApi;
import com.example.map.data.remote.model.LocalSearchResult;
import com.example.map.domain.repository.LocalSearchRepository;
import com.example.map.domain.request.SearchByAddressRequest;
import com.example.map.domain.request.SearchByCategoryRequest;
import com.example.map.domain.request.SearchByKeywordRequest;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalSearchRepositoryImpl implements LocalSearchRepository {
    private final LocalSearchApi localSearchApi = KakaoLocalServiceFactory.create(LocalSearchApi.class);

    @Override
    public void searchByAddress(SearchByAddressRequest request, Consumer<LocalSearchResult> onSuccess, Consumer<Throwable> onFailure) {
        localSearchApi.searchByAddress(request.getQuery()).enqueue(new Callback<LocalSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<LocalSearchResult> call, @NonNull Response<LocalSearchResult> response) {
                onSuccess.accept(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LocalSearchResult> call, @NonNull Throwable t) {
                onFailure.accept(t);
            }
        });
    }

    @Override
    public void searchByCategory(SearchByCategoryRequest request, Consumer<LocalSearchResult> onSuccess, Consumer<Throwable> onFailure) {
        localSearchApi.searchByCategory(request.getCategoryGroupCode(), request.getX(), request.getY(), request.getRadius()).enqueue(new Callback<LocalSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<LocalSearchResult> call, @NonNull Response<LocalSearchResult> response) {
                onSuccess.accept(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LocalSearchResult> call, @NonNull Throwable t) {
                onFailure.accept(t);
            }
        });
    }

    @Override
    public void searchByKeyword(SearchByKeywordRequest request, Consumer<LocalSearchResult> onSuccess, Consumer<Throwable> onFailure) {
        localSearchApi.searchByKeyword(request.getQuery(), request.getX(), request.getY(), request.getRadius()).enqueue(new Callback<LocalSearchResult>() {
            @Override
            public void onResponse(@NonNull Call<LocalSearchResult> call, @NonNull Response<LocalSearchResult> response) {
                onSuccess.accept(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<LocalSearchResult> call, @NonNull Throwable t) {
                onFailure.accept(t);
            }
        });
    }
}
