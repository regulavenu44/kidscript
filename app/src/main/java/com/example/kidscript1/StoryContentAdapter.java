package com.example.kidscript1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StoryContentAdapter extends RecyclerView.Adapter<StoryContentAdapter.ViewHolder> {
    private List<String> storyContentList;

    public StoryContentAdapter(List<String> storyContentList) {
        this.storyContentList = storyContentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String storyContent = storyContentList.get(position);
        holder.storyContentTextView.setText(storyContent);
    }

    @Override
    public int getItemCount() {
        return storyContentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView storyContentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyContentTextView = itemView.findViewById(R.id.storyContentTextView);
        }
    }
}
