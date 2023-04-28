package com.example.map.data.remote.model

import com.google.gson.annotations.SerializedName

// https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-documents-road-address
data class RoadAddress(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("region_1depth_name") val region1DepthName: String,
    @SerializedName("region_2depth_name") val region2DepthName: String,
    @SerializedName("region_3depth_name") val region3DepthName: String,
    @SerializedName("road_name") val roadName: String,
    @SerializedName("underground_yn") val undergroundYn: String,
    @SerializedName("main_building_no") val mainBuildingNo: String,
    @SerializedName("sub_building_no") val subBuildingNo: String,
    @SerializedName("building_name") val buildingName: String,
    @SerializedName("zone_no") val zoneNo: String,
    @SerializedName("x") val x: String,
    @SerializedName("y") val y: String,
)
