package com.example.parikramaapp.explore;

import com.google.gson.annotations.SerializedName;

public class OpenAIResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("object")
    private String object;

    @SerializedName("created")
    private long created;

    @SerializedName("model")
    private String model;

    @SerializedName("choices")
    private OpenAIChoice[] choices;

    // Getters for the fields

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public long getCreated() {
        return created;
    }

    public String getModel() {
        return model;
    }

    public OpenAIChoice[] getChoices() {
        return choices;
    }

    // Method to extract the content from the first choice
    public String getGeneratedText() {
        if (choices != null && choices.length > 0 && choices[0] != null && choices[0].getContent() != null) {
            return choices[0].getContent();
        }
        return null;
    }
}
