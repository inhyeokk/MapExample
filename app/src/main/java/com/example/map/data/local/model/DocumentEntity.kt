package com.example.map.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DocumentEntity {
    @PrimaryKey
    private final @NonNull String id;

    @ColumnInfo(name = "place_name")
    private final String placeName;

    @ColumnInfo(name = "category_name")
    private final String categoryName;

    @ColumnInfo(name = "road_address_name")
    private final String roadAddressName;

    @ColumnInfo(name = "x")
    private final String x;

    @ColumnInfo(name = "y")
    private final String y;

    @ColumnInfo(name = "rate")
    private final float rate;

    public DocumentEntity(@NonNull String id, String placeName, String categoryName, String roadAddressName, String x, String y, float rate) {
        this.id = id;
        this.placeName = placeName;
        this.categoryName = categoryName;
        this.roadAddressName = roadAddressName;
        this.x = x;
        this.y = y;
        this.rate = rate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getCategoryName() {
        return categoryName;
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

    public float getRate() {
        return rate;
    }

}
