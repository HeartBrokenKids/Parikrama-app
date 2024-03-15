package com.example.parikramaapp.economicOpportunities;

public class JobPost {
    private String title;
    private String userId;
    private String description;
    private int upvotes; // Add upvotes field
    private String contactInfo;

    // Constructor with all fields
    public JobPost(String title, String description, int upvotes, String userId, String contactInfo) {
        this.title = title;
        this.description = description;
        this.upvotes = upvotes;
        this.userId = userId;
        this.contactInfo = contactInfo;
    }

    // Getters and setters
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
}
