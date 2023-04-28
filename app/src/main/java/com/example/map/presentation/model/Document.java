package com.example.map.presentation.model;

import com.example.map.data.local.model.DocumentEntity;
import com.example.map.data.remote.model.DocumentResult;

import net.daum.mf.map.api.MapPOIItem;

import java.util.Random;

public class Document {
    private final String id;
    private final String placeName;
    private final String categoryName;
    private final String roadAddressName;
    private final String x;
    private final String y;
    private final float rate;
    private boolean isFavorite;
    private boolean isSelected;
    private MapPOIItem mapPOIItem;

    public Document(String id, String placeName, String categoryName, String roadAddressName, String x, String y, float rate, boolean isFavorite) {
        this.id = id;
        this.placeName = placeName;
        this.categoryName = categoryName;
        this.roadAddressName = roadAddressName;
        this.x = x;
        this.y = y;
        this.rate = rate;
        this.isFavorite = isFavorite;
    }

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

    public Double getX() {
        return Double.parseDouble(x);
    }

    public Double getY() {
        return Double.parseDouble(y);
    }
    public float getRate() {
        return rate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public MapPOIItem getMapPOIItem() {
        return mapPOIItem;
    }

    public void setMapPOIItem(MapPOIItem mapPOIItem) {
        this.mapPOIItem = mapPOIItem;
    }

    public DocumentEntity toEntity() {
        return new DocumentEntity(id, placeName, categoryName, roadAddressName, x, y, rate);
    }

    public static Document fromDocumentEntity(DocumentEntity entity) {
        return new Document(
            entity.getId(),
            entity.getPlaceName(),
            entity.getCategoryName(),
            entity.getRoadAddressName(),
            entity.getX(),
            entity.getY(),
            entity.getRate(),
            true
        );
    }

    public static Document fromDocumentResult(DocumentResult result) {
        return new Document(
            result.getId(),
            result.getPlaceName(),
            result.getCategoryName(),
            result.getRoadAddressName(),
            result.getX(),
            result.getY(),
            (new Random().nextFloat() * 5),
            false
        );
    }

}
