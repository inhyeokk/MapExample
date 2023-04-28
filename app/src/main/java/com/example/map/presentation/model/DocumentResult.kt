package com.example.map.presentation.model;

import java.util.List;

public class DocumentResult {
    private final List<Document> documentList;
    private final boolean isMoveCamera;

    public DocumentResult(List<Document> documentList, boolean isMoveCamera) {
        this.documentList = documentList;
        this.isMoveCamera = isMoveCamera;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public boolean isMoveCamera() {
        return isMoveCamera;
    }
}
