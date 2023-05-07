package com.example.map.presentation.model

import android.os.Parcelable
import com.example.map.data.remote.model.DocumentResult
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResult(
    val addressName: String,
    val addressType: String,
    private val x: String,
    private val y: String
) : Parcelable {
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
