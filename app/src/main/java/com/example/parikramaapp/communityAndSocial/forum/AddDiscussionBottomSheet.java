package com.example.parikramaapp.communityAndSocial.forum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.parikramaapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDiscussionBottomSheet extends BottomSheetDialogFragment {

    public interface DiscussionCreationListener {
        void onDiscussionCreated();
    }

    private DiscussionCreationListener mListener;

    private EditText editTextDiscussionTitle;
    private EditText editTextDiscussionContent;
    private Button buttonCreateDiscussion;
    private String forumId;

    public static AddDiscussionBottomSheet newInstance(String forumId) {
        AddDiscussionBottomSheet fragment = new AddDiscussionBottomSheet();
        Bundle args = new Bundle();
        args.putString("forumId", forumId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_discussion, container, false);

        editTextDiscussionTitle = v.findViewById(R.id.editTextDiscussionTitle);
        editTextDiscussionContent = v.findViewById(R.id.editTextDiscussionContent);
        buttonCreateDiscussion = v.findViewById(R.id.buttonAddDiscussion);

        if (getArguments() != null) {
            forumId = getArguments().getString("forumId");
        }

        buttonCreateDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDiscussion();
            }
        });

        return v;
    }

    private void createDiscussion() {
        String title = editTextDiscussionTitle.getText().toString().trim();
        String content = editTextDiscussionContent.getText().toString().trim();

        if (!title.isEmpty() && !content.isEmpty() && forumId != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new discussion map
            Map<String, Object> discussion = new HashMap<>();
            discussion.put("title", title);
            discussion.put("content", content);

            // Add a new document with a generated ID
            db.collection("forums").document(forumId).collection("discussions")
                    .add(discussion)
                    .addOnSuccessListener(documentReference -> {
                        if (mListener != null) {
                            mListener.onDiscussionCreated();
                        }
                        dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Log.w("NewDiscussion", "Error adding document", e);
                    });
        }else {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
        }
    }
    public void setDiscussionCreationListener(DiscussionCreationListener listener) {
        mListener = listener;
    }

}
