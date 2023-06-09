package com.example.map.presentation.model

data class DocumentResult(val documentList: List<Document>, val isMoveCamera: Boolean) {
    companion object {
        fun empty() = DocumentResult(documentList = emptyList(), isMoveCamera = false)
    }
}
