package com.example.map.data.remote.model;

import com.google.gson.annotations.SerializedName;

// https://developers.kakao.com/docs/latest/ko/local/dev-guide#address-coord-documents
public class DocumentResult {
    @SerializedName("id") private String id;
    @SerializedName("place_name") private String placeName;
    @SerializedName("category_name") private String categoryName;
    @SerializedName("category_group_code") private String categoryGroupCode;
    @SerializedName("category_group_name") private String categoryGroupName;
    @SerializedName("phone") private String phone;
    @SerializedName("address_name") private String addressName;
    @SerializedName("road_address_name") private String roadAddressName;
    @SerializedName("x") private String x;
    @SerializedName("y") private String y;
    @SerializedName("place_url") private String placeUrl;
    @SerializedName("distance") private String distance;
    @SerializedName("address_type") private String addressType;
    @SerializedName("address") private Address address;
    @SerializedName("road_address") private RoadAddress roadAddress;

    public String getId() {
        return id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryGroupCode() {
        return categoryGroupCode;
    }

    public String getCategoryGroupName() {
        return categoryGroupName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getRoadAddressName() {
        return roadAddressName;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public String getDistance() {
        return distance;
    }

    public String getAddressType() {
        return addressType;
    }

    public Address getAddress() {
        return address;
    }

    public RoadAddress getRoadAddress() {
        return roadAddress;
    }
}
