package com.example.parikramaapp.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.example.parikramaapp.communityAndSocial.CommunityMainFragment;
import com.example.parikramaapp.economicOpportunities.EconomicOpportunitiesFragment;
import com.example.parikramaapp.localExploration.LocalExplorationFragment;
import com.example.parikramaapp.translate.TranslateFragment;
import com.example.parikramaapp.transportation.TransportationFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class homeFragment extends Fragment implements serviceAdapter.ItemClickListener, PreferencesDialogFragment.PreferencesDialogListener {

    private RecyclerView recyclerView;
    private List<serviceItem> services;
    private serviceAdapter adapter;
    private boolean[] selectedPreferences;
    private int lastSelectedSubFragmentIndex = -1; // Default value when none selected

    private static final String PREF_FILE_NAME = "homeFragmentPrefs";
    private static final String LAST_SELECTED_INDEX_KEY = "lastSelectedIndex";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeServicesList();
        setupRecyclerView(view);
        setupSearchView(view);
        setupEditButton(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Restore the last selected sub-fragment index
        SharedPreferences prefs = requireContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        lastSelectedSubFragmentIndex = prefs.getInt(LAST_SELECTED_INDEX_KEY, -1);
        if (lastSelectedSubFragmentIndex != -1) {
            navigateToSubFragment(lastSelectedSubFragmentIndex);
        }
    }

    private void initializeServicesList() {
        services = Arrays.asList(
                new serviceItem("Local Exploration", R.drawable.localexplorationicon),
                new serviceItem("Translate", R.drawable.translateicon),
                new serviceItem("Community", R.drawable.communnityicon),
                new serviceItem("Economic Opportunity", R.drawable.economicopportunitiesicon),
                new serviceItem("Transportation", R.drawable.transportationicon),
                new serviceItem("Food Rescue", R.drawable.foodrescueicon),
                new serviceItem("City Services", R.drawable.cityserviceicon),
                new serviceItem("Environment and Health", R.drawable.environmentandhealthicon),
                new serviceItem("Education", R.drawable.educationicon)
        );

        selectedPreferences = new boolean[services.size()];
        Arrays.fill(selectedPreferences, true);
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new serviceAdapter(getContext(), services);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    private void setupSearchView(View view) {
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    private void setupEditButton(View view) {
        TextView editButton = view.findViewById(R.id.editbutton);
        editButton.setOnClickListener(v -> showPreferencesDialog());
    }

    private void showPreferencesDialog() {
        // Convert your list of serviceItems to a list of Strings for the service names.
        List<String> serviceNames = new ArrayList<>();
        for (serviceItem item : services) {
            serviceNames.add(item.getTitle()); // Assuming getTitle() returns the name of the service.
        }

        // Now use the newInstance method to create the PreferencesDialogFragment.
        PreferencesDialogFragment dialogFragment = PreferencesDialogFragment.newInstance(serviceNames, selectedPreferences);
        dialogFragment.setTargetFragment(this, 0); // 'this' refers to homeFragment instance.
        dialogFragment.show(getParentFragmentManager(), "PreferencesDialogFragment");
    }

    @Override
    public void onPreferencesSelected(boolean[] selectedPreferences) {
        this.selectedPreferences = selectedPreferences;
        adapter.filterDisplayedServices(selectedPreferences);
    }

    @Override
    public void onItemClick(View view, int position) {
        lastSelectedSubFragmentIndex = position; // Update the last selected sub-fragment index
        navigateToSubFragment(position);
    }

    private void navigateToSubFragment(int position) {
        serviceItem clickedItem = adapter.getCurrentItem(position);
        Fragment selectedFragment = null;

        if (clickedItem != null) {
            switch (clickedItem.getTitle()) {
                case "City Services":
                    // selectedFragment = new CityServicesFragment();
                    break;
                case "Food Rescue":
                    // selectedFragment = new FoodRescueFragment();
                    break;
                case "Transportation":
                    selectedFragment = new TransportationFragment();
                    break;
                case "Local Exploration":
                    selectedFragment = new LocalExplorationFragment();
                    break;
                case "Community":
                    selectedFragment = new CommunityMainFragment();
                    break;
                case "Economic Opportunity":
                    selectedFragment = new EconomicOpportunitiesFragment();
                    break;
                case "Translate":
                    selectedFragment = new TranslateFragment();
                    break;
                default:
                    Toast.makeText(getContext(), "Service not implemented yet.", Toast.LENGTH_SHORT).show();
                    break;
            }

            // Perform the fragment transaction if a valid selection was made
            if (selectedFragment != null) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .addToBackStack(null) // Add transaction to the back stack for navigation
                        .commit();
            }
        } else {
            Toast.makeText(getContext(), "Error: Item not found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save the last selected sub-fragment index
        SharedPreferences prefs = requireContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LAST_SELECTED_INDEX_KEY, lastSelectedSubFragmentIndex);
        editor.apply();
    }
}
