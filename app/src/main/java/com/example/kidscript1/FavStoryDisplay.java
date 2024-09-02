package com.example.kidscript1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FavStoryDisplay extends AppCompatActivity {
    TextView timestamptextview,resultstory,prompttext;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_story_display);
        timestamptextview=findViewById(R.id.timestampTextView);
        resultstory=findViewById(R.id.resultTextView);
        prompttext=findViewById(R.id.promptTextView);
        Intent intent = getIntent();
        if (intent != null) {
            String timestamp = intent.getStringExtra("timestamp");
            String result = intent.getStringExtra("result");
            String prompt=intent.getStringExtra("prompt");
            timestamptextview.setText(timestamp);
            resultstory.setText(result);
            prompttext.setText(prompt);
        }
    }
}