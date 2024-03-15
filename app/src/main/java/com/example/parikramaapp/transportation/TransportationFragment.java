package com.example.parikramaapp.transportation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TransportationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText destinationEditText;
    private Button showRouteButton;

    public TransportationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transportation, container, false);

        destinationEditText = rootView.findViewById(R.id.destinationEditText);
        showRouteButton = rootView.findViewById(R.id.showRouteButton);

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Set click listener for show route button
        showRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destination = destinationEditText.getText().toString().trim();
                if (!destination.isEmpty()) {
                    fetchAndShowDirections(destination);
                } else {
                    Toast.makeText(requireContext(), "Please enter a destination", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void fetchAndShowDirections(String destination) {
        // Check for location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Fetch current location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        // Show current location and destination on map
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        LatLng destLatLng = getLocationFromAddress(destination);

                        if (destLatLng != null) {
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
                            mMap.addMarker(new MarkerOptions().position(destLatLng).title("Destination"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                        } else {
                            Toast.makeText(requireContext(), "Failed to find location for the destination", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private LatLng getLocationFromAddress(String strAddress) {
        // Use Geocoder to get LatLng from address
        // Implement this method to convert address to LatLng
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, fetch and show directions
            String destination = destinationEditText.getText().toString().trim();
            if (!destination.isEmpty()) {
                fetchAndShowDirections(destination);
            } else {
                Toast.makeText(requireContext(), "Please enter a destination", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
