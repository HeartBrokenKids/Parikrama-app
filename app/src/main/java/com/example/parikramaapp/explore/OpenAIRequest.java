package com.example.parikramaapp.explore;

import java.util.List;
import java.util.Map;

public class OpenAIRequest {
    private String model;
    private List<Map<String, String>> messages;

    public OpenAIRequest(String model, List<Map<String, String>> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public List<Map<String, String>> getMessagesList() {
        return messages;
    }
}
