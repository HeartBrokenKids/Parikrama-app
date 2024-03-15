package com.example.parikramaapp.localExploration;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlace extends AsyncTask<Object, String, String>
{
    private String googleplaceData, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects)
    {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try
        {
            googleplaceData = downloadUrl.ReadTheURL(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googleplaceData;
    }


    @Override
    protected void onPostExecute(String s)
    {
        List<HashMap<String, String>> nearByPlacesList = null;
        DataParser dataParser = new DataParser();
        nearByPlacesList = dataParser.parse(s);
        Log.d("placess", nearByPlacesList.toString());
        DisplayNearbyPlaces(nearByPlacesList);
    }
    private List<Marker> markers = new ArrayList<>();

    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearByPlacesList) {
        // Remove all previous markers
        for (Marker marker : markers) {
            marker.remove();
        }
        mMap.clear();

        markers.clear();
        Log.d("after_clearing", markers.toString());

        // Add new markers
        for (HashMap<String, String> googleNearbyPlace : nearByPlacesList) {
            String nameOfPlace = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
            double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(nameOfPlace + " : " + vicinity)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

            // Add marker to the map and store it in the list
            Marker marker = mMap.addMarker(markerOptions);
            markers.add(marker);
        }
        Log.d("after_clearing", markers.toString());

        // Adjust camera position and zoom level
        if (!nearByPlacesList.isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (HashMap<String, String> googleNearbyPlace : nearByPlacesList) {
                double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
                double lng = Double.parseDouble(googleNearbyPlace.get("lng"));
                builder.include(new LatLng(lat, lng));
            }
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }
}
