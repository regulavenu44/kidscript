package com.example.kidscript1;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

public class Fragment3 extends Fragment {
    RecyclerView recyclerView;
    EditText messageEditText;
    ImageView sendButton;
    ImageView microphonebtn;
    ImageView copybutton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    ImageView clearchat;
    ImageView chatspeaker;
    TextToSpeech textToSpeech;
    ImageView videoplayerbutton;
    boolean isTTSInitialized = false;
    boolean isTTSPlaying = false;
    boolean isliked=false;
    String result;
    String heading;
    ImageView favimage;
    private Set<String> profanitySet;
    DatabaseReference db;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private SpeechRecognizer speechRecognizer;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    TextView userQuestionTextView; // Added TextView for user's question

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_btn);
        userQuestionTextView = view.findViewById(R.id.user_question_text_view); // Added
        clearchat=view.findViewById(R.id.clearimageView);
        chatspeaker=view.findViewById(R.id.botspeakerimage);
        copybutton=view.findViewById(R.id.copy_image);
        microphonebtn=view.findViewById(R.id.microphoneimage);
        favimage=view.findViewById(R.id.likeimage);
        favimage.setVisibility(View.GONE);
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(requireActivity());
        videoplayerbutton=view.findViewById(R.id.video_player_button);
        chatspeaker.setVisibility(View.GONE);
        initializeProfanitySet();
        userQuestionTextView.setBackgroundResource(R.drawable.white_bg);
        db = FirebaseDatabase.getInstance().getReference("Stories").child("FavStories");
        textToSpeech = new TextToSpeech(requireContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Handle language not supported error
                    } else {
                        isTTSInitialized = true;
                    }
                }
            }
        });
        // Rest of the code from SAMPLEACTIVITY

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSentence(messageEditText.getText().toString()) == 1) {
                String question = messageEditText.getText().toString().trim();
                if(question.length()>150){
                    userQuestionTextView.setText("hello");
                }
                else {
                    userQuestionTextView.setText(question); // Set user's question above RecyclerView
                }
                userQuestionTextView.setBackgroundResource(R.drawable.round_corner_user_message);
                chatspeaker.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                favimage.setVisibility(View.VISIBLE);
                isliked = false;
                favimage.setImageResource(R.drawable.white_heart);
                // Clear the previous messages
                Glide.with(requireContext())
                        .load(R.drawable.mwv) // Replace with the resource ID of your animated GIF
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Use DiskCacheStrategy to cache the original GIF data
                        .into(sendButton);
                // Assuming you're inside an Activity or Fragment
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Replace with another image
                        sendButton.setImageResource(R.drawable.magic_wand_pic);
                    }
                }, 6000);
                messageList.clear();
                messageAdapter.notifyDataSetChanged();
                callAPI(question);
            }
                else{
                    addResponse("This content may violate our content policy. If you believe this to be in error, please submit your feedback — your input will aid our research in this area.");
                }
        }
        });
        microphonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  promptSpeechInput();
            }
        });
        clearchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userQuestionTextView.setText("");
                userQuestionTextView.setBackgroundResource(R.drawable.white_bg);
                chatspeaker.setVisibility(View.GONE);
                favimage.setVisibility(View.GONE);
                Glide.with(requireContext())
                        .load(R.drawable.trash) // Replace with the resource ID of your animated GIF
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Use DiskCacheStrategy to cache the original GIF data
                        .into(clearchat);
                recyclerView.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Replace with another image
                        clearchat.setImageResource(R.drawable.trash_pic);
                    }
                }, 3000);
            }
        });
        chatspeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTTSInitialized) {
                    if (!isTTSPlaying) {
                        // Get the bot's reply from the last message
                        Glide.with(requireContext())
                                .load(R.drawable.sound) // Replace with the resource ID of your animated GIF
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Use DiskCacheStrategy to cache the original GIF data
                                .into(chatspeaker);
                        String botReply = messageList.get(messageList.size() - 1).getMessage();
                        // Speak the bot's reply
                        textToSpeech.speak(botReply, TextToSpeech.QUEUE_FLUSH, null, null);
                        isTTSPlaying = true;
                    } else {
                        // Stop TTS when the speaker image is clicked again
                        chatspeaker.setImageResource(R.drawable.muteanime);
                        textToSpeech.stop();
                        isTTSPlaying = false;
                    }
                }
            }
        });
        favimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isliked) {
                    // The result is already stored, delete it
                    deleteResultFromDatabase();
                } else {
                    // The result is not stored, add it
                    isliked = true;
                    Glide.with(requireContext())
                            .load(R.drawable.heart_anime)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into(favimage);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            favimage.setImageResource(R.drawable.red_heart);
                        }
                    }, 3000);
                    if (result != null) {
                        String timestamp = getCurrentTimestamp();
                        String prompt=userQuestionTextView.getText().toString();
                        saveResultToDatabase(result, timestamp,prompt);
                    }
                }
            }
        });
        videoplayerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),VideoActivity.class);
                heading=userQuestionTextView.getText().toString();
                i.putExtra("head",heading);
                i.putExtra("result",result);
                startActivity(i);
            }
        });
        copybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", result);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                }
                Glide.with(requireContext())
                        .load(R.drawable.copy)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(copybutton);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        copybutton.setImageResource(R.drawable.copy);
                    }
                }, 3000);
            }
        });

        return view;
    }
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    // Define methods like callAPI as they were in SAMPLEACTIVITY
    void addToChat(String message,String sentBy){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        if (messageList != null && !messageList.isEmpty()) {
            messageList.remove(messageList.size() - 1);
        }
        addToChat(response, Message.SENT_BY_BOT);
    }


    private void callAPI(String question) {
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", BuildConfig.apiKey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(question) // Use the question as the prompt
                .build();

        Executor executor = Executors.newSingleThreadExecutor(); // Executor to handle async task

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result1) {
                String resultText = result1.getText();
                // Update UI with the API response
                if (resultText != null && !resultText.isEmpty()) {
                    addResponse(resultText);
                    result=resultText;
                    favimage.setVisibility(View.VISIBLE); // Show the favorite button
                } else {
                    addResponse("Empty response received from API.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                // Handle the failure scenario
                addResponse("Failed to generate content. Please try again.");
            }
        }, executor);
    }
    private void saveResultToDatabase(String result, String timestamp,String prompt) {
        // Create a reference to the "FavStories" node in your database
        DatabaseReference favref = FirebaseDatabase.getInstance().getReference("FavStories");

        // Generate a unique key for the new FavStory entry
        String key = favref.push().getKey();

        // Create a new child node under "FavStories" for the current timestamp
        DatabaseReference storyRef = favref.child(key);

        // Set the "result" and "timestamp" as values under the current timestamp node
        storyRef.child("result").setValue(result);
        storyRef.child("timestamp").setValue(timestamp);
        storyRef.child("prompt").setValue(prompt);
 //       storyRef.child("newnode").setValue(prompt+timestamp);
    }
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        LocalDate localDate = null;
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayName;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                dayName = "Sunday";
                break;
            case Calendar.MONDAY:
                dayName = "Monday";
                break;
            case Calendar.TUESDAY:
                dayName = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayName = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayName = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayName = "Friday";
                break;
            case Calendar.SATURDAY:
                dayName = "Saturday";
                break;
            default:
                dayName = "Unknown";
                break;
        }
        return sdf.format(new Date())+" "+dayName;
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            // Handle exception when speech recognition is not supported on the device
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && result.size() > 0) {
                    String spokenText = result.get(0);
                    messageEditText.setText(spokenText);
                }
            }
        }
    }

    private void deleteResultFromDatabase() {
        DatabaseReference favref = FirebaseDatabase.getInstance().getReference("FavStories");

        // Query to find the specific node based on "result" value
        favref.orderByChild("result").equalTo(result).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Remove the node if the "result" matches
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
        isliked = false;
        favimage.setImageResource(R.drawable.white_heart);
    }
    private void initializeProfanitySet() {
        // Retrieve the array from resources
        String[] profanityArray = getResources().getStringArray(R.array.profanity_array);
        // Initialize the set for efficient lookup
        profanitySet = new HashSet<>(Arrays.asList(profanityArray));
    }

    private int checkSentence(String checking_text) {
        String sentence = checking_text.toLowerCase(); // Convert to lowercase for case-insensitive comparison
        String[] words = sentence.split("\\s+"); // Split the sentence into words
        for (String word : words) {
            if (profanitySet.contains(word)) {
                return 0;
            }
        }
        return 1;
    }
}