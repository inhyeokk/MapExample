package com.example.map.presentation.view.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.map.presentation.model.Document
import com.example.map.presentation.view.main.adapter.viewholder.DocumentViewHolder

class DocumentAdapter(
    private val onClickListener: DocumentViewHolder.OnClickListener
) : RecyclerView.Adapter<DocumentViewHolder>() {
    private val documentList = ArrayList<Document>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        return DocumentViewHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bind(documentList[position], position)
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    fun setItemList(documentList: List<Document>) {
        this.documentList.clear()
        this.documentList.addAll(documentList)
        notifyDataSetChanged()
    }

    fun updateSelect(oldPosition: Int, position: Int) {
        if (oldPosition != -1) {
            documentList[oldPosition].isSelected = false
            notifyItemChanged(oldPosition)
        }
        if (position != -1) {
            documentList[position].isSelected = true
            notifyItemChanged(position)
        }
    }
}
