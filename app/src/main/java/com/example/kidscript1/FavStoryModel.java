package com.example.kidscript1;

public class FavStoryModel {
    private String result;
    private long timestamp; // Assuming timestamp is a long value

    public FavStoryModel() {
        // Default constructor required for Firebase
    }

    public FavStoryModel(String result, long timestamp) {
        this.result = result;
        this.timestamp = timestamp;
    }

    public String getResult() {
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
