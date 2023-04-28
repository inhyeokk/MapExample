package com.example.map.data.remote.model

import com.google.gson.annotations.SerializedName

data class RegionInfo(
    @SerializedName("region") val region: List<String>,
    @SerializedName("keyword") val keyword: String,
    @SerializedName("selected_region") val selectedRegion: String
)
