package com.example.parikramaapp.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class PreferencesDialogFragment extends DialogFragment {

    private static final String ARG_SERVICE_ITEMS = "serviceItems";
    private static final String ARG_CHECKED_ITEMS = "checkedItems";

    public interface PreferencesDialogListener {
        void onPreferencesSelected(boolean[] selectedPreferences);
    }

    private PreferencesDialogListener listener;
    private List<String> serviceNames;
    private boolean[] checkedItems;

    public static PreferencesDialogFragment newInstance(List<String> serviceNames, boolean[] checkedItems) {
        PreferencesDialogFragment fragment = new PreferencesDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_SERVICE_ITEMS, new ArrayList<>(serviceNames));
        args.putBooleanArray(ARG_CHECKED_ITEMS, checkedItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceNames = getArguments().getStringArrayList(ARG_SERVICE_ITEMS);
            checkedItems = getArguments().getBooleanArray(ARG_CHECKED_ITEMS);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PreferencesDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PreferencesDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence[] items = serviceNames != null ? serviceNames.toArray(new CharSequence[0]) : new CharSequence[0];

        builder.setTitle("Edit Preferences")
                .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> checkedItems[which] = isChecked)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    if (listener != null) {
                        listener.onPreferencesSelected(checkedItems);
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }
}
