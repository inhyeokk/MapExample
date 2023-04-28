package com.example.map.presentation.view.search.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map.databinding.ItemSearchResultBinding;
import com.example.map.presentation.model.SearchResult;

import java.util.function.Consumer;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {
    private final ItemSearchResultBinding binding;
    private final Consumer<SearchResult> onClickListener;

    public SearchResultViewHolder(@NonNull ItemSearchResultBinding binding, Consumer<SearchResult> onClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.onClickListener = onClickListener;
    }

    public void bind(SearchResult searchResult) {
        binding.tvName.setText(searchResult.getAddressName());
        binding.getRoot().setOnClickListener(v -> onClickListener.accept(searchResult));
    }

    public static SearchResultViewHolder create(ViewGroup parent, Consumer<SearchResult> onClickListener) {
        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchResultViewHolder(binding, onClickListener);
    }
}
