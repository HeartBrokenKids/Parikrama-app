package com.example.parikramaapp.explore;

import com.google.gson.annotations.SerializedName;

public class OpenAIChoice {
    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }
}
