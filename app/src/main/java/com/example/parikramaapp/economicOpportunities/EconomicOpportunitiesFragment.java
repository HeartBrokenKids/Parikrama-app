package com.example.parikramaapp.economicOpportunities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EconomicOpportunitiesFragment extends Fragment {

    private RecyclerView recyclerView;
    private JobPostAdapter adapter;
    private List<JobPost> jobPostList; // Use List<JobPost> instead of List<Job>
    private FirebaseFirestore db;

    public EconomicOpportunitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_economic_opportunities, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        jobPostList = new ArrayList<>(); // Initialize with JobPost type
        adapter = new JobPostAdapter(jobPostList, getContext()); // Pass JobPost list
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        fetchJobPosts(); // Update method name to fetchJobPosts()

        return rootView;
    }

    private void fetchJobPosts() {
        db.collection("job_posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            JobPost jobPost = documentSnapshot.toObject(JobPost.class); // Assuming JobPost is the correct model class
                            jobPostList.add(jobPost); // Add JobPost objects to the list
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to fetch job posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
