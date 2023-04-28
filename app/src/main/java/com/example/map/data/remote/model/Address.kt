package com.example.map.data.remote.model

import com.google.gson.annotations.SerializedName

// https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-documents-address
data class Address(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("region_1depth_name") val region1DepthName: String,
    @SerializedName("region_2depth_name") val region2DepthName: String,
    @SerializedName("region_3depth_name") val region3DepthName: String,
    @SerializedName("region_3depth_h_name") val region3DepthHName: String,
    @SerializedName("h_code") val hCode: String,
    @SerializedName("b_code") val bCode: String,
    @SerializedName("mountain_yn") val mountainYn: String,
    @SerializedName("main_address_no") val mainAddressNo: String,
    @SerializedName("x") val x: String?,
    @SerializedName("y") val y: String,
)
