package com.example.parikramaapp.communityAndSocial.forum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiscussionDetailsFragment extends Fragment {

    private static final String ARG_FORUM_ID = "forum_id";
    private static final String ARG_DISCUSSION_ID = "discussion_id";

    private String mForumId;
    private String mDiscussionId;
    private TextView textViewDiscussionTitle, textViewDiscussionContent, textViewComments;
    private List<String> mComments = new ArrayList<>();


    public DiscussionDetailsFragment() {
        // Required empty public constructor
    }

    public static DiscussionDetailsFragment newInstance(String forumId, String discussionId) {
        DiscussionDetailsFragment fragment = new DiscussionDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FORUM_ID, forumId);
        args.putString(ARG_DISCUSSION_ID, discussionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForumId = getArguments().getString(ARG_FORUM_ID);
            mDiscussionId = getArguments().getString(ARG_DISCUSSION_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discussion_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        textViewDiscussionTitle = view.findViewById(R.id.textViewDiscussionTitle);
        textViewDiscussionContent = view.findViewById(R.id.textViewDiscussionContent);
        textViewComments = view.findViewById(R.id.textViewComments);

        if (mForumId != null && mDiscussionId != null) {
            Log.d("DiscussionDetails", "Fetching details for forumId: " + mForumId + " and discussionId: " + mDiscussionId);
            fetchDiscussionDetails(mForumId, mDiscussionId);
            fetchComments(mForumId, mDiscussionId);
        } else {
            Log.e("DiscussionDetails", "The forumId or discussionId is not provided.");
            Toast.makeText(getContext(), "Error: The forumId or discussionId is missing.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchDiscussionDetails(String forumId, String discussionId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("DiscussionDetails", "Attempting to fetch discussion details from Firestore");
        db.collection("forums").document(forumId).collection("discussions").document(discussionId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("DiscussionDetails", "Discussion details found: " + document.getData());
                                String title = document.getString("title");
                                String content = document.getString("content");
                                // Update UI with fetched details
                                updateDiscussionDetailsUI(title, content);
                            } else {
                                Log.e("DiscussionDetails", "No discussion details found in document.");
                                Toast.makeText(getContext(), "Discussion not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("DiscussionDetails", "Error fetching discussion details", task.getException());
                            Toast.makeText(getContext(), "Error fetching discussion details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchComments(String forumId, String discussionId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("DiscussionDetails", "Attempting to fetch comments from Firestore");
        db.collection("forums").document(forumId).collection("discussions").document(discussionId).collection("comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mComments.clear(); // Clear previous comments
                            Log.d("DiscussionDetails", "Comments fetched successfully");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DiscussionDetails", "Comment: " + document.getData());
                                String comment = document.getString("content");
                                if (comment != null) {
                                    mComments.add(comment);
                                }
                            }
                            updateCommentsUI();
                        } else {
                            Log.e("DiscussionDetails", "Error fetching comments", task.getException());
                            Toast.makeText(getContext(), "Error fetching comments", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void updateDiscussionDetailsUI(String title, String content) {
        // Set discussion details in TextViews
        textViewDiscussionTitle.setText(title);
        textViewDiscussionContent.setText(content);
    }

    private void updateCommentsUI() {
        StringBuilder commentsText = new StringBuilder();
        for (String comment : mComments) {
            commentsText.append(comment).append("\n\n");
        }
        textViewComments.setText(commentsText.toString());
    }
}
