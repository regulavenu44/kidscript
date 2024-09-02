package com.example.kidscript1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class YourAdapter extends RecyclerView.Adapter<YourAdapter.YourViewHolder> {
    private List<YourDataModel> yourDataList;

    public YourAdapter(List<YourDataModel> yourDataList) {
        this.yourDataList = yourDataList;
    }

    @NonNull
    @Override
    public YourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_item_layout, parent, false);
        return new YourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourViewHolder holder, int position) {
        YourDataModel data = yourDataList.get(position);
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return yourDataList.size();
    }

    public static class YourViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemName;

        public YourViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
        }

        public void bindData(YourDataModel data) {
            itemImage.setImageResource(data.getImageResource());
            itemName.setText(data.getName());
        }
    }
}
