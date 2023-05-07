package com.example.map.presentation.screen.main

import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.map.R
import com.example.map.extension.toggle
import com.example.map.presentation.model.Document
import com.example.map.presentation.model.DocumentResult
import com.example.map.presentation.screen.BackButton
import com.example.map.presentation.screen.CommonTopAppBar
import com.example.map.presentation.screen.SearchImage
import com.example.map.presentation.screen.main.entity.ListMode
import com.example.map.presentation.screen.main.entity.MapViewMode
import com.example.map.presentation.screen.main.entity.SelectPosition
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapView.CurrentLocationTrackingMode
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    mapViewMode: MapViewMode,
    documentResult: DocumentResult,
    selectPositionEvent: SelectPosition,
    trackingMode: CurrentLocationTrackingMode,
    listMode: ListMode,
    onDocumentClick: (Document, Int) -> Unit,
    onDocumentFavoriteClick: (Document) -> Unit,
    onDocumentUnFavoriteClick: (Document) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFoodClick: () -> Unit,
    onCafeClick: () -> Unit,
    onConvenienceClick: () -> Unit,
    onFlowerClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onTrackingModeClick: () -> Unit,
    onListModeClick: () -> Unit,
    mapView: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val initialValue =
        if (mapViewMode.isNotDefault) BottomSheetValue.Expanded else BottomSheetValue.Collapsed
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = initialValue)
    )
    BottomSheetScaffold(
        sheetContent = {
            val lazyListState = rememberLazyListState()
            if (selectPositionEvent.position != -1 && selectPositionEvent.selectedByMap) {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        lazyListState.scrollToItem(selectPositionEvent.position)
                    }
                }
            }
            MainBottomSheet(
                state = lazyListState,
                documentList = documentResult.documentList,
                selectedPosition = selectPositionEvent.position,
                onDocumentClick = onDocumentClick,
                onFavoriteClick = onDocumentFavoriteClick,
                onUnFavoriteClick = onDocumentUnFavoriteClick,
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false,
    ) {
        Box {
            mapView()
            Column {
                MainTopAppBar(
                    mapViewMode = mapViewMode,
                    onBackClick = onBackClick,
                    onSearchClick = onSearchClick,
                )
                MainButtonRow(
                    mapViewMode = mapViewMode,
                    onFoodClick = onFoodClick,
                    onCafeClick = onCafeClick,
                    onConvenienceClick = onConvenienceClick,
                    onFlowerClick = onFlowerClick,
                    onFavoriteClick = onFavoriteClick,
                )
                MainFloatingActionButtons(
                    mapViewMode = mapViewMode,
                    trackingMode = trackingMode,
                    listMode = listMode,
                    onTrackingModeClick = onTrackingModeClick,
                    onListModeClick = {
                        onListModeClick()
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.toggle()
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun ColumnScope.MainTopAppBar(
    mapViewMode: MapViewMode,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    CommonTopAppBar(
        navigationIcon = {
            if (mapViewMode.isNotDefault) {
                BackButton(onBackClick)
            } else {
                SearchImage()
            }
        },
        title = {
            val modifier = if (mapViewMode.isDefault) {
                Modifier.clickable { onSearchClick() }
            } else {
                Modifier
            }
            Text(
                text = stringResource(
                    id = mapViewMode.toTitleRes()
                ), modifier = modifier, fontSize = 16.sp, color = Color.Black
            )
        },
    )
}

@StringRes
private fun MapViewMode.toTitleRes(): Int {
    return when (this) {
        MapViewMode.DEFAULT -> R.string.MainActivity_search_bar
        MapViewMode.SEARCH_FOOD -> R.string.MainActivity_food
        MapViewMode.SEARCH_CAFE -> R.string.MainActivity_cafe
        MapViewMode.SEARCH_CONVENIENCE -> R.string.MainActivity_convenience
        MapViewMode.SEARCH_FLOWER -> R.string.MainActivity_flower
    }
}

@Composable
fun ColumnScope.MainButtonRow(
    mapViewMode: MapViewMode,
    onFoodClick: () -> Unit,
    onCafeClick: () -> Unit,
    onConvenienceClick: () -> Unit,
    onFlowerClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier.horizontalScroll(scrollState)) {
        MainButton(
            text = stringResource(id = R.string.MainActivity_food),
            modifier = Modifier
                .padding(start = 8.dp, end = 4.dp),
            isSelected = mapViewMode == MapViewMode.SEARCH_FOOD,
            onClick = { onFoodClick() }
        )
        MainButton(
            text = stringResource(id = R.string.MainActivity_cafe),
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp),
            isSelected = mapViewMode == MapViewMode.SEARCH_CAFE,
            onClick = { onCafeClick() }
        )
        MainButton(
            text = stringResource(id = R.string.MainActivity_convenience),
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp),
            isSelected = mapViewMode == MapViewMode.SEARCH_CONVENIENCE,
            onClick = { onConvenienceClick() },
        )
        MainButton(
            text = stringResource(id = R.string.MainActivity_flower),
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp),
            isSelected = mapViewMode == MapViewMode.SEARCH_FLOWER,
            onClick = { onFlowerClick() },
        )
        MainButton(
            text = stringResource(id = R.string.MainActivity_favorite),
            modifier = Modifier
                .padding(start = 4.dp, end = 8.dp),
            onClick = { onFavoriteClick() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainButton(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.defaultMinSize(minWidth = 60.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = if (isSelected) BorderStroke(width = 1.dp, color = Color.Black) else null,
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Gray
        )
    }
}

@Composable
fun ColumnScope.MainFloatingActionButtons(
    mapViewMode: MapViewMode,
    trackingMode: CurrentLocationTrackingMode,
    listMode: ListMode,
    onTrackingModeClick: () -> Unit,
    onListModeClick: () -> Unit
) {
    MainFloatingActionButton(
        onClick = onTrackingModeClick,
    ) {
        val res = when (trackingMode) {
            CurrentLocationTrackingMode.TrackingModeOnWithoutHeading -> R.drawable.baseline_gps_activated_24
            CurrentLocationTrackingMode.TrackingModeOnWithHeading -> R.drawable.baseline_compass_calibration_24
            else -> R.drawable.baseline_gps_fixed_24
        }
        Image(painter = painterResource(id = res), contentDescription = "")
    }
    if (mapViewMode.isNotDefault) {
        MainFloatingActionButton(
            onClick = onListModeClick,
        ) {
            val res = when (listMode) {
                ListMode.LIST -> R.drawable.baseline_map_24
                else -> R.drawable.baseline_list_24
            }
            Image(painter = painterResource(id = res), contentDescription = "")
        }
    }
}

@Composable
fun ColumnScope.MainFloatingActionButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 8.dp, end = 8.dp)
            .align(Alignment.End),
        shape = CircleShape,
        containerColor = Color.White,
        content = content
    )
}

@Composable
fun MainBottomSheet(
    state: LazyListState,
    documentList: List<Document>,
    selectedPosition: Int,
    onDocumentClick: (Document, Int) -> Unit,
    onFavoriteClick: (Document) -> Unit,
    onUnFavoriteClick: (Document) -> Unit
) {
    DocumentList(
        modifier = Modifier
            .height(190.dp)
            .background(Color.White),
        state = state,
        documentList = documentList,
        selectedPosition = selectedPosition,
        onClick = onDocumentClick,
        onFavoriteClick = onFavoriteClick,
        onUnFavoriteClick = onUnFavoriteClick
    )
}

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
                    modifier = if (isSelected) {
                        Modifier.border(
                            width = 1.dp, color = Color.Black
                        )
                    } else {
                        Modifier
                    },
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
            .clickable { onClick(document, index) }
            .padding(horizontal = 8.dp, vertical = 8.dp),
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
