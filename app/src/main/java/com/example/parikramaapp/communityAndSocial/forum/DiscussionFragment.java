package com.example.parikramaapp.communityAndSocial.forum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    public DiscussionFragment() {
        // Required empty public constructor
    }

    public static DiscussionFragment newInstance() {
        return new DiscussionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discussion, container, false);

        listViewDiscussions = rootView.findViewById(R.id.listViewDiscussions);
        db = FirebaseFirestore.getInstance();
        discussionTitles = new ArrayList<>();
        discussionIds = new ArrayList<>();

        // Fetch discussions
        fetchDiscussions();

        return rootView;
    }

    private void fetchDiscussions() {
        // Here you should fetch discussions from Firestore based on the selected forum
        // For now, I'll demonstrate fetching all discussions
        db.collection("forums").document("yourForumId").collection("discussions")
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, discussionTitles);
                            listViewDiscussions.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Failed to fetch discussions", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Handle item click listener for discussions
        listViewDiscussions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String discussionId = discussionIds.get(position);
                // Start a new activity or fragment to display the details of the selected discussion
                // Pass the discussionId to the new activity or fragment
            }
        });
    }
}
