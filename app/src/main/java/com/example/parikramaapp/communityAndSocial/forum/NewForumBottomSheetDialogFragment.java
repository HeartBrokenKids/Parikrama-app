package com.example.parikramaapp.communityAndSocial.forum;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parikramaapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewForumBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private EditText editTextForumTitle, editTextDiscussionTitle, editTextDiscussionContent;
    private Button buttonCreateForum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_forum_dialog, container, false);

        editTextForumTitle = view.findViewById(R.id.editTextForumTitle);
        editTextDiscussionTitle = view.findViewById(R.id.editTextDiscussionTitle);
        editTextDiscussionContent = view.findViewById(R.id.editTextDiscussionContent);
        buttonCreateForum = view.findViewById(R.id.buttonCreateForum);

        buttonCreateForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewForum();
            }
        });

        return view;
    }

    private void createNewForum() {
        String forumTitle = editTextForumTitle.getText().toString().trim();
        String discussionTitle = editTextDiscussionTitle.getText().toString().trim();
        String discussionContent = editTextDiscussionContent.getText().toString().trim();

        if (TextUtils.isEmpty(forumTitle) || TextUtils.isEmpty(discussionTitle) || TextUtils.isEmpty(discussionContent)) {
            Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create the new forum
        Map<String, Object> forumData = new HashMap<>();
        forumData.put("title", forumTitle);
        db.collection("forums")
                .add(forumData)
                .addOnSuccessListener(documentReference -> {
                    // Successfully created forum, now add discussion
                    Map<String, Object> discussionData = new HashMap<>();
                    discussionData.put("title", discussionTitle);
                    discussionData.put("content", discussionContent);

                    db.collection("forums")
                            .document(documentReference.getId())
                            .collection("discussions")
                            .add(discussionData)
                            .addOnSuccessListener(documentReference1 -> {
                                Toast.makeText(getContext(), "Forum and Discussion created successfully", Toast.LENGTH_SHORT).show();
                                dismiss();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to create discussion", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to create forum", Toast.LENGTH_SHORT).show();
                });
    }
}

