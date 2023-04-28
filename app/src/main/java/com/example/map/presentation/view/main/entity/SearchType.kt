package com.example.map.presentation.view.main.entity

enum class SearchType(val type: String) {
    // category
    FOOD("FD6"),
    CAFE("CE7"),
    CONVENIENCE("CS2"),
    // keyword
    FLOWER("꽃집");

    val isCategory: Boolean
        get() = this == FOOD || this == CAFE || this == CONVENIENCE

    fun toMapViewMode(): MapViewMode {
        return when (this) {
            FOOD -> MapViewMode.SEARCH_FOOD
            CAFE -> MapViewMode.SEARCH_CAFE
            CONVENIENCE -> MapViewMode.SEARCH_CONVENIENCE
            FLOWER -> MapViewMode.SEARCH_FLOWER
        }
    }

    companion object {
        fun from(mapViewMode: MapViewMode): SearchType? {
            return when (mapViewMode) {
                MapViewMode.SEARCH_FOOD -> FOOD
                MapViewMode.SEARCH_CAFE -> CAFE
                MapViewMode.SEARCH_CONVENIENCE -> CONVENIENCE
                MapViewMode.SEARCH_FLOWER -> FLOWER
                else -> null
            }
        }
    }
}
