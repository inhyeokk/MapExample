package com.example.map.domain.request

data class SearchByKeywordRequest(val query: String, val x: String, val y: String, val radius: Int)
