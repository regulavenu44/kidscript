package com.example.kidscript1;

public class FavStories {
    private String result;
    private String timestamp;

    public FavStories() {
        // Default constructor required for calls to DataSnapshot.getValue(FavStories.class)
    }

    public FavStories(String result, String timestamp) {
        this.result = result;
        this.timestamp = timestamp;
    }

    public String getResult() {
        return result;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
