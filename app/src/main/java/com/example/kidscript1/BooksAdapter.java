package com.example.kidscript1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private List<Category> categoryList; // Replace 'Category' with your actual data model
    private OnItemClickListener listener;
    public ImageView categoryImageView;
    private Context context;

    public BooksAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(int position, Category category);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryView = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);

        if (category != null) {
            // Set data to views in the ViewHolder
            String categoryName = category.getName();
            int imageResource = category.getImageResource(); // Get the image resource ID

            if (categoryName != null) {
                holder.categoryNameTextView.setText(categoryName);
            }

            if (imageResource != 0) {
                // Load the category image from the resource ID
                //categoryImageView.setImageResource(imageResource);
                Glide.with(context)
                        .load(imageResource) // Replace with the resource ID of your animated GIF
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Use DiskCacheStrategy to cache the original GIF data
                        .into(categoryImageView);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(position, category);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryNameTextView;
        public LinearLayout bgimageofbox;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryName);
            categoryImageView = itemView.findViewById(R.id.categoryImage);

        }
    }
}
