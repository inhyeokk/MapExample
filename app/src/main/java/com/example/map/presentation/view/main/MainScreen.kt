package com.example.map.presentation.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.map.R
import com.example.map.presentation.model.Document
import java.util.*

@Composable
fun DocumentList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    documentList: List<Document>,
    selectedPosition: Int = -1,
    onClick: (Document, Int) -> Unit = { _, _ -> },
    onFavoriteClick: (Document) -> Unit = {},
    onUnFavoriteClick: (Document) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier, state = state, contentPadding = contentPadding
    ) {
        itemsIndexed(
            items = documentList,
            key = { _, item -> item.toString() },
            itemContent = { index, item ->
                val isSelected = index == selectedPosition
                DocumentListItem(
                    modifier = if (isSelected) Modifier.border(width = 1.dp, color = Color.Black) else Modifier,
                    index = index,
                    document = item,
                    onClick = onClick,
                    onFavoriteClick = onFavoriteClick,
                    onUnFavoriteClick = onUnFavoriteClick
                )
                Divider()
            },
        )
    }
}

@Composable
fun DocumentListItem(
    modifier: Modifier = Modifier,
    index: Int,
    document: Document,
    onClick: (Document, Int) -> Unit = { _, _ -> },
    onFavoriteClick: (Document) -> Unit = {},
    onUnFavoriteClick: (Document) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick(document, index) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                Text(text = document.placeName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = document.category(),
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Row {
                Text(text = document.roadAddressName, color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = stringResource(
                        id = R.string.DocumentViewHolder_rate, String.format(
                            Locale.getDefault(), "%.1f", document.rate
                        )
                    ),
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        if (document.isFavorite) {
            UnFavoriteButton { onUnFavoriteClick(document) }
        } else {
            FavoriteButton { onFavoriteClick(document) }
        }
    }
}

@Composable
fun FavoriteButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_border_24),
            contentDescription = "즐겨찾기"
        )
    }
}

@Composable
fun UnFavoriteButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_24),
            contentDescription = "즐겨찾기 해제"
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun DocumentListItemPreview() {
    MaterialTheme {
        Column {
            DocumentListItem(
                document = Document(
                    "0", "더진영어학원", "영어학원", "대구 달서구 이곡동", "0", "0", 4.3f, true
                ),
                index = 0,
            )
            DocumentListItem(
                document = Document(
                    "1", "더진영어학원", "영어학원", "대구 달서구 이곡동", "0", "0", 4.3f, true
                ),
                index = 1,
            )
            DocumentListItem(
                document = Document(
                    "2", "더진영어학원", "영어학원", "대구 달서구 이곡동", "0", "0", 4.3f, true
                ),
                index = 2,
            )
        }
    }
}
