package com.example.parikramaapp.communityAndSocial.news;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LocalNewsFragment extends Fragment {

    private static final int PERMISSION_REQUEST_LOCATION = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView newsRecyclerView; // Change ListView to RecyclerView

    public LocalNewsFragment() {
        // Required empty public constructor
    }

    public static LocalNewsFragment newInstance() {
        return new LocalNewsFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_local_news, container, false);

        newsRecyclerView = rootView.findViewById(R.id.news_recycler_view);

        // Attach a layout manager to the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        newsRecyclerView.setLayoutManager(layoutManager);

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request location permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        } else {
            // Permission already granted, start location updates
            startLocationUpdates();
        }

        return rootView;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location updates
                startLocationUpdates();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateNewsList(List<Article> articles) {
        // Check if the RecyclerView and context are not null
        if (newsRecyclerView != null && getContext() != null) {
            // Create an adapter with the fetched articles
            NewsAdapter adapter = new NewsAdapter(getContext());
            // Set the articles to the adapter
            adapter.setArticles(articles);
            // Set the adapter to your RecyclerView
            newsRecyclerView.setAdapter(adapter);
        } else {
            Log.e("LocalNewsFragment", "RecyclerView or context is null");
        }
    }


    private void startLocationUpdates() {
        try {
            // Create location request
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10000) // Update interval in milliseconds
                    .setFastestInterval(5000); // Fastest update interval in milliseconds

            // Create location callback
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult != null) {
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            // Get user's city from location
                            String userCity = getUserCity(location.getLatitude(), location.getLongitude());
                            // Fetch news for the user's city
                            fetchNews(userCity);

                            // Remove location updates after receiving the first location result
                            fusedLocationClient.removeLocationUpdates(this);
                        }
                    }
                }
            };

            // Request location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Location permission required", Toast.LENGTH_SHORT).show();
        }
    }


    private String getUserCity(double latitude, double longitude) {
        if (!isAdded()) {
            // Fragment is not attached to the activity, return a default value or handle the situation accordingly
            return "Unknown";
        }

        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                // Return the city name
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private void fetchNews(String city) {
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the Retrofit service
        NewsService newsService = retrofit.create(NewsService.class);

        // Define the API key
        String apiKey = "69d6dd6a81fa4b2ca2d88c3459443a42";

        // Call the API to fetch news
        Call<NewsResponse> call = newsService.getTopHeadlines("india", apiKey);
        Log.d("newsoupt", call.toString());
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();
                    Log.d("newsoupt", newsResponse.toString());
                    if (newsResponse != null && "ok".equals(newsResponse.getStatus())) {
                        List<Article> articles = newsResponse.getArticles();
                        Log.d("newsoupt", articles.toString());
                        // Update UI with fetched articles
                        updateNewsList(articles);
                    } else {
                        Log.e("LocalNewsFragment", "Error: " + response.message());
                    }
                } else {
                    Log.e("LocalNewsFragment", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e("LocalNewsFragment", "Error: " + t.getMessage());
            }
        });
    }

}
