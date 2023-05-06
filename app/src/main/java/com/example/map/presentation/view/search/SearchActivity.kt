package com.example.map.presentation.view.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.map.R
import com.example.map.extension.showToast
import com.example.map.presentation.model.SearchResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val viewModel by viewModels<SearchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var lastQuery = ""
                val searchResultList by viewModel.searchResultLiveData.observeAsState(initial = emptyList())
                SearchScreen(
                    searchResultList = searchResultList,
                    onCloseClick = {
                        finish()
                    },
                    onSearchClick = { query ->
                        lastQuery = query.trim { it <= ' ' }
                        requestSearch(lastQuery)
                    },
                    onSearchResultClick = this::onSearchResultClick
                )
                if (lastQuery.isNotEmpty() && searchResultList.isEmpty()) {
                    showToast(R.string.Toast_empty_search)
                }
            }
        }
    }

    private fun onSearchResultClick(searchResult: SearchResult) {
        val intent = Intent()
        intent.putExtra(SEARCH_RESULT, searchResult)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun requestSearch(query: String) {
        if (query.isNotEmpty()) {
            viewModel.search(query)
        } else {
            showToast(R.string.Toast_empty_query)
        }
    }

    companion object {
        const val SEARCH_RESULT = "SEARCH_RESULT"
    }
}
