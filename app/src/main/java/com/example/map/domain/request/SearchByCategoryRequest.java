package com.example.map.domain.request;

public class SearchByCategoryRequest {
    private String categoryGroupCode;
    private String x;
    private String y;
    private int radius;

    public SearchByCategoryRequest(String categoryGroupCode, String x, String y, int radius) {
        this.categoryGroupCode = categoryGroupCode;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public String getCategoryGroupCode() {
        return categoryGroupCode;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }
}
