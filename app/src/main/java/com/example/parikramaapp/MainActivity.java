package com.example.parikramaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.parikramaapp.home.homeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    TextView temperature;
    private FusedLocationProviderClient fusedLocationClient;
    private ExecutorService executorService;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigation);
        temperature = findViewById(R.id.temperature);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        executorService = Executors.newSingleThreadExecutor();

        checkLocationPermissionAndFetch();
        navigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.action_home) {
                fragment = new homeFragment(); // Replace with actual fragment initialization
            } else if (id == R.id.action_explore) {
                fragment = new exploreFragment(); // Replace with actual fragment initialization
            } else if (id == R.id.action_profile) {
                fragment = new profileFragment(); // Replace with actual fragment initialization
            }
        return loadFragment(fragment);
        });

        // Load the default fragment
        if (savedInstanceState == null) {
            navigationView.setSelectedItemId(R.id.action_home);
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }
    private void checkLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            temperature.setText("-");
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations, this can be null.
                            if (location != null) {
                                fetchTemperature(location.getLatitude(), location.getLongitude());
                            } else {
                                temperature.setText("-");
                            }
                        }
                    });
        }
    }
    private void fetchTemperature(double latitude, double longitude) {
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true", latitude, longitude);
            Request request = new Request.Builder().url(url).build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject currentWeather = jsonObject.getJSONObject("current_weather");
                    double temp = currentWeather.getDouble("temperature");

                    // Update the UI on the main thread
                    handler.post(() -> temperature.setText(String.format("%s°C", temp)));
                } else {
                    // Handle the case of an unsuccessful response or no data
                    handler.post(() -> temperature.setText("-"));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                handler.post(() -> temperature.setText("-"));
            }
        });
    }

}
