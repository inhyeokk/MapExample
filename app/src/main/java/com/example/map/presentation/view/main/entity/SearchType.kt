package com.example.map.presentation.view.main.entity;

public enum SearchType {
    // category
    FOOD("FD6"),
    CAFE("CE7"),
    CONVENIENCE("CS2"),

    // keyword
    FLOWER("꽃집");

    private String name;

    SearchType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCategory() {
        return this == FOOD || this == CAFE || this == CONVENIENCE;
    }

    public MapViewMode toMapViewMode() {
        switch (this) {
            case FOOD:
                return MapViewMode.SEARCH_FOOD;
            case CAFE:
                return MapViewMode.SEARCH_CAFE;
            case CONVENIENCE:
                return MapViewMode.SEARCH_CONVENIENCE;
            case FLOWER:
                return MapViewMode.SEARCH_FLOWER;
            default:
                return MapViewMode.DEFAULT;
        }
    }

    public static SearchType from(MapViewMode mapViewMode) {
        switch (mapViewMode) {
            case SEARCH_FOOD:
                return SearchType.FOOD;
            case SEARCH_CAFE:
                return SearchType.CAFE;
            case SEARCH_CONVENIENCE:
                return SearchType.CONVENIENCE;
            case SEARCH_FLOWER:
                return SearchType.FLOWER;
            default:
                return null;
        }
    }
}
