package com.example.map.presentation.view.favorite;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.map.databinding.ActivityFavoriteBinding;
import com.example.map.presentation.model.Document;
import com.example.map.presentation.view.main.adapter.DocumentAdapter;
import com.example.map.presentation.view.main.adapter.viewholder.DocumentViewHolder;

public class FavoriteActivity extends AppCompatActivity {
    private FavoriteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFavoriteBinding binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DocumentAdapter documentAdapter = new DocumentAdapter(createOnClickListener());
        initView(binding, documentAdapter);
        observeViewModel(binding, documentAdapter);
    }

    @NonNull
    private DocumentViewHolder.OnClickListener createOnClickListener() {
        return new DocumentViewHolder.OnClickListener() {
            @Override
            public void onClick(Document document, int position) {

            }

            @Override
            public void onFavoriteClick(Document document) {
                viewModel.addFavoriteDocument(document);
            }

            @Override
            public void onUnFavoriteClick(Document document) {
                viewModel.removeFavoriteDocument(document);
            }
        };
    }

    private void initView(@NonNull ActivityFavoriteBinding binding, DocumentAdapter documentAdapter) {
        binding.btnClose.setOnClickListener(v -> finish());
        binding.rvFavorite.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvFavorite.setAdapter(documentAdapter);
    }

    private void observeViewModel(ActivityFavoriteBinding binding, DocumentAdapter documentAdapter) {
        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(FavoriteViewModel.initializer)).get(FavoriteViewModel.class);
        viewModel.favoriteDocumentListLiveData.observe(this, documents -> {
            documentAdapter.setItemList(documents);
            ViewKt.setVisible(binding.tvEmpty, documents.isEmpty());
        });
    }
}