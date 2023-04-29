package com.example.map.data.remote.model

// https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-documents-road-address
data class RoadAddress(
    val addressName: String,
    val region1depthName: String,
    val region2depthName: String,
    val region3depthName: String,
    val roadName: String,
    val undergroundYn: String,
    val mainBuildingNo: String,
    val subBuildingNo: String,
    val buildingName: String,
    val zoneNo: String,
    val x: String,
    val y: String,
)
