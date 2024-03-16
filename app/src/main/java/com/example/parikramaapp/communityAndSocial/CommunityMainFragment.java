package com.example.parikramaapp.communityAndSocial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;
import com.example.parikramaapp.communityAndSocial.forum.ForumFragment;
import com.example.parikramaapp.communityAndSocial.news.LocalNewsFragment;
import com.example.parikramaapp.communityAndSocial.volunteering.VolunteeringFragment;

public class CommunityMainFragment extends Fragment {

    public CommunityMainFragment() {
    }

    public static CommunityMainFragment newInstance() {
        return new CommunityMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_main, container, false);

        GridView gridView = rootView.findViewById(R.id.grid_view);

        String[] options = {"Forum", "News", "Volunteering"};
        int[] icons = {R.drawable.forum_image, R.drawable.news_image, R.drawable.volunteering_image};

        GridItemAdapter adapter = new GridItemAdapter(getContext(), options, icons);

        gridView.setAdapter(adapter);

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
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ForumFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void openNewsFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, LocalNewsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void openVolunteeringFragment() {
        Toast.makeText(getContext(), "Module under development", Toast.LENGTH_SHORT).show();
    }
}
