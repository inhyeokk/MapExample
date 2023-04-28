package com.example.map.presentation.view.main.entity;

public enum MapViewMode {
    DEFAULT,
    SEARCH_FOOD,
    SEARCH_CAFE,
    SEARCH_CONVENIENCE,
    SEARCH_FLOWER;

    public boolean isDefault() {
        return this == DEFAULT;
    }

    public boolean isNotDefault() {
        return this != DEFAULT;
    }
}
