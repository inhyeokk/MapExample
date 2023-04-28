package com.example.map.presentation.view.main.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map.R;
import com.example.map.databinding.ItemDocumentBinding;
import com.example.map.presentation.model.Document;

import java.util.Locale;

import kotlin.collections.ArraysKt;

public class DocumentViewHolder extends RecyclerView.ViewHolder {
    private final ItemDocumentBinding binding;
    private final OnClickListener onClickListener;

    public DocumentViewHolder(@NonNull ItemDocumentBinding binding, OnClickListener onClickListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.onClickListener = onClickListener;
    }

    public void bind(Document document, int position) {
        binding.tvName.setText(document.getPlaceName());
        String category = ArraysKt.lastOrNull(document.getCategoryName().split(">"));
        if (category == null) {
            category = document.getCategoryName();
        }
        binding.tvCategory.setText(category);
        binding.tvAddress.setText(document.getRoadAddressName());
        binding.tvRate.setText(itemView.getContext().getString(R.string.DocumentViewHolder_rate, String.format(Locale.getDefault(), "%.1f", document.getRate())));
        if (document.isFavorite()) {
            binding.btnFavorite.setImageResource(R.drawable.baseline_bookmark_24);
            binding.btnFavorite.setOnClickListener(v -> onClickListener.onUnFavoriteClick(document));
        } else {
            binding.btnFavorite.setImageResource(R.drawable.baseline_bookmark_border_24);
            binding.btnFavorite.setOnClickListener(v -> onClickListener.onFavoriteClick(document));
        }
        binding.getRoot().setSelected(document.isSelected());
        binding.getRoot().setOnClickListener(v -> onClickListener.onClick(document, position));
    }

    public interface OnClickListener {
        void onClick(Document document, int position);
        void onFavoriteClick(Document document);
        void onUnFavoriteClick(Document document);
    }

    public static DocumentViewHolder create(ViewGroup parent, OnClickListener onClickListener) {
        ItemDocumentBinding binding = ItemDocumentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DocumentViewHolder(binding, onClickListener);
    }
}
