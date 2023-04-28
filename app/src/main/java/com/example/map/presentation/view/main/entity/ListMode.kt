package com.example.map.presentation.view.main.entity

enum class ListMode {
    LIST, MAP;

    val isList: Boolean
        get() = this == LIST
}
