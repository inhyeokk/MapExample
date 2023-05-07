package com.example.map.presentation.screen.favorite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {
    private val viewModel by viewModels<FavoriteViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val documentList by viewModel.favoriteDocumentListFlow.collectAsState(initial = emptyList())
                FavoriteScreen(documentList, onCloseClick = {
                    finish()
                }, onUnFavoriteClick = {
                    viewModel.removeFavoriteDocument(it)
                })
            }
        }
    }

}
