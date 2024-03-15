package com.example.parikramaapp.explore;

import android.net.wifi.aware.DiscoverySession;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.parikramaapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatBotFragment extends Fragment {
    private EditText userInput;
    private TextView chatOutput;
    private List<Map<String, String>> messagesList = new ArrayList<>();
    private static final String TAG = "ChatBotFragment";
    private static final String OPENAI_API_KEY = "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";
    private static final String OPENAI_API_URL = "https://api.openai.com";

    public ChatBotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_bot, container, false);
        userInput = view.findViewById(R.id.user_input);
        chatOutput = view.findViewById(R.id.chat_output);
        Button sendButton = view.findViewById(R.id.send_button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OPENAI_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenAIApiService openAIInterface = retrofit.create(OpenAIApiService.class);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> initialContextMessage = new HashMap<>();
                initialContextMessage.put("role", "system");
//                initialContextMessage.put("role", "user");
                String currentLocationCity = "Mumbai"; // need to get location use case
                initialContextMessage.put("content", "You are a city-based assistant app. The city is "+currentLocationCity);
                messagesList.add(initialContextMessage);

//                Map<String, String> userCityMessage = new HashMap<>();
//                userCityMessage.put("role", "user");
//                userCityMessage.put("content", currentLocationCity);
//                messagesList.add(userCityMessage);

                String inputText = userInput.getText().toString();
                appendMessage("User: " + inputText);
                Map<String, String> userMessage = new HashMap<>();
                userMessage.put("role", "user");
                userMessage.put("content", inputText);
                messagesList.add(userMessage);

                // Make a request to OpenAI API
                sendRequestToOpenAI(messagesList, openAIInterface);

                userInput.getText().clear();
            }
        });

        return view;
    }

    private void sendRequestToOpenAI(List<Map<String, String>> messagesList, OpenAIApiService openAIInterface) {
        // Create an instance of OpenAIRequest with the model and messages list
        OpenAIRequest openAIRequest = new OpenAIRequest("gpt-3.5-turbo", messagesList);

        try {
            // Pass the OpenAIRequest object to the sendMessage method
            Call<OpenAIResponse> call = openAIInterface.sendMessage("Bearer " + OPENAI_API_KEY, openAIRequest);
            Log.d(TAG, "Sending message to OpenAI: " + new Gson().toJson(openAIRequest)); // Log sending message
            call.enqueue(new Callback<OpenAIResponse>() {
                @Override
                public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        OpenAIResponse openAIResponse = response.body();
                        String botResponse = openAIResponse.getGeneratedText(); // using getGeneratedText method
                        appendMessage("Wanderlust AI: " + botResponse);
                        Log.d(TAG, "Received response from OpenAI: " + botResponse); // Log received response
                    } else {
                        Log.e(TAG, "Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                    Log.e(TAG, "Error sending message to OpenAI: " + t.getMessage(), t);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error creating Retrofit call: " + e.getMessage(), e);
        }
    }


    private void appendMessage(String message) {
        chatOutput.append("\n" + message);
    }
}
