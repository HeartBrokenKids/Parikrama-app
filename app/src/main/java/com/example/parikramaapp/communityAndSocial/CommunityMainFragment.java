package com.example.parikramaapp.communityAndSocial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;

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
        return rootView;
    }
}
