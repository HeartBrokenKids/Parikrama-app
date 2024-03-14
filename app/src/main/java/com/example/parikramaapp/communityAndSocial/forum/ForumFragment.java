package com.example.parikramaapp.communityAndSocial.forum;
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

        fetchForums();

        return rootView;
    }

    private void fetchForums() {
        db.collection("forums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String forumId = document.getId();
                                forumTitles.add(title);
                                forumIds.add(forumId);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Failed to fetch forums", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
