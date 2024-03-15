package com.example.parikramaapp.economicOpportunities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddJobFragment extends Fragment {

    private FirebaseFirestore db;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextContactInfo;
    private Button btnAddJob;

    public AddJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_job, container, false);

        db = FirebaseFirestore.getInstance();

        editTextTitle = rootView.findViewById(R.id.editTextTitle);
        editTextDescription = rootView.findViewById(R.id.editTextDescription);
        editTextContactInfo = rootView.findViewById(R.id.editTextContactInfo);
        btnAddJob = rootView.findViewById(R.id.btnAddJob);

        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJobPost();
            }
        });

        return rootView;
    }

    private void addJobPost() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String contactInfo = editTextContactInfo.getText().toString().trim();

        // Check if any of the fields are empty
        if (title.isEmpty() || description.isEmpty() || contactInfo.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming you have a way to get the userId, replace "your_user_id" with the actual userId
        String userId = "your_user_id"; // Replace this with the actual userId

        // Create a HashMap to hold the job post data
        Map<String, Object> jobPost = new HashMap<>();
        jobPost.put("userId", userId);
        jobPost.put("title", title);
        jobPost.put("description", description);
        jobPost.put("upvotes", 0); // Assuming initial upvotes are 0
        jobPost.put("contactInfo", contactInfo);

        // Add the job post to Firestore
        db.collection("job_posts").add(jobPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Job added successfully", Toast.LENGTH_SHORT).show();
                        // Clear the input fields after successful addition
                        editTextTitle.setText("");
                        editTextDescription.setText("");
                        editTextContactInfo.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to add job: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
