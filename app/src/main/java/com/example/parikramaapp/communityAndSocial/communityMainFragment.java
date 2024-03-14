package com.example.parikramaapp.communityAndSocial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;

import java.util.ArrayList;

public class communityMainFragment extends Fragment {

    private ListView newsListView;

    public communityMainFragment() {
        // Required empty public constructor
    }

    public static communityMainFragment newInstance() {
        return new communityMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community_main, container, false);

        // Initialize views
        newsListView = rootView.findViewById(R.id.news_list_view);

        // Dummy data for testing
        ArrayList<String> newsList = new ArrayList<>();
        newsList.add("News 1: This is a sample news article.");
        newsList.add("News 2: Another sample news article.");
        newsList.add("News 3: Yet another sample news article.");

        // Create adapter and set it to ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, newsList);
        newsListView.setAdapter(adapter);

        return rootView;
    }
}
