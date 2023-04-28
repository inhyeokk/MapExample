package com.example.map.presentation.view.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.map.R;
import com.example.map.databinding.ActivitySearchBinding;
import com.example.map.extension.ToastKt;
import com.example.map.presentation.model.SearchResult;
import com.example.map.presentation.view.search.adapter.SearchResultAdapter;

import java.util.function.Consumer;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SearchResultAdapter parkingAdapter = new SearchResultAdapter(createOnClickListener());
        initView(binding, parkingAdapter);
        observeViewModel(parkingAdapter);
    }

    private Consumer<SearchResult> createOnClickListener() {
        return searchResult -> {
            Intent intent = new Intent();
            intent.putExtra(SEARCH_RESULT, searchResult);
            setResult(RESULT_OK, intent);
            finish();
        };
    }

    private void initView(ActivitySearchBinding binding, SearchResultAdapter searchResultAdapter) {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                requestSearch();
                return true;
            } else {
                return false;
            }
        });
        binding.btnSearch.setOnClickListener(v -> requestSearch());
        binding.rvSearchResult.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvSearchResult.setAdapter(searchResultAdapter);
    }

    private void requestSearch() {
        String query = getQuery();
        if (!query.isEmpty()) {
            viewModel.search(query);
        } else {
            ToastKt.show(this, R.string.Toast_empty_query);
        }
    }

    private String getQuery() {
        return binding.edSearch.getText().toString().trim();
    }

    private void observeViewModel(SearchResultAdapter searchResultAdapter) {
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        viewModel.searchResultLiveData.observe(this, searchResults -> {
            searchResultAdapter.setItemList(searchResults);
            ViewKt.setVisible(binding.tvEmpty, searchResults.isEmpty());
            if (!getQuery().isEmpty() && searchResults.isEmpty()) {
                ToastKt.show(this, R.string.Toast_empty_search);
            }
        });
    }

    public static final String SEARCH_RESULT = "SEARCH_RESULT";
}
