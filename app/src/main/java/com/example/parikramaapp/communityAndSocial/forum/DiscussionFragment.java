package com.example.parikramaapp.communityAndSocial.forum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DiscussionFragment extends Fragment {

    private ListView listViewDiscussions;
    private FirebaseFirestore db;
    private ArrayList<String> discussionTitles;
    private ArrayList<String> discussionIds;
    private RecyclerView recyclerViewDiscussions;
    private DiscussionAdapter adapter;

    public DiscussionFragment() {
        // Required empty public constructor
    }

    public static DiscussionFragment newInstance(String forumId) {
        DiscussionFragment fragment = new DiscussionFragment();
        Bundle args = new Bundle();
        args.putString("forumId", forumId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discussion, container, false);

        recyclerViewDiscussions = rootView.findViewById(R.id.recyclerViewDiscussions);
        recyclerViewDiscussions.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseFirestore.getInstance();
        discussionTitles = new ArrayList<>();
        discussionIds = new ArrayList<>();
        adapter = new DiscussionAdapter(getContext(), discussionTitles, discussionIds);

        recyclerViewDiscussions.setAdapter(adapter);

        // Set click listener
        adapter.setClickListener(new DiscussionAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String discussionId = adapter.getItem(position);
                openDiscussionDetailsFragment(discussionId);
            }
        });

        // Fetch discussions
        fetchDiscussions();

        return rootView;
    }

    private void fetchDiscussions() {
        if (getArguments() != null) {
            String forumId = getArguments().getString("forumId");
            db.collection("forums").document(forumId).collection("discussions")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String title = document.getString("title");
                                    String discussionId = document.getId();
                                    discussionTitles.add(title);
                                    discussionIds.add(discussionId);
                                }
                                Log.d("DiscussionFragment", "Number of discussions fetched: " + discussionTitles.size());
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.e("DiscussionFragment", "Error getting documents: ", task.getException());
                                Toast.makeText(getContext(), "Failed to fetch discussions", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Forum ID is null", Toast.LENGTH_SHORT).show();
        }
    }




    private void openDiscussionDetailsFragment(String discussionId) {
        // Get the selected forum ID again from arguments to pass along with discussion ID
        String forumId = getArguments() != null ? getArguments().getString("forumId") : "";

        // Check if the forum ID is not null or empty
        if (forumId != null && !forumId.isEmpty()) {
            DiscussionDetailsFragment fragment = DiscussionDetailsFragment.newInstance(forumId, discussionId);
            // Navigate to the DiscussionDetailsFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.forums_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "Error: Forum ID is not available.", Toast.LENGTH_SHORT).show();
        }
    }
}
