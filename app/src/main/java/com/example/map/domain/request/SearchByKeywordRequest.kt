package com.example.map.domain.request;

public class SearchByKeywordRequest {
    private final String query;
    private final String x;
    private final String y;
    private final int radius;

    public SearchByKeywordRequest(String query, String x, String y, int radius) {
        this.query = query;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public String getQuery() {
        return query;
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
