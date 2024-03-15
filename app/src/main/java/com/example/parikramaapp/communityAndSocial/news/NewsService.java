package com.example.parikramaapp.communityAndSocial.news;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );
}
