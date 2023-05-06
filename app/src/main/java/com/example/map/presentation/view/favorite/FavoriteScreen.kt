package com.example.map.presentation.view.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
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
import com.example.map.presentation.view.CloseButton
import com.example.map.presentation.view.CommonScaffold
import java.util.*

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
        FavoriteDocumentList(
            it,
            documentList,
            onUnFavoriteClick,
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

@Composable
fun FavoriteDocumentList(
    padding: PaddingValues, documentList: List<Document>, onUnFavoriteClick: (Document) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.padding(top = 16.dp), state = lazyListState, contentPadding = padding
    ) {
        itemsIndexed(
            items = documentList,
            key = { _, item -> item.id },
            itemContent = { _, item ->
                DocumentListItem(
                    document = item, onUnFavoriteClick = onUnFavoriteClick
                )
                Divider()
            },
        )
    }
}

@Composable
fun DocumentListItem(
    modifier: Modifier = Modifier, document: Document, onUnFavoriteClick: (Document) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                Text(text = document.placeName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = document.category(),
                    modifier = modifier.padding(start = 8.dp),
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
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_bookmark_24),
            contentDescription = "즐겨찾기 해제",
            modifier = Modifier
                .padding(6.dp)
                .clickable {
                    onUnFavoriteClick(document)
                },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun FavoriteDocumentListItemPreview() {
    MaterialTheme {
        Column {
            DocumentListItem(
                document = Document(
                    "0", "더진영어학원", "영어학원", "대구 달서구 이곡동", "0", "0", 4.3f, true
                )
            )
            DocumentListItem(
                document = Document(
                    "1", "더진영어학원", "영어학원", "대구 달서구 이곡동", "0", "0", 4.3f, true
                )
            )
            DocumentListItem(
                document = Document(
                    "2", "더진영어학원", "영어학원", "대구 달서구 이곡동", "0", "0", 4.3f, true
                )
            )
        }
    }
}
