package com.example.parikramaapp.communityAndSocial;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.parikramaapp.R;

public class communityMainFragment extends Fragment {

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

        // Inflate layouts for local news, forums, and volunteering
        View localNewsLayout = inflater.inflate(R.layout.fragment_local_news, container, false);
        View forumsLayout = inflater.inflate(R.layout.fragment_forum, container, false);
        View volunteeringLayout = inflater.inflate(R.layout.fragment_volunteering, container, false);

        // Add these layouts to the main layout (rootView)
        // For example, assuming you have LinearLayouts in your XML layout
        ViewGroup localNewsContainer = rootView.findViewById(R.id.local_news_container);
        ViewGroup forumsContainer = rootView.findViewById(R.id.forums_container);
        ViewGroup volunteeringContainer = rootView.findViewById(R.id.volunteering_container);

        localNewsContainer.addView(localNewsLayout);
        forumsContainer.addView(forumsLayout);
        volunteeringContainer.addView(volunteeringLayout);

        return rootView;
    }
}
