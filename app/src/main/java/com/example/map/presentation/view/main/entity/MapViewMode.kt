package com.example.map.presentation.view.main.entity

enum class MapViewMode {
    DEFAULT, SEARCH_FOOD, SEARCH_CAFE, SEARCH_CONVENIENCE, SEARCH_FLOWER;

    val isDefault: Boolean
        get() = this == DEFAULT
    val isNotDefault: Boolean
        get() = this != DEFAULT
}
