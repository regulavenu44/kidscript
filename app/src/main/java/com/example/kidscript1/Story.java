package com.example.kidscript1;

public class Story {
    private String title;
    private String content;

    public Story() {
        // Default constructor required for Firebase
    }

    public Story(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
