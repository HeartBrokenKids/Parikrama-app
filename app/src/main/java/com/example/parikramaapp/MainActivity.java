package com.example.parikramaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parikramaapp.communityAndSocial.CommunityMainFragment;
import com.example.parikramaapp.economicOpportunities.EconomicOpportunitiesFragment;
import com.example.parikramaapp.explore.ChatBotFragment;
import com.example.parikramaapp.foodRescue.FoodRescueFragment;
import com.example.parikramaapp.home.homeFragment;
import com.example.parikramaapp.localExploration.LocalExplorationFragment;
import com.example.parikramaapp.profileFragment;
import com.example.parikramaapp.translate.TranslateFragment;
import com.example.parikramaapp.transportation.TransportationFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
        clearLastSelectedIndex();
        checkLocationPermissionAndFetch();
        navigationView.setOnNavigationItemSelectedListener(item -> {
            Log.d("NAVVV", "onCreate: presssed");
            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.action_home) {
                fragment = HOMEEE(fragment);
            } else if (id == R.id.action_explore) {
                fragment = new ChatBotFragment();
            } else if (id == R.id.action_profile) {
                fragment = new profileFragment();
            }
            return loadFragment(fragment);
        });

        if (savedInstanceState == null) {
            navigationView.setSelectedItemId(R.id.action_home);
        }

    }

    private Fragment HOMEEE(Fragment fragment2) {
        Log.d("FRAGG", "HOMEEE: "+fragment2);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (
                        fragment instanceof FoodRescueFragment ||
                            fragment instanceof TransportationFragment ||
                            fragment instanceof LocalExplorationFragment ||
                            fragment instanceof CommunityMainFragment ||
                            fragment instanceof EconomicOpportunitiesFragment ||
                            fragment instanceof TranslateFragment) {
                Log.d("DEBUGGGGG", "YESS IT IS WORKING");
                clearLastSelectedIndex();
                fragment = new homeFragment();
                return fragment;
            }
            else{
                Log.d("DEBUGGGGG", "YESS IT IS WORKING2222");
                fragment = new homeFragment();
                return fragment;
            }
    }

    private void clearLastSelectedIndex() {
        SharedPreferences prefs = getSharedPreferences("homeFragmentPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("lastSelectedIndex");
        editor.apply();
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            if (fragment instanceof homeFragment) {
                if (!fragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack("home")
                            .commit();
                }
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
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
                    Log.d("MainActivity", "Response: " + responseBody);

                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject currentWeather = jsonObject.getJSONObject("current_weather");
                    double temp = currentWeather.getDouble("temperature");

                    handler.post(() -> temperature.setText(String.format("%s°C", temp)));
                } else {
                    handler.post(() -> temperature.setText("-"));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                handler.post(() -> temperature.setText("-"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        printBackStackEntries();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Log.d("DEBUGGGGG", "onBackPressed: "+currentFragment);
        if (
                currentFragment instanceof FoodRescueFragment ||
                        currentFragment instanceof TransportationFragment ||
                        currentFragment instanceof LocalExplorationFragment ||
                        currentFragment instanceof CommunityMainFragment ||
                        currentFragment instanceof EconomicOpportunitiesFragment ||
                        currentFragment instanceof TranslateFragment) {

            Log.d("DEBUGGGGG", "YESS IT IS WORKING");
            clearLastSelectedIndex();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new homeFragment())
                    .commit();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void printBackStackEntries() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            if (getSupportFragmentManager().getBackStackEntryAt(i).getName() != null){
                Log.d("BackStackEntry", getSupportFragmentManager().getBackStackEntryAt(i).getName());

            }
        }
    }

}