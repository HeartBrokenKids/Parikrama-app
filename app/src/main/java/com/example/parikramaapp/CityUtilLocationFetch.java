package com.example.parikramaapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CityUtilLocationFetch {

    private static final int PERMISSION_REQUEST_LOCATION = 1001;

    public interface CityFetchListener {
        void onCityFetched(String city);
    }

    public static void getUserCity(Context context, CityFetchListener fetchListener) {
        if (checkLocationPermission(context)) {
            fetchLocationAndCity(context, fetchListener);
        } else {
            fetchListener.onCityFetched("Permission not granted");
        }
    }

    private static boolean checkLocationPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private static void fetchLocationAndCity(Context context, CityFetchListener fetchListener) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    String city = fetchCity(context, location.getLatitude(), location.getLongitude());
                    fusedLocationClient.removeLocationUpdates(this);
                    fetchListener.onCityFetched(city);
                } else {
                    fetchListener.onCityFetched("Unknown location");
                }
            }
        }, Looper.getMainLooper());
    }

    private static String fetchCity(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("CityUtils", "Error getting city: " + e.getMessage());
        }
        return "Unknown";
    }
}
