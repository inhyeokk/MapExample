package com.example.map.data.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegionInfo {
    @SerializedName("region") private List<String> region;
    @SerializedName("keyword") private String keyword;
    @SerializedName("selected_region") private String selectedRegion;

    public List<String> getRegion() {
        return region;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getSelectedRegion() {
        return selectedRegion;
    }
}
