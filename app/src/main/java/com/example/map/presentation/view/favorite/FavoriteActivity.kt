package com.example.map.presentation.view.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.map.databinding.ActivityFavoriteBinding
import com.example.map.extension.launchWithLifecycle
import com.example.map.presentation.model.Document
import com.example.map.presentation.view.main.adapter.DocumentAdapter
import com.example.map.presentation.view.main.adapter.viewholder.DocumentViewHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {
    private val viewModel by viewModels<FavoriteViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val documentAdapter = DocumentAdapter(createOnClickListener())
        initView(binding, documentAdapter)
        observeViewModel(binding, documentAdapter)
    }

    private fun createOnClickListener(): DocumentViewHolder.OnClickListener {
        return object : DocumentViewHolder.OnClickListener {
            override fun onClick(document: Document, position: Int) {}
            override fun onFavoriteClick(document: Document) {
                viewModel.addFavoriteDocument(document)
            }

            override fun onUnFavoriteClick(document: Document) {
                viewModel.removeFavoriteDocument(document)
            }
        }
    }

    private fun initView(binding: ActivityFavoriteBinding, documentAdapter: DocumentAdapter) {
        binding.btnClose.setOnClickListener { finish() }
        binding.rvFavorite.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        binding.rvFavorite.adapter = documentAdapter
    }

    private fun observeViewModel(
        binding: ActivityFavoriteBinding,
        documentAdapter: DocumentAdapter
    ) {
        viewModel.favoriteDocumentListFlow.launchWithLifecycle(activity = this) {
            documentAdapter.setItemList(it)
            binding.tvEmpty.isVisible = it.isEmpty()
        }
    }
}
