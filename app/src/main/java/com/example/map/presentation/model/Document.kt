package com.example.map.presentation.model

import com.example.map.data.local.model.DocumentEntity
import com.example.map.data.remote.model.DocumentResult
import net.daum.mf.map.api.MapPOIItem
import java.util.*

data class Document(
    val id: String,
    val placeName: String,
    val categoryName: String,
    val roadAddressName: String,
    private val x: String,
    private val y: String,
    val rate: Float,
    var isFavorite: Boolean,
    var isSelected: Boolean = false,
    var mapPOIItem: MapPOIItem? = null
) {
    fun x() = x.toDouble()
    fun y() = y.toDouble()

    fun toEntity(): DocumentEntity {
        return DocumentEntity(id, placeName, categoryName, roadAddressName, x, y, rate)
    }

    companion object {
        fun fromDocumentEntity(entity: DocumentEntity): Document {
            return Document(
                entity.id,
                entity.placeName,
                entity.categoryName,
                entity.roadAddressName,
                entity.x,
                entity.y,
                entity.rate,
                true
            )
        }

        fun fromDocumentResult(result: DocumentResult): Document {
            return Document(
                result.id,
                result.placeName,
                result.categoryName,
                result.roadAddressName,
                result.x,
                result.y,
                Random().nextFloat() * 5,
                false
            )
        }
    }
}
