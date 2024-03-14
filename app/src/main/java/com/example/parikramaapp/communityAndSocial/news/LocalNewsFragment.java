package com.example.parikramaapp.communityAndSocial.news;

import android.Manifest;
import android.content.Context;
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
import android.widget.ListView;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocalNewsFragment extends Fragment {

    private static final int PERMISSION_REQUEST_LOCATION = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private ListView newsListView;

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


        newsListView = rootView.findViewById(R.id.news_list_view);

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
//            loadLocalNews();
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
//                            fetchNews(userCity);
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

//    private void fetchNews(String city) {
//        Log.d("LocalNewsFragment", "Fetching news for city: " + city);
//
//        // Replace YOUR_API_KEY with your actual News API key
//        String apiKey = "69d6dd6a81fa4b2ca2d88c3459443a42";
//        String url = "https://newsapi.org/v2/top-headlines?q=" + URLEncoder.encode(city) + "&apiKey=" + apiKey;
//
//        new FetchNewsTask().execute(url);
//    }

//    private class FetchNewsTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//
//            String jsonResponse = null;
//            HttpURLConnection urlConnection = null;
//            InputStream inputStream = null;
//            BufferedReader reader = null;
//
//            try {
//                URL url = new URL(urls[0]);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                inputStream = urlConnection.getInputStream();
//                StringBuilder builder = new StringBuilder();
//                if (inputStream != null) {
//                    reader = new BufferedReader(new InputStreamReader(inputStream));
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        builder.append(line);
//                    }
//                    jsonResponse = builder.toString();
//                }
//            } catch (IOException e) {
//                Log.e("LocalNewsFragment", "Error retrieving news JSON", e);
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    } catch (IOException e) {
//                        Log.e("LocalNewsFragment", "Error closing input stream", e);
//                    }
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        Log.e("LocalNewsFragment", "Error closing reader", e);
//                    }
//                }
//            }
//
//            return jsonResponse;
//        }
//
////        @Override
////        protected void onPostExecute(String jsonResponse) {
////            if (jsonResponse != null) {
////                saveNewsToFile(jsonResponse); // Save the news JSON to a local file
////                updateNewsList(jsonResponse);
////            }
////        }
//    }

//    private void updateNewsList(String jsonResponse) {
//        try {
//            JSONObject responseJson = new JSONObject(jsonResponse);
//            if ("ok".equals(responseJson.getString("status"))) {
//                JSONArray articlesArray = responseJson.getJSONArray("articles");
//                List<Article> articles = new ArrayList<>();
//                for (int i = 0; i < articlesArray.length(); i++) {
//                    JSONObject articleJson = articlesArray.getJSONObject(i);
//                    Article article = new Article(
//                            articleJson.getString("title"),
//                            articleJson.getString("description"),
//                            articleJson.getString("url"),
//                            articleJson.getString("urlToImage"),
//                            articleJson.getString("publishedAt")
//                    );
//                    articles.add(article);
//                }
//                // Update the UI with the fetched articles
//                getActivity().runOnUiThread(() -> {
//                    NewsAdapter adapter = new NewsAdapter(requireContext(), articles);
//                    newsListView.setAdapter(adapter);
//                });
//            } else {
//                Log.e("LocalNewsFragment", "News API returned an error: " + responseJson.getString("message"));
//            }
//        } catch (JSONException e) {
//            Log.e("LocalNewsFragment", "Error parsing news JSON", e);
//        }
//    }

//    private void loadLocalNews() {
//        String newsJson = readNewsFromFile();
//        if (!newsJson.isEmpty()) {
//            updateNewsList(newsJson);
//        } else {
//            // If there's no local file, fetch news normally
//            startLocationUpdates();
//        }
//    }
//    private void saveNewsToFile(String newsJson) {
//        FileOutputStream fos = null;
//        try {
//            fos = requireContext().openFileOutput("local_news.json", Context.MODE_PRIVATE);
//            fos.write(newsJson.getBytes());
//            Log.d("LocalNewsFragment", "News saved locally");
//        } catch (Exception e) {
//            Log.e("LocalNewsFragment", "Error saving news", e);
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    Log.e("LocalNewsFragment", "Error closing file output stream", e);
//                }
//            }
//        }
//    }
//    private String readNewsFromFile() {
//        FileInputStream fis = null;
//        StringBuilder builder = new StringBuilder();
//        try {
//            fis = requireContext().openFileInput("local_news.json");
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader reader = new BufferedReader(isr);
//            String line;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//            }
//        } catch (Exception e) {
//            Log.e("LocalNewsFragment", "Error reading news from file", e);
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    Log.e("LocalNewsFragment", "Error closing file input stream", e);
//                }
//            }
//        }
//        return builder.toString();
//    }
}
