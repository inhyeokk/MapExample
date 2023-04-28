package com.example.map.presentation.view.search.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.map.databinding.ItemSearchResultBinding
import com.example.map.presentation.model.SearchResult
import java.util.function.Consumer

class SearchResultViewHolder(
    private val binding: ItemSearchResultBinding,
    private val onClickListener: Consumer<SearchResult>
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(searchResult: SearchResult) {
        binding.tvName.text = searchResult.addressName
        binding.root.setOnClickListener { onClickListener.accept(searchResult) }
    }

    companion object {
        fun create(
            parent: ViewGroup, onClickListener: Consumer<SearchResult>
        ): SearchResultViewHolder {
            val binding = ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return SearchResultViewHolder(binding, onClickListener)
        }
    }
}
