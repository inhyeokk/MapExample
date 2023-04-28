package com.example.map.presentation.view.main.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.map.presentation.view.main.adapter.viewholder.DocumentViewHolder;
import com.example.map.presentation.model.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentViewHolder> {
    private final ArrayList<Document> documentList = new ArrayList<>();
    private final DocumentViewHolder.OnClickListener onClickListener;

    public DocumentAdapter(DocumentViewHolder.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return DocumentViewHolder.create(parent, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        holder.bind(documentList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public void setItemList(List<Document> documentList) {
        this.documentList.clear();
        this.documentList.addAll(documentList);
        notifyDataSetChanged();
    }

    public void updateSelect(int oldPosition, int position) {
        if (oldPosition != -1) {
            documentList.get(oldPosition).setSelected(false);
            notifyItemChanged(oldPosition);
        }
        if (position != -1) {
            documentList.get(position).setSelected(true);
            notifyItemChanged(position);
        }
    }
}
