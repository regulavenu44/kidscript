package com.example.kidscript1;

// BoxModel.java
public class BoxModel {
    private int imageResourceId;
    private String name;

    public BoxModel(int imageResourceId, String name) {
        this.imageResourceId = imageResourceId;
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getName() {
        return name;
    }
}
