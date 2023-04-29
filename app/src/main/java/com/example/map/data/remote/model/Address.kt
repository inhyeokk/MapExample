package com.example.map.data.remote.model

// https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-documents-address
data class Address(
    val addressName: String,
    val region1depthName: String,
    val region2depthName: String,
    val region3depthName: String,
    val region3depthHName: String,
    val hCode: String,
    val bCode: String,
    val mountainYn: String,
    val mainAddressNo: String,
    val x: String,
    val y: String,
)
