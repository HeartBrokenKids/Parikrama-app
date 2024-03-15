package com.example.parikramaapp.economicOpportunities;

public class JobPost {
    private String id; // Add document ID field
    private String title;
    private String userId;
    private String description;
    private int upvotes;
    private String contactInfo;

    public JobPost() {
        // Default constructor required for Firestore
    }

    // Constructor with all fields
    public JobPost(String id, String title, String userId, String description, int upvotes, String contactInfo) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.description = description;
        this.upvotes = upvotes;
        this.contactInfo = contactInfo;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    // Method to increment upvotes
    public void incrementUpvotes() {
        upvotes++;
    }
}
