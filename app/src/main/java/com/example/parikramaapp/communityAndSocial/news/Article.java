package com.example.parikramaapp.communityAndSocial.news;

public class Article {
    private String title;
    private String description;
    private String imageUrl;
    private String publishedAt;

    public Article(String title, String description, String imageUrl, String publishedAt) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;
    }

    // Getters and setters for all fields
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
