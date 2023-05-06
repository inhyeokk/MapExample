package com.example.map.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp),
        topBar = {
            TopAppBar(
                modifier = Modifier.border(
                    width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(4.dp)
                ),
                navigationIcon = navigationIcon,
                title = title,
            )
        }, content = content
    )
}

@Composable
fun CloseButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Image(imageVector = Icons.Default.Close, contentDescription = "닫기")
    }
}

@Composable
fun SearchButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Image(imageVector = Icons.Default.Search, contentDescription = "검색")
    }
}
