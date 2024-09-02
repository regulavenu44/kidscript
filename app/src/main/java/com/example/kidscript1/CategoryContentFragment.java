package com.example.kidscript1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;

public class CategoryContentFragment extends AppCompatActivity {

    private RecyclerView recyclerViewStories;
    private List<String> storyTitles;
    private StoriesAdapter adapter;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category_content);

        String categoryName = getIntent().getStringExtra("categoryName");

        recyclerViewStories = findViewById(R.id.recyclerViewStories);
        storyTitles = new ArrayList<>();
        adapter = new StoriesAdapter(storyTitles, this);
        recyclerViewStories.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStories.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Stories").child(categoryName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storyTitles.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String title = childSnapshot.child("Title").getValue(String.class);
                    storyTitles.add(title);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database retrieval error
            }
        });

        adapter.setOnItemClickListener(new StoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String title) {
                fetchStoryContent(categoryName, title);
            }
        });
    }

    private void fetchStoryContent(String categoryName, String storyTitle) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storyTitles.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String title1 = childSnapshot.child("Title").getValue(String.class);
                    String title2 = childSnapshot.child("Htitle").getValue(String.class);
                    String title3 = childSnapshot.child("Ttitle").getValue(String.class);
                    String content=childSnapshot.child("Content").getValue(String.class);
                    String telugucontent=childSnapshot.child("Tcontent").getValue(String.class);
                    String hindicontent=childSnapshot.child("Hcontent").getValue(String.class);
                    if(title1==storyTitle) {
                          showFullStory(storyTitle,title2,title3,content,telugucontent,hindicontent);
                    }
                    storyTitles.add(title1);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database retrieval error
            }
        });
}

     void showFullStory(String englishtitle,String hindititle,String telugutitle, String content,String telugucontent,String hindicontent) {
        Intent intent = new Intent(CategoryContentFragment.this, FullStoryActivity.class);
        intent.putExtra("englishTitle", englishtitle);
         intent.putExtra("hindiTitle", hindititle);
         intent.putExtra("teluguTitle", telugutitle);
        intent.putExtra("storyContent", content);
         intent.putExtra("storyTeluguContent", telugucontent);
         intent.putExtra("storyHindiContent", hindicontent);
        startActivity(intent);
    }
}
