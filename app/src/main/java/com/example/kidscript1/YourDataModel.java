package com.example.kidscript1;

public class YourDataModel {
    private String name;
    private int imageResource;

    public YourDataModel(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}
