package com.example.kidscript1;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
public class Fragment1 extends Fragment {
    androidx.constraintlayout.widget.ConstraintLayout cl1;
    ProgressBar pbar;
    Button checkinbtn;
    LinearLayout ll1,ll2,ll3,ll4;
    ImageView im1,im2,im3,im4;
    TextView tv1,tv2,tv3,tv4;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        cl1=view.findViewById(R.id.contraintlayout1);
        pbar=view.findViewById(R.id.checkInProgressBar);
        checkinbtn=view.findViewById(R.id.checkInButton);
        ll1=view.findViewById(R.id.linearLayout1);
        ll2=view.findViewById(R.id.linearLayout2);
        ll3=view.findViewById(R.id.linearLayout3);
        ll4=view.findViewById(R.id.linearLayout4);
        im1=view.findViewById(R.id.boxImage1);
        im2=view.findViewById(R.id.boxImage2);
        im3=view.findViewById(R.id.boxImage3);
        im4=view.findViewById(R.id.boxImage4);
        tv1=view.findViewById(R.id.boxName1);
        tv2=view.findViewById(R.id.boxName2);
        tv3=view.findViewById(R.id.boxName3);
        tv4=view.findViewById(R.id.boxName4);
        // Define the animation
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_right);
        view.startAnimation(anim);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavFragment favfag=new FavFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,favfag);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();
            }
        });
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavFragment favfag=new FavFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,favfag);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();

            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Fragment2 f2=new Fragment2();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,f2);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment2 f2=new Fragment2();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,f2);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();

            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment3 f3=new Fragment3();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,f3);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment3 f3=new Fragment3();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,f3);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();

            }
        });
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment4 f4=new Fragment4();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,f4);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment4 f4=new Fragment4();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout,f4);
                transaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                transaction.commit();

            }
        });
        return view;
    }
}
