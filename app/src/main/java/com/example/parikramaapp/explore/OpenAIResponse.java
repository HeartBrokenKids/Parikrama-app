package com.example.parikramaapp.explore;

import com.google.gson.annotations.SerializedName;

public class OpenAIResponse {
    @SerializedName("choices")
    private OpenAIChoice[] choices;

    public String getGeneratedText() {
        if (choices != null && choices.length > 0) {
            return choices[0].getText();
        }
        return null;
    }
}
