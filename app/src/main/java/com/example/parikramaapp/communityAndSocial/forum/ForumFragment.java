package com.example.parikramaapp.communityAndSocial.forum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.example.parikramaapp.communityAndSocial.forum.DiscussionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ForumFragment extends Fragment {

    private RecyclerView recyclerViewForums;
    private FirebaseFirestore db;
    private ArrayList<String> forumTitles;
    private ArrayList<String> forumIds;
    private ForumAdapter adapter;
    private static final String TAG = "ForumFragment";

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance() {
        return new ForumFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forum, container, false);

        recyclerViewForums = rootView.findViewById(R.id.recyclerViewForums);
        recyclerViewForums.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseFirestore.getInstance();
        forumTitles = new ArrayList<>();
        forumIds = new ArrayList<>();
        adapter = new ForumAdapter(forumTitles);

        recyclerViewForums.setAdapter(adapter);

        // Set item click listener for the RecyclerView
        adapter.setOnItemClickListener(new ForumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    String selectedForumId = forumIds.get(position);
                    Log.d(TAG, "Selected forum ID: " + selectedForumId); // Log the selected forum ID
                    navigateToDiscussionFragment(selectedForumId);
                } catch (Exception e) {
                    Log.e(TAG, "Error on item click: ", e); // Log any exception that occurs
                    Toast.makeText(getContext(), "Error handling the forum click", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button newForumButton = rootView.findViewById(R.id.newforum);
        newForumButton.setOnClickListener(v -> showNewForumDialog());

        fetchForums();

        return rootView;
    }

    // Method to fetch forums from Firestore
    private void fetchForums() {
        db = FirebaseFirestore.getInstance();
        db.collection("forums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            forumTitles.clear();
                            forumIds.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String id = document.getId();
                                forumTitles.add(title);
                                forumIds.add(id);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Failed to fetch forums", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showNewForumDialog() {
        NewForumBottomSheetDialogFragment newForumDialog = new NewForumBottomSheetDialogFragment();
        newForumDialog.show(getChildFragmentManager(), newForumDialog.getClass().getSimpleName());
        newForumDialog.setOnDismissListener(dialog -> fetchForums());
    }

    // Method to navigate to DiscussionFragment and pass selected forum ID
    private void navigateToDiscussionFragment(String forumId) {
        DiscussionFragment discussionFragment = DiscussionFragment.newInstance(forumId);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, discussionFragment)
                .addToBackStack(null)
                .commit();
    }
}
