package com.example.parikramaapp.communityAndSocial.news;
import java.util.List;

public class NewsResponse {
    private String status;
    private List<Article> articles;

    public String getStatus() {
        return status;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
