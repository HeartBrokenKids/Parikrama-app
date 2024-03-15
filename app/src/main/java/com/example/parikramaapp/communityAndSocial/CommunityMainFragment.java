package com.example.parikramaapp.communityAndSocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;
import com.example.parikramaapp.communityAndSocial.forum.ForumFragment;
import com.example.parikramaapp.communityAndSocial.news.LocalNewsFragment;
import com.example.parikramaapp.communityAndSocial.volunteering.VolunteeringFragment;

public class CommunityMainFragment extends Fragment {

    public CommunityMainFragment() {
        // Required empty public constructor
    }

    public static CommunityMainFragment newInstance() {
        return new CommunityMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_main, container, false);

        // Get references to the GridView
        GridView gridView = rootView.findViewById(R.id.grid_view);

        // Create an array of options and icons
        String[] options = {"Forum", "News", "Volunteering"};
        int[] icons = {R.drawable.forum_image, R.drawable.news_image, R.drawable.volunteering_image};

        // Create a custom adapter to populate the GridView
        GridItemAdapter adapter = new GridItemAdapter(getContext(), options, icons);

        // Set the adapter for the GridView
        gridView.setAdapter(adapter);

        // Set item click listener to open corresponding fragment
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        openForumFragment();
                        break;
                    case 1:
                        openNewsFragment();
                        break;
                    case 2:
                        openVolunteeringFragment();
                        break;
                }
            }
        });

        return rootView;
    }

    private void openForumFragment() {
        // Replace the current fragment with the ForumFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ForumFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void openNewsFragment() {
        // Replace the current fragment with the LocalNewsFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, LocalNewsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void openVolunteeringFragment() {
        // Replace the current fragment with the VolunteeringFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new VolunteeringFragment())
                .addToBackStack(null)
                .commit();
    }
}
