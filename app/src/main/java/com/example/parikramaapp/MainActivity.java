package com.example.parikramaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.parikramaapp.communityAndSocial.forum.DiscussionFragment;
import com.example.parikramaapp.communityAndSocial.forum.ForumFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create instance of your fragment
        ForumFragment forumFragment = new ForumFragment();

        // Get fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Start a transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the content of the container with your fragment
        fragmentTransaction.replace(R.id.fragment_container, forumFragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}