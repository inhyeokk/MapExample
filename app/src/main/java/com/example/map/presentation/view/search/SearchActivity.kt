package com.example.map.presentation.view.search

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.map.R
import com.example.map.databinding.ActivitySearchBinding
import com.example.map.extension.showToast
import com.example.map.presentation.model.SearchResult
import com.example.map.presentation.view.search.adapter.SearchResultAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.function.Consumer

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val parkingAdapter = SearchResultAdapter(createOnClickListener())
        initView(binding, parkingAdapter)
        observeViewModel(parkingAdapter)
    }

    private fun createOnClickListener(): Consumer<SearchResult> {
        return Consumer {
            val intent = Intent()
            intent.putExtra(SEARCH_RESULT, it)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initView(binding: ActivitySearchBinding, searchResultAdapter: SearchResultAdapter) {
        binding.btnClose.setOnClickListener { finish() }
        binding.edSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                requestSearch()
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
        binding.btnSearch.setOnClickListener { requestSearch() }
        binding.rvSearchResult.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        binding.rvSearchResult.adapter = searchResultAdapter
    }

    private fun requestSearch() {
        val query = query
        if (query.isNotEmpty()) {
            viewModel.search(query)
        } else {
            showToast(R.string.Toast_empty_query)
        }
    }

    private val query: String
        get() = binding.edSearch.text.toString().trim { it <= ' ' }

    private fun observeViewModel(searchResultAdapter: SearchResultAdapter) {
        viewModel.searchResultLiveData.observe(this) {
            searchResultAdapter.setItemList(it)
            binding.tvEmpty.isVisible = it.isEmpty()
            if (query.isNotEmpty() && it.isEmpty()) {
                showToast(R.string.Toast_empty_search)
            }
        }
    }

    companion object {
        const val SEARCH_RESULT = "SEARCH_RESULT"
    }
}
