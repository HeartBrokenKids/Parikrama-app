package com.example.parikramaapp.explore;

import java.util.List;
import java.util.Map;

public class OpenAIRequest {
    private String model;
    private List<Map<String, String>> messagesList;

    public OpenAIRequest(String model, List<Map<String, String>> messagesList) {
        this.model = model; // Use the value passed to the constructor
        this.messagesList = messagesList;
    }

    public String getModel() {
        return model;
    }

    public List<Map<String, String>> getMessagesList() {
        return messagesList;
    }
}
