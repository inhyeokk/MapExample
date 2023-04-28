package com.example.map.presentation.view.search.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map.presentation.model.SearchResult;
import com.example.map.presentation.view.search.adapter.viewholder.SearchResultViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    private final ArrayList<SearchResult> searchResultList = new ArrayList<>();
    private final Consumer<SearchResult> onClickListener;

    public SearchResultAdapter(Consumer<SearchResult> onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SearchResultViewHolder.create(parent, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.bind(searchResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }

    public void setItemList(List<SearchResult> searchResultList) {
        this.searchResultList.clear();
        this.searchResultList.addAll(searchResultList);
        notifyDataSetChanged();
    }
}
