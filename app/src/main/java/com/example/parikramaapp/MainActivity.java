package com.example.parikramaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.parikramaapp.home.homeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.action_home) {
                fragment = new homeFragment(); // Replace with actual fragment initialization
            } else if (id == R.id.action_explore) {
                fragment = new exploreFragment(); // Replace with actual fragment initialization
            } else if (id == R.id.action_profile) {
                fragment = new profileFragment(); // Replace with actual fragment initialization
            }
        return loadFragment(fragment);
        });

        // Load the default fragment
        if (savedInstanceState == null) {
            navigationView.setSelectedItemId(R.id.action_home);
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }
}
