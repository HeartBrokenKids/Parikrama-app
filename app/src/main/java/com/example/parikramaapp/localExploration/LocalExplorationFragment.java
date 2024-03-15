package com.example.parikramaapp.localExploration;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.parikramaapp.localExploration.GetNearbyPlace;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LocalExplorationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private double latitude, longitude;
    private int ProximityRadius = 10000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_exploration, container, false);

        // Initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set onClickListener for search button
        Button searchButton = view.findViewById(R.id.search_address);
        searchButton.setOnClickListener(v -> searchAddress());

        // Set onClickListeners for nearby places buttons
        view.findViewById(R.id.hospitals_nearby).setOnClickListener(v -> fetchNearbyPlaces("hospital"));
        view.findViewById(R.id.schools_nearby).setOnClickListener(v -> fetchNearbyPlaces("school"));
        view.findViewById(R.id.restaurants_nearby).setOnClickListener(v -> fetchNearbyPlaces("restaurant"));

        return view;
    }

    private void fetchNearbyPlaces(String nearbyPlace) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String url = getUrl(latitude, longitude, nearbyPlace);
                Log.d("URL",url);
                GetNearbyPlace getNearbyPlaces = new GetNearbyPlace();
                Object[] transferData = new Object[2];
                transferData[0] = mMap;
                transferData[1] = url;
                Log.d("finalquery", Arrays.toString(transferData));
                getNearbyPlaces.execute(transferData);
                Toast.makeText(requireContext(), "Searching for Nearby " + nearbyPlace + "...", Toast.LENGTH_SHORT).show();
                Toast.makeText(requireContext(), "Showing Nearby " + nearbyPlace + "...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Unable to fetch current location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + ProximityRadius);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyAVlOm3A8tcE7lML0mgm-eYqEhWEh3IV2U");
        return googleURL.toString();
    }

    private void searchAddress() {
        EditText addressField = requireView().findViewById(R.id.location_search);
        String address = addressField.getText().toString();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(requireContext(), "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            fetchNearbyPlaces(address);
        }
        // Perform address search and display on map (similar to your implementation)
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        // Get the user's current location
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                // Get user's current latitude and longitude
                double currentLatitude = location.getLatitude();
                double currentLongitude = location.getLongitude();

                // Create LatLng object for user's current location
                LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);

                // Define zoom level
                float zoomLevel = 15.0f; // Change this value as needed

                // Animate camera to user's current location with zoom level
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoomLevel));
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Implement if needed
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Implement if needed
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Implement if needed
    }

    @Override
    public void onLocationChanged(Location location) {
        // Implement if needed
    }
}
