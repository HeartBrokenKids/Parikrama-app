package com.example.parikramaapp.foodRescue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parikramaapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    private Button selectLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        selectLocationButton = findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return the selected location to the calling activity (AddFoodRescueCaseFragment)
                if (marker != null) {
                    LatLng selectedLatLng = marker.getPosition();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("latitude", selectedLatLng.latitude);
                    resultIntent.putExtra("longitude", selectedLatLng.longitude);
                    setResult(RESULT_OK, resultIntent);
                }
                // Close the MapActivity
                finish();
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location (e.g., your city or any other preferred location)
        LatLng defaultLocation = new LatLng(37.7749, -122.4194);
        marker = mMap.addMarker(new MarkerOptions().position(defaultLocation).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {}

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {}

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                // Update marker position when the user finishes dragging
                marker.setPosition(marker.getPosition());
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Update marker position when the user clicks on the map
                if (marker != null) {
                    marker.setPosition(latLng);
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                }
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                // Update marker position when the user long clicks on the map
                if (marker != null) {
                    marker.setPosition(latLng);
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Pass the selected location back to the calling activity (AddFoodRescueCaseFragment)
        if (marker != null) {
            LatLng selectedLatLng = marker.getPosition();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("latitude", selectedLatLng.latitude);
            resultIntent.putExtra("longitude", selectedLatLng.longitude);
            setResult(RESULT_OK, resultIntent);
        }
        // Close the MapActivity
        finish();
        super.onBackPressed();
    }
}