package com.example.map.presentation.view.main.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.map.R
import com.example.map.databinding.ItemDocumentBinding
import com.example.map.presentation.model.Document
import java.util.*

class DocumentViewHolder(
    private val binding: ItemDocumentBinding,
    private val onClickListener: OnClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(document: Document, position: Int) {
        binding.tvName.text = document.placeName
        var category = document.categoryName.split(">".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray().lastOrNull()
        if (category == null) {
            category = document.categoryName
        }
        binding.tvCategory.text = category
        binding.tvAddress.text = document.roadAddressName
        binding.tvRate.text = itemView.context.getString(
            R.string.DocumentViewHolder_rate, String.format(
                Locale.getDefault(), "%.1f", document.rate
            )
        )
        if (document.isFavorite) {
            binding.btnFavorite.setImageResource(R.drawable.baseline_bookmark_24)
            binding.btnFavorite.setOnClickListener {
                onClickListener.onUnFavoriteClick(document)
            }
        } else {
            binding.btnFavorite.setImageResource(R.drawable.baseline_bookmark_border_24)
            binding.btnFavorite.setOnClickListener {
                onClickListener.onFavoriteClick(document)
            }
        }
        binding.root.isSelected = document.isSelected
        binding.root.setOnClickListener { onClickListener.onClick(document, position) }
    }

    interface OnClickListener {
        fun onClick(document: Document, position: Int)
        fun onFavoriteClick(document: Document)
        fun onUnFavoriteClick(document: Document)
    }

    companion object {
        fun create(parent: ViewGroup, onClickListener: OnClickListener): DocumentViewHolder {
            val binding = ItemDocumentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return DocumentViewHolder(binding, onClickListener)
        }
    }
}
