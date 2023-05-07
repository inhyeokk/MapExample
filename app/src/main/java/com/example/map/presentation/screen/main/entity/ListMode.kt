package com.example.map.presentation.screen.main.entity

enum class ListMode {
    LIST, MAP;

    val isList: Boolean
        get() = this == LIST
}
