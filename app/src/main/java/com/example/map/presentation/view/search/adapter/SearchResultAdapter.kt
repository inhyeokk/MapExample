package com.example.map.presentation.view.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.map.presentation.model.SearchResult
import com.example.map.presentation.view.search.adapter.viewholder.SearchResultViewHolder
import java.util.function.Consumer

class SearchResultAdapter(
    private val onClickListener: Consumer<SearchResult>
) : RecyclerView.Adapter<SearchResultViewHolder>() {
    private val searchResultList = ArrayList<SearchResult>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(searchResultList[position])
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

    fun setItemList(searchResultList: List<SearchResult>) {
        this.searchResultList.clear()
        this.searchResultList.addAll(searchResultList)
        notifyDataSetChanged()
    }
}
