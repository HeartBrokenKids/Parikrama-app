package com.example.parikramaapp.foodRescue;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FoodRescueFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodListingAdapter adapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_rescue, container, false);
        FloatingActionButton fabAddFoodRescue = view.findViewById(R.id.fabAddFoodRescue);
        recyclerView = view.findViewById(R.id.foodRescueRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        fabAddFoodRescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddFoodRescueFragment();
            }
        });

        db = FirebaseFirestore.getInstance();

        adapter = new FoodListingAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        loadFoodListings();

        return view;
    }

    private void navigateToAddFoodRescueFragment() {
         FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
         FragmentTransaction transaction = fragmentManager.beginTransaction();
         transaction.replace(R.id.fragment_container, new AddFoodRescueCaseFragment());
         transaction.addToBackStack(null);
         transaction.commit();
    }

    private void loadFoodListings() {
        db.collection("food_listings")
                .whereEqualTo("status", "available")
                .orderBy("timePosted", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<FoodListing> listings = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            FoodListing listing = documentSnapshot.toObject(FoodListing.class);
                            listings.add(listing);
                        }
                        adapter.updateData(listings);
                        Log.d("FoodRescueFragment", "Food listings loaded successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error loading food listings", Toast.LENGTH_SHORT).show();
                        Log.e("FoodRescueFragment", "Error loading food listings", e);
                    }
                });
    }
}
