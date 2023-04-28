package com.example.map.data.remote.model;

import com.google.gson.annotations.SerializedName;

// https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-documents-road-address
public class RoadAddress {
    @SerializedName("address_name") private String addressName;
    @SerializedName("region_1depth_name") private String region1DepthName;
    @SerializedName("region_2depth_name") private String region2DepthName;
    @SerializedName("region_3depth_name") private String region3DepthName;
    @SerializedName("road_name") private String roadName;
    @SerializedName("underground_yn") private String undergroundYn;
    @SerializedName("main_building_no") private String mainBuildingNo;
    @SerializedName("sub_building_no") private String subBuildingNo;
    @SerializedName("building_name") private String buildingName;
    @SerializedName("zone_no") private String zoneNo;
    @SerializedName("x") private String x;
    @SerializedName("y") private String y;

    public String getAddressName() {
        return addressName;
    }

    public String getRegion1DepthName() {
        return region1DepthName;
    }

    public String getRegion2DepthName() {
        return region2DepthName;
    }

    public String getRegion3DepthName() {
        return region3DepthName;
    }

    public String getRoadName() {
        return roadName;
    }

    public String getUndergroundYn() {
        return undergroundYn;
    }

    public String getMainBuildingNo() {
        return mainBuildingNo;
    }

    public String getSubBuildingNo() {
        return subBuildingNo;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getZoneNo() {
        return zoneNo;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }
}
