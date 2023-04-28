package com.example.map.presentation.model;

import com.example.map.data.remote.model.DocumentResult;

import java.io.Serializable;

public class SearchResult implements Serializable {
    private final String addressName;
    private final String addressType;
    private final String x;
    private final String y;

    public SearchResult(String addressName, String addressType, String x, String y) {
        this.addressName = addressName;
        this.addressType = addressType;
        this.x = x;
        this.y = y;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getAddressType() {
        return addressType;
    }

    public Double getX() {
        return Double.parseDouble(x);
    }

    public Double getY() {
        return Double.parseDouble(y);
    }

    public static SearchResult fromDocumentResult(DocumentResult result) {
        return new SearchResult(
            result.getAddressName(),
            result.getAddressType(),
            result.getX(),
            result.getY()
        );
    }
}
