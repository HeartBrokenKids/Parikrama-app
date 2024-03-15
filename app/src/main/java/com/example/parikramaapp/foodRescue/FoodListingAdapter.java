package com.example.parikramaapp.foodRescue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parikramaapp.LocationHelper;
import com.example.parikramaapp.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FoodListingAdapter extends RecyclerView.Adapter<FoodListingAdapter.ViewHolder> {
    private List<FoodListing> foodListings;

    public FoodListingAdapter(List<FoodListing> foodListings) {
        this.foodListings = foodListings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodListing listing = foodListings.get(position);
        holder.title.setText(listing.getTitle());
        holder.description.setText(listing.getDescription());

        // Convert latitude and longitude to a rough estimate of the city
        String city = LocationHelper.getCityFromCoordinates(holder.itemView.getContext(), listing.getLocation().getLatitude(), listing.getLocation().getLongitude());
        holder.location.setText(String.format(Locale.getDefault(), "City: %s", city));

        // Check if expiryTime is not null before formatting
        if (listing.getExpiryTime() != null) {
            holder.expiryTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(listing.getExpiryTime().toDate()));
        } else {
            holder.expiryTime.setText("N/A");
        }

        holder.quantity.setText(String.format(Locale.getDefault(), "Quantity: %s", listing.getQuantity())); // Use %s for a string
        holder.donorId.setText(String.format(Locale.getDefault(), "Donor ID: %s", listing.getDonorId()));

        // Check if timePosted is not null before formatting
        if (listing.getTimePosted() != null) {
            holder.timePosted.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(listing.getTimePosted().toDate()));
        } else {
            holder.timePosted.setText("N/A");
        }

        holder.status.setText(String.format(Locale.getDefault(), "Status: %s", listing.getStatus()));
    }


    @Override
    public int getItemCount() {
        return foodListings.size();
    }

    public void updateData(List<FoodListing> newFoodListings) {
        foodListings.clear();
        foodListings.addAll(newFoodListings);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView location;
        TextView expiryTime;
        TextView quantity;
        TextView donorId;
        TextView timePosted;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.foodListingTitle);
            description = itemView.findViewById(R.id.foodListingDescription);
            location = itemView.findViewById(R.id.foodListingLocation);
            expiryTime = itemView.findViewById(R.id.foodListingExpiry);
            quantity = itemView.findViewById(R.id.foodListingQuantity);
            donorId = itemView.findViewById(R.id.foodListingDonorId);
            timePosted = itemView.findViewById(R.id.foodListingTimePosted);
            status = itemView.findViewById(R.id.foodListingStatus);
        }
    }
}
