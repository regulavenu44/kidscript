package com.example.kidscript1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.ViewCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment currentFragment = new Fragment1();
    private Animation fadeIn;
    private Animation fadeOut;
    private boolean isAnimating = false;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        // Check if the user is authenticated
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            // User is not authenticated, open LoginActivity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // Finish the current activity
        }

        bottomNavigationView = findViewById(R.id.bnv);
        frameLayout = findViewById(R.id.frame_layout);
        Fragment1 f=new Fragment1();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frame_layout,f);
        ft.commit();
        bottomNavigationView.setSelectedItemId(R.id.books);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (!isAnimating) {
                    int itemId = item.getItemId();
                    Fragment selectedFragment = null;
                    // Check if the selected fragment is different from the current fragment
                    if (itemId == R.id.home && !(currentFragment instanceof Fragment1)) {
                        selectedFragment = new Fragment1();
                    } else if (itemId == R.id.books && !(currentFragment instanceof Fragment2)) {
                        selectedFragment = new Fragment2();
                    } else if (itemId == R.id.generate && !(currentFragment instanceof Fragment3)) {
                        selectedFragment = new Fragment3();
                    } else if (itemId == R.id.profile && !(currentFragment instanceof Fragment4)) {
                        selectedFragment = new Fragment4();
                    }
                    if (selectedFragment != null) {
                        isAnimating = true;
                        // Apply custom animations to the fragments
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setCustomAnimations(
                                R.anim.slide_in_right,   // Enter animation
                                R.anim.slide_out_left,   // Exit animation
                                R.anim.slide_in_left,    // Pop enter animation
                                R.anim.slide_out_right   // Pop exit animation
                        );

                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        // Delay animation flag reset after the animation duration
                        frameLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isAnimating = false;
                            }
                        }, getResources().getInteger(android.R.integer.config_mediumAnimTime));

                        // Update the current fragment
                        currentFragment = selectedFragment;
                    }

                }
                return true;
            }
        });
    }
    private void setFragment(Fragment fragment) {
        isAnimating = true;
        // Apply custom animations to the fragments
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,   // Enter animation
                R.anim.slide_out_left,   // Exit animation
                R.anim.slide_in_left,    // Pop enter animation
                R.anim.slide_out_right   // Pop exit animation
        );

        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Delay animation flag reset after the animation duration
        frameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                isAnimating = false;
            }
        }, getResources().getInteger(android.R.integer.config_mediumAnimTime));

        // Update the current fragment
        currentFragment = fragment;
    }
}
