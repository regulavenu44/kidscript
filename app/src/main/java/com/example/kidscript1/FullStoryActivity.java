package com.example.kidscript1;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FullStoryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    public Spinner languageSpinner;
    private RecyclerView recyclerViewStoryContent;
    private StoryContentAdapter storyContentAdapter;
    TextView storyTitle;
    ImageView speakerimage;
    String title;
    String telugutitle;
    String hindititle;
    String content;
    String telugucontent;
    String hindicontent;
    private DatabaseReference dbr;
    List<String> storyContentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);
        speakerimage=findViewById(R.id.speakerbutton1);
        setanime1();
        languageSpinner = findViewById(R.id.languageSpinner);
        recyclerViewStoryContent = findViewById(R.id.recyclerViewStoryContent);
        storyContentList = new ArrayList<>();
        storyContentAdapter = new StoryContentAdapter(storyContentList);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
        final boolean[] isSpeaking = {false};
        storyTitle = findViewById(R.id.storyTitle);

        textToSpeech = new TextToSpeech(this, this);

        // Retrieve the story title and content from the intent
        title = getIntent().getStringExtra("englishTitle");
        telugutitle = getIntent().getStringExtra("teluguTitle");
        hindititle = getIntent().getStringExtra("hindiTitle");
        content = getIntent().getStringExtra("storyContent");
        telugucontent = getIntent().getStringExtra("storyTeluguContent");
        hindicontent = getIntent().getStringExtra("storyHindiContent");
        // Set the story title in the TextView
        storyTitle.setText(title);
        ////////////////////////////
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLanguage = languageSpinner.getSelectedItem().toString();
                // Default to English
                if (selectedLanguage.equals("English")) {
                    storyContentList.clear();
                    dothis();
                } else if (selectedLanguage.equals("Hindi")) {
                    storyContentList.clear();
                    dothis1();
                }
                else{
                    storyContentList.clear();
                    dothis2();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ////////////////////////////
        //storyContentList.add(telugucontent);
        //recyclerViewStoryContent.setAdapter(storyContentAdapter);
        //recyclerViewStoryContent.setLayoutManager(new LinearLayoutManager(this));
        speakerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSpeaking[0]) {
                    // Start speaking the story content
                    //Glide.with(this).load(R.drawable.sound).asGif().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(speakerimage);
                    //speakerimage.setImageResource(R.drawable.sound);
                    setanime();
                    String selectedLanguage = languageSpinner.getSelectedItem().toString();
                    Locale selectedLocale = new Locale("en");
                    textToSpeech.setLanguage(selectedLocale);
                    textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                    // Default to English
                    if (selectedLanguage.equals("Hindi")) {
                        selectedLocale = new Locale("hi");
                        textToSpeech.setLanguage(selectedLocale);
                        textToSpeech.speak(hindicontent, TextToSpeech.QUEUE_FLUSH, null);
                    } else if (selectedLanguage.equals("Telugu")) {
                        textToSpeech.setLanguage(selectedLocale);
                        textToSpeech.speak(telugucontent, TextToSpeech.QUEUE_FLUSH, null);
                        selectedLocale = new Locale("te");
                    }
                    //textToSpeech.setLanguage(selectedLocale);
                    //textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                    isSpeaking[0] = true;

                } else {
                    // Stop speaking
                    textToSpeech.stop();
                    isSpeaking[0] = false;
                    setanime1();
                }
            }
        });
        /*speakerimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ismute)
                {
                   speakerimage.setImageResource(R.drawable.speaker);
                   ismute=false;
                }
                else
                {
                   speakerimage.setImageResource(R.drawable.mute);
                   ismute=true;
                }
            }
        });*/
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // TTS engine is ready, you can set the language and other options here.
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
    void dothis()
    {
        storyTitle.setText(title);
        storyContentList.add(content);
        recyclerViewStoryContent.setAdapter(storyContentAdapter);
        recyclerViewStoryContent.setLayoutManager(new LinearLayoutManager(this));
    }
    void dothis1()
    {
        storyTitle.setText(hindititle);
        storyContentList.add(hindicontent);
        recyclerViewStoryContent.setAdapter(storyContentAdapter);
        recyclerViewStoryContent.setLayoutManager(new LinearLayoutManager(this));
    }
    void dothis2()
    {
        storyTitle.setText(telugutitle);
        storyContentList.add(telugucontent);
        recyclerViewStoryContent.setAdapter(storyContentAdapter);
        recyclerViewStoryContent.setLayoutManager(new LinearLayoutManager(this));
    }
    void setanime()
    {
        Glide.with(this)
                .load(R.drawable.sound) // Replace with the resource ID of your animated GIF
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Use DiskCacheStrategy to cache the original GIF data
                .into(speakerimage);
    }
    void setanime1()
    {
        Glide.with(this)
                .load(R.drawable.muteanime) // Replace with the resource ID of your animated GIF
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Use DiskCacheStrategy to cache the original GIF data
                .into(speakerimage);
    }
}
