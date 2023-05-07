package com.example.map.presentation.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.map.R
import com.example.map.presentation.model.SearchResult
import com.example.map.presentation.screen.CloseButton
import com.example.map.presentation.screen.CommonScaffold
import com.example.map.presentation.screen.SearchButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchResultList: List<SearchResult>,
    onCloseClick: () -> Unit,
    onSearchClick: (String) -> Unit,
    onSearchResultClick: (SearchResult) -> Unit
) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    CommonScaffold(
        navigationIcon = {
            CloseButton(onCloseClick)
        },
        title = {
            TextField(
                value = query,
                onValueChange = { query = it },
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                placeholder = {
                    Text(
                        text = stringResource(
                            id = R.string.SearchActivity_title
                        ), fontSize = 16.sp, color = Color.Gray
                    )
                },
                trailingIcon = {
                    SearchButton {
                        onSearchClick(query.text)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearchClick(query.text) }),
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        },
    ) {
        SearchResultList(
            it,
            searchResultList,
            onSearchResultClick,
        )
        if (searchResultList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.SearchActivity_empty),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SearchResultList(
    padding: PaddingValues, searchResultList: List<SearchResult>, onClick: (SearchResult) -> Unit
) {
    LazyColumn(
        contentPadding = padding
    ) {
        itemsIndexed(
            items = searchResultList,
            key = { _, item -> item.addressName },
            itemContent = { _, item ->
                SearchResultListItem(
                    searchResult = item, onClick = onClick
                )
                Divider()
            },
        )
    }
}

@Composable
fun SearchResultListItem(
    modifier: Modifier = Modifier, searchResult: SearchResult, onClick: (SearchResult) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable { onClick(searchResult) },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = searchResult.addressName,
            color = Color.Black,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SearchResultListItemPreview() {
    MaterialTheme {
        Column {
            SearchResultListItem(
                searchResult = SearchResult("판교역로", "", "0", "0")
            )
            SearchResultListItem(
                searchResult = SearchResult("판교역로", "", "0", "0")
            )
            SearchResultListItem(
                searchResult = SearchResult("판교역로", "", "0", "0")
            )
        }
    }
}

