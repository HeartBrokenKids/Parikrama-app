package com.example.parikramaapp.communityAndSocial.forum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DiscussionFragment extends Fragment implements AddDiscussionBottomSheet.DiscussionCreationListener{
    private FirebaseFirestore db;
    private ArrayList<String> discussionTitles;
    private ArrayList<String> discussionIds;
    private RecyclerView recyclerViewDiscussions;
    private DiscussionAdapter adapter;
    private String forumId;

    public DiscussionFragment() {
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

        if (getArguments() != null) {
            forumId = getArguments().getString("forumId"); // Retrieve forumId here
        }

        Button newDiscussionButton = rootView.findViewById(R.id.buttonPostComment);
        newDiscussionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewDiscussionBottomSheet();
            }
        });

        recyclerViewDiscussions = rootView.findViewById(R.id.recyclerViewDiscussions);
        recyclerViewDiscussions.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseFirestore.getInstance();
        discussionTitles = new ArrayList<>();
        discussionIds = new ArrayList<>();
        adapter = new DiscussionAdapter(getContext(), discussionTitles, discussionIds);

        recyclerViewDiscussions.setAdapter(adapter);

        adapter.setClickListener(new DiscussionAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String discussionId) {
                openDiscussionDetailsFragment(discussionId);
            }
        });

        fetchDiscussions();

        return rootView;
    }

    private void showNewDiscussionBottomSheet() {
        AddDiscussionBottomSheet newDiscussionBottomSheetDialogFragment = AddDiscussionBottomSheet.newInstance(forumId);
        newDiscussionBottomSheetDialogFragment.setDiscussionCreationListener(this);
        newDiscussionBottomSheetDialogFragment.show(getChildFragmentManager(), newDiscussionBottomSheetDialogFragment.getClass().getSimpleName());
    }

    private void openDiscussionDetailsFragment(String discussionId) {
        if (forumId != null && !forumId.isEmpty()) {
            DiscussionDetailsFragment fragment = DiscussionDetailsFragment.newInstance(forumId, discussionId);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);

            transaction.commit();
        } else {
            Toast.makeText(getContext(), "Error: Forum ID is not available.", Toast.LENGTH_SHORT).show();
            Log.e("DiscussionFragment", "Error: Forum ID is not provided or is empty.");
        }
    }

    private void fetchDiscussions() {
        if (getArguments() != null) {
            String forumId = getArguments().getString("forumId");

            discussionTitles.clear();
            discussionIds.clear();

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


    @Override
    public void onDiscussionCreated() {
        fetchDiscussions();
    }
}
