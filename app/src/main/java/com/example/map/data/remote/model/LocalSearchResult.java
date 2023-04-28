package com.example.map.data.remote.model;

import java.util.List;

public class LocalSearchResult {
    private Meta meta;
    private List<DocumentResult> documents;

    public Meta getMeta() {
        return meta;
    }

    public List<DocumentResult> getDocuments() {
        return documents;
    }
}
