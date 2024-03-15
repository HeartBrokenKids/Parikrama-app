package com.example.parikramaapp.explore;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class OpenAIChoice {
    @SerializedName("text")
    private String text;

    @SerializedName("message")
    private Map<String, String> message;

    public String getText() {
        return text;
    }

    public String getContent() {
        if (message != null && message.containsKey("content")) {
            return message.get("content");
        }
        return null;
    }
}
