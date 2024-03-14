package com.example.parikramaapp.communityAndSocial.forum;

import android.os.Bundle;
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

public class DiscussionDetailsFragment extends Fragment {

    private static final String ARG_FORUM_ID = "u8ddPLQD3jsVuSXBZo3f"; // Added
    private static final String ARG_DISCUSSION_ID = "UjAdXANiH3YocFx8s1gp";

    private String mForumId; // Added
    private String mDiscussionId;
    private TextView textViewDiscussionTitle;
    private TextView textViewDiscussionContent;

    public DiscussionDetailsFragment() {
        // Required empty public constructor
    }

    public static DiscussionDetailsFragment newInstance(String forumId, String discussionId) {
        DiscussionDetailsFragment fragment = new DiscussionDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FORUM_ID, forumId); // Added
        args.putString(ARG_DISCUSSION_ID, discussionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForumId = getArguments().getString(ARG_FORUM_ID); // Added
            mDiscussionId = getArguments().getString(ARG_DISCUSSION_ID);
            // Fetch discussion details based on the discussion ID
            fetchDiscussionDetails(mForumId, mDiscussionId); // Modified
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
    }

    private void fetchDiscussionDetails(String forumId, String discussionId) { // Modified
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("forums").document(forumId).collection("discussions").document(discussionId) // Modified
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String title = document.getString("title");
                                String content = document.getString("content");
                                // Update UI with fetched details
                                updateUI(title, content);
                            } else {
                                // Document does not exist
                                Toast.makeText(getContext(), "Discussion not found", Toast.LENGTH_SHORT).show(); // Added
                            }
                        } else {
                            // Error fetching document
                            Toast.makeText(getContext(), "Error fetching discussion details", Toast.LENGTH_SHORT).show(); // Added
                        }
                    }
                });
    }

    private void updateUI(String title, String content) {
        // Set discussion details in TextViews
        textViewDiscussionTitle.setText(title);
        textViewDiscussionContent.setText(content);
    }
}
