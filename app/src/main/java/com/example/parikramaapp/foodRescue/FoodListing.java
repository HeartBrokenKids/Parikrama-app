package com.example.parikramaapp.foodRescue;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class FoodListing {
    private String title;
    private String description;
    private GeoPoint location; // For lat, long coordinates
    private Timestamp expiryTime;
    private String quantity; // Modified quantity to be a string
    private String donorId;
    private Timestamp timePosted;
    private String status;
    private String imageUrl;

    public FoodListing() {
        // Empty constructor needed for Firestore
    }

    // Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Location
    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    // ExpiryTime
    public Timestamp getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Timestamp expiryTime) {
        this.expiryTime = expiryTime;
    }

    // Quantity
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    // DonorId
    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    // TimePosted
    public Timestamp getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(Timestamp timePosted) {
        this.timePosted = timePosted;
    }

    // Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ImageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
