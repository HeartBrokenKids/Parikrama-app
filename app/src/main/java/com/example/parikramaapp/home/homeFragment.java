package com.example.parikramaapp.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.parikramaapp.R;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment implements ServiceAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private ServiceAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Data for the cards
        List<String> services = Arrays.asList(
                "City Services", "Food Rescue", "Transportation",
                "Local Exploration", "Community", "Economic Opportunities",
                "Environment and Health", "Education", "Safety");

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 columns
        adapter = new ServiceAdapter(getContext(), services);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Fragment selectedFragment = null;

        switch (position) {
            case 0:
//                selectedFragment = new CityServicesFragment();
                Toast.makeText(getContext(), "City", Toast.LENGTH_SHORT).show();
                break;
            case 1:
//                selectedFragment = new FoodRescueFragment();
                Toast.makeText(getContext(), "Food", Toast.LENGTH_SHORT).show();
                break;
            case 2:
//                selectedFragment = new TransportationFragment();
                Toast.makeText(getContext(), "Transport", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                selectedFragment = new LocalExplorationFragment();
//                Toast.makeText(getContext(), "Local", Toast.LENGTH_SHORT).show();
                break;
            case 4:
//                selectedFragment = new communityMainFragment();
                Toast.makeText(getContext(), "Community", Toast.LENGTH_SHORT).show();
                break;
            case 5:
//                selectedFragment = new EconomicOpportunitiesFragment();
                Toast.makeText(getContext(), "Economic", Toast.LENGTH_SHORT).show();
                break;
            case 6:
//                selectedFragment = new EnvironmentAndHealthFragment();
                Toast.makeText(getContext(), "Env", Toast.LENGTH_SHORT).show();
                break;
            case 7:
//                selectedFragment = new EducationFragment();
                Toast.makeText(getContext(), "Edu", Toast.LENGTH_SHORT).show();
                break;
            case 8:
//                selectedFragment = new SafetyFragment();
                Toast.makeText(getContext(), "safety", Toast.LENGTH_SHORT).show();
                break;
            // Add more cases as needed for additional cards
        }

        // Perform the fragment transaction
        if (selectedFragment != null) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment) // Ensure you have a container in your layout to replace fragments
                    .addToBackStack(null) // Add transaction to back stack for navigation
                    .commit();
        }
    }

}