package com.example.parikramaapp.communityAndSocial;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class localNewsFragment extends Fragment {

    private static final int PERMISSION_REQUEST_LOCATION = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private TextView newsTextView;

    public localNewsFragment() {
        // Required empty public constructor
    }

    public static localNewsFragment newInstance() {
        return new localNewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_local_news, container, false);

        // Initialize views
        newsTextView = rootView.findViewById(R.id.news_text_view);

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
        Log.d("localNewsFragment", "Fetching news for city: " + city);
        NewsApiClient newsApiClient = new NewsApiClient(new NewsApiClient.NewsListener() {
            @Override
            public void onNewsFetched(JSONArray articles) {
                // Process and display the news articles
                displayNews(articles);
            }
        });
        newsApiClient.execute(city);
    }

    private void displayNews(JSONArray articles) {
        // Display the fetched news articles in the UI
        try {
            StringBuilder newsText = new StringBuilder();
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("title");
                String description = article.getString("description");
                newsText.append(title).append(": ").append(description).append("\n\n");
            }
            newsTextView.setText(newsText.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class NewsApiClient extends AsyncTask<String, Void, String> {

        private static final String API_KEY = "69d6dd6a81fa4b2ca2d88c3459443a42";
        private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines";

        private NewsListener listener;

        public NewsApiClient(NewsListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            try {
                Log.d("NewsApiClient", "Fetching news for city: " + city);
                String encodedCity = URLEncoder.encode(city, "UTF-8");
                URL url = new URL(NEWS_API_URL + "?q=" + encodedCity + "&apiKey=" + API_KEY);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("NewsApiClient", "Error fetching news: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray articles = jsonObject.getJSONArray("articles");
                    listener.onNewsFetched(articles);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public interface NewsListener {
            void onNewsFetched(JSONArray articles);
        }
    }
}