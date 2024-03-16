package com.example.parikramaapp.translate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;


import java.util.ArrayList;
import java.util.Locale;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class TranslateFragment extends Fragment {

    private static final int SPEECH_REQUEST_CODE = 123;
    private TextToSpeech textToSpeech;
    private TextView translatedText;
    private Spinner languageSpinner;
    private static final String[] LANGUAGES = {
            "Hindi", "Bengali", "Telugu", "Marathi", "Tamil", "Urdu", "Gujarati", "Kannada", "Odia", "Punjabi","Spanish", "French", "German", "Italian"
    };

    private Translate translate;

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
        languageSpinner = rootView.findViewById(R.id.languageSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, LANGUAGES);
        languageSpinner.setAdapter(adapter);

        // Initialize the translation service
        String apiKey = getString(R.string.maps_api_key);
        translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();



        speechInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });
        Button speechOutputButton = rootView.findViewById(R.id.speechOutputButton);

        speechOutputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakTranslatedText(translatedText.getText().toString());
            }
        });

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TranslateFragment", "Language not supported");
                    }
                } else {
                    Log.e("TranslateFragment", "Initialization failed");
                }
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
        String selectedLanguage = languageSpinner.getSelectedItem().toString();
        String targetLanguageCode = getLanguageCode(selectedLanguage);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String translatedText = "";
                try {
                    // Translate the spoken text with auto detection of source language
                    Translation translation = translate.translate(spokenText,
                            Translate.TranslateOption.targetLanguage(targetLanguageCode));

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

    private void speakTranslatedText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "translate");
        }
    }

    private String getLanguageCode(String language) {
        switch (language) {
            case "Hindi":
                return "hi";
            case "Spanish":
                return "es";
            case "French":
                return "fr";
            case "German":
                return "de";
            case "Italian":
                return "it";
            case "Bengali":
                return "bn";
            case "Telugu":
                return "te";
            case "Marathi":
                return "mr";
            case "Tamil":
                return "ta";
            case "Urdu":
                return "ur";
            case "Gujarati":
                return "gu";
            case "Kannada":
                return "kn";
            case "Odia":
                return "or";
            case "Punjabi":
                return "pa";
            default:
                return "en"; // Default to English
        }
    }
}
