package com.example.kidscript1;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public class Fragment2 extends Fragment {
    private RecyclerView recyclerViewBooks;
    private List<Category> categoryList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);
        recyclerViewBooks = rootView.findViewById(R.id.recyclerViewBooks);

        populateCategoryList();

        BooksAdapter adapter = new BooksAdapter(categoryList, requireContext());
        recyclerViewBooks.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewBooks.setAdapter(adapter);

        adapter.setOnItemClickListener(new BooksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Category category) {
                // Handle item click here
                Intent intent = new Intent(getActivity(), CategoryContentFragment.class);
                intent.putExtra("categoryName", category.getName());
                startActivity(intent);
            }
        });

        return rootView;
    }
    private void populateCategoryList() {
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Adventure",R.drawable.advanime));
        categoryList.add(new Category("Fantasy",R.drawable.fant));
        categoryList.add(new Category("Sci-fi",R.drawable.sci));
        categoryList.add(new Category("comedy",R.drawable.comd));
        categoryList.add(new Category("Drama",R.drawable.dram));
        categoryList.add(new Category("mystery",R.drawable.myst));
        categoryList.add(new Category("super hero",R.drawable.sup));
        categoryList.add(new Category("mythology",R.drawable.myth));
        categoryList.add(new Category("thriller",R.drawable.thri));
        // Add more categories as needed
    }
}
