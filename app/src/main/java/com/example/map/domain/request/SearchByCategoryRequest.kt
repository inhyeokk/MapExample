package com.example.map.domain.request

data class SearchByCategoryRequest(
    val categoryGroupCode: String,
    val x: String,
    val y: String,
    val radius: Int
)
