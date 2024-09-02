package com.example.kidscript1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VideoActivity extends AppCompatActivity {

    TextView prompttext;
    ImageView promtimage;
    ImageView speakbutton;
    ProgressBar progressBar;
    String headingtext;
    String readcontent;
    TextToSpeech textToSpeech1;
    boolean isTTSInitialized = false;
    boolean isTTSPlaying = false;
    LinkedList<ImageView> linkedList;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        prompttext=findViewById(R.id.promttext);
        promtimage=findViewById(R.id.promtimage);
        speakbutton=findViewById(R.id.speaker_image);
        progressBar=findViewById(R.id.progress_bar);
        textToSpeech1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech1.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Handle language not supported error
                    } else {
                        isTTSInitialized = true;
                    }
                }
            }
        });
        Intent i=getIntent();
        linkedList = new LinkedList<>();
        headingtext=i.getStringExtra("head");
        readcontent=i.getStringExtra("result");
        prompttext.setText(headingtext);
        if(headingtext.isEmpty()){
            return;
        }
        else {
            String[] sentences = headingtext.split("\\.\\s+");
            for (String sentence : sentences) {
                callAPI(sentence);
            }
        }
        speakbutton.callOnClick();
        speakbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTTSInitialized) {
                    if (!isTTSPlaying) {
                        // Get the bot's reply from the last message
                        Glide.with(getApplicationContext())
                                .load(R.drawable.sound) // Replace with the resource ID of your animated GIF
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Use DiskCacheStrategy to cache the original GIF data
                                .into(speakbutton);
                        //String botReply = messageList.get(messageList.size() - 1).getMessage();
                        // Speak the bot's reply
                        textToSpeech1.speak(readcontent, TextToSpeech.QUEUE_FLUSH, null, null);
                        isTTSPlaying = true;
                    } else {
                        // Stop TTS when the speaker image is clicked again
                        speakbutton.setImageResource(R.drawable.muteanime);
                        textToSpeech1.stop();
                        isTTSPlaying = false;
                    }
                }
            }
        });
    }
    void callAPI(String text){
        //API CALL
        setInProgress(true);
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("prompt",text);
            jsonBody.put("size","256x256");
        }catch (Exception e){
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .header("Authorization","your api")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(getApplicationContext(),"Failed to generate image",Toast.LENGTH_LONG).show();
            }
        @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try{
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String imageUrl = jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                    loadImage(imageUrl);
                    setInProgress(false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    void setInProgress(boolean inProgress){
        runOnUiThread(()->{
            if(inProgress){
                progressBar.setVisibility(View.VISIBLE);
            }else{
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    void loadImage(String url){
        runOnUiThread(() -> {
            Picasso.get().load(url).into(promtimage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(10000); // 10 seconds
                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                    scaleAnimation.setRepeatCount(Animation.INFINITE);
                    promtimage.startAnimation(scaleAnimation);
                }
                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }
    @Override
    public void onDestroy() {
        if (textToSpeech1 != null) {
            textToSpeech1.stop();
            textToSpeech1.shutdown();
        }
        super.onDestroy();
    }
}
