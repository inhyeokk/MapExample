package com.example.map.data.remote.model

// https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-documents
data class DocumentResult(
    val id: String,
    val placeName: String,
    val categoryName: String,
    val categoryGroupCode: String,
    val categoryGroupName: String,
    val phone: String,
    val addressName: String,
    val roadAddressName: String,
    val x: String,
    val y: String,
    val placeUrl: String,
    val distance: String,
    val addressType: String,
    val address: Address,
    val roadAddress: RoadAddress,
)
