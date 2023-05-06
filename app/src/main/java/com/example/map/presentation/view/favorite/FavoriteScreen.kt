package com.example.map.presentation.view.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.map.R
import com.example.map.presentation.model.Document
import com.example.map.presentation.view.CloseButton
import com.example.map.presentation.view.CommonScaffold
import com.example.map.presentation.view.main.DocumentList

@Composable
fun FavoriteScreen(
    documentList: List<Document>, onCloseClick: () -> Unit, onUnFavoriteClick: (Document) -> Unit
) {
    CommonScaffold(
        navigationIcon = {
            CloseButton(onCloseClick)
        },
        title = {
            Text(
                text = stringResource(
                    id = R.string.FavoriteActivity_title
                ), fontSize = 16.sp, color = Color.Black
            )
        },
    ) {
        DocumentList(
            contentPadding = it,
            documentList = documentList,
            onUnFavoriteClick = onUnFavoriteClick,
        )
        if (documentList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.FavoriteActivity_empty),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
