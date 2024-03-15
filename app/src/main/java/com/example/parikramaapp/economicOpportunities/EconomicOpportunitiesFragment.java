package com.example.parikramaapp.economicOpportunities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.parikramaapp.R;

public class EconomicOpportunitiesFragment extends Fragment {

    private FloatingActionButton btnAddJob;

    public EconomicOpportunitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_economic_opportunities, container, false);

        btnAddJob = rootView.findViewById(R.id.btnAddJob);

        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace EconomicOpportunitiesFragment with AddJobFragment
                replaceWithAddJobFragment();
            }
        });

        return rootView;
    }

    private void replaceWithAddJobFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new AddJobFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
