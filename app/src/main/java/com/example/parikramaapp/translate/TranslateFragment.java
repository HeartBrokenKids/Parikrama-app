package com.example.parikramaapp.translate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;

import java.util.ArrayList;
import java.util.Locale;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class TranslateFragment extends Fragment {

    private static final int SPEECH_REQUEST_CODE = 123;
    private TextView translatedText;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TranslateFragment() {
        // Required empty public constructor
    }

    public static TranslateFragment newInstance(String param1, String param2) {
        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);

        Button speechInputButton = rootView.findViewById(R.id.speechInputButton);
        translatedText = rootView.findViewById(R.id.translatedText);

        speechInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });

        return rootView;
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                String spokenText = result.get(0);
                // Call the method to translate the spoken text
                translateText(spokenText);
            }
        }
    }

    private void translateText(final String spokenText) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String translatedText = "";
                try {
                    String apiKey = "AIzaSyB7xiHtZANrnvxul8jvjNLtbyzHgJMeyos"; // Replace with your Google Cloud API key

                    // Initialize the translation service
                    Translate translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
                    Log.d("spoken - ",spokenText);
                    // Translate the spoken text with auto detection of source language
                    Translation translation = translate.translate(spokenText,
                            Translate.TranslateOption.targetLanguage("hi")); // Hindi as the target language

                    // Get the translated text
                    translatedText = translation.getTranslatedText();
                } catch (Exception e) {
                    Log.e("TranslateFragment", "Error translating text: " + e.getMessage());
                }
                return translatedText;
            }

            @Override
            protected void onPostExecute(String translatedTexts) {
                // Update the translatedText TextView with the translated result
                translatedText.setText(translatedTexts);
            }
        }.execute();
    }


}