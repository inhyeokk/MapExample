package com.example.map.domain.request;

public class SearchByAddressRequest {
    private final String query;

    public SearchByAddressRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
