package com.example.map.presentation.view.main.entity;

public class SelectPosition {
    private int oldPosition = -1;
    private int position = -1;

    public SelectPosition() {

    }

    public SelectPosition(int position) {
        this.position = position;
    }

    public SelectPosition(int oldPosition, int position) {
        this.oldPosition = oldPosition;
        this.position = position;
    }

    public int getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
