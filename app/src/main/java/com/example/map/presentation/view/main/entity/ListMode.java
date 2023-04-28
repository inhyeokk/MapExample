package com.example.map.presentation.view.main.entity;

public enum ListMode {
    LIST, MAP;

    public boolean isList() {
        return this == LIST;
    }
}
