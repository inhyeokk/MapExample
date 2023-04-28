package com.example.map.data.remote.model;

import com.google.gson.annotations.SerializedName;

// https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-documents-address
public class Address {
    @SerializedName("address_name") private String addressName;
    @SerializedName("region_1depth_name") private String region1DepthName;
    @SerializedName("region_2depth_name") private String region2DepthName;
    @SerializedName("region_3depth_name") private String region3DepthName;
    @SerializedName("region_3depth_h_name") private String region3DepthHName;
    @SerializedName("h_code") private String hCode;
    @SerializedName("b_code") private String bCode;
    @SerializedName("mountain_yn") private String mountainYn;
    @SerializedName("main_address_no") private String mainAddressNo;
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

    public String getRegion3DepthHName() {
        return region3DepthHName;
    }

    public String gethCode() {
        return hCode;
    }

    public String getbCode() {
        return bCode;
    }

    public String getMountainYn() {
        return mountainYn;
    }

    public String getMainAddressNo() {
        return mainAddressNo;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }
}
