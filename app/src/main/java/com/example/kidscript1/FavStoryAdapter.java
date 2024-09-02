package com.example.kidscript1;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class FavStoryAdapter extends RecyclerView.Adapter<FavStoryAdapter.ViewHolder> {
    private List<String> stories;
    private Context context;
    public FavStoryAdapter(List<String> stories,Context context) {
        this.stories = stories;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] storyParts = stories.get(position).split("\\|");
        String timestamp = storyParts[0];
        String result = storyParts[1];
        String prompt=storyParts[2];
        holder.timestampTextView.setText(prompt+" ("+timestamp+" )");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click event
                Intent intent = new Intent(context,FavStoryDisplay.class);
                intent.putExtra("timestamp", timestamp);
                intent.putExtra("result", result);
                intent.putExtra("prompt",prompt);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return stories.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timestampTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }
}
