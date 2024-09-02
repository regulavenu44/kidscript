package com.example.kidscript1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class FavFragment extends Fragment {

    private DatabaseReference databaseReference;
    private List<String> storyList;
    private FavStoryAdapter storyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("FavStories");

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewStories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storyList = new ArrayList<>();
        storyAdapter = new FavStoryAdapter(storyList,requireContext());
        recyclerView.setAdapter(storyAdapter);
        // Fetch data from Firebase
        fetchDataFromFirebase();

        return view;
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String timestamp=snapshot.child("timestamp").getValue(String.class);
                    String sresult=snapshot.child("result").getValue(String.class);
                    String prompt=snapshot.child("prompt").getValue(String.class);
                    if(timestamp!=null){
                        storyList.add(timestamp+"|"+sresult+"|"+prompt);
                    }
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}

