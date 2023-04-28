package com.example.map.presentation.model

import com.example.map.data.remote.model.DocumentResult
import java.io.Serializable

data class SearchResult(
    val addressName: String,
    val addressType: String,
    private val x: String,
    private val y: String
) : Serializable {
    fun x() = x.toDouble()
    fun y() = y.toDouble()

    companion object {
        fun fromDocumentResult(result: DocumentResult): SearchResult {
            return SearchResult(
                result.addressName,
                result.addressType,
                result.x,
                result.y
            )
        }
    }
}
