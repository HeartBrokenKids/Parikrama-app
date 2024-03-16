package com.example.parikramaapp.foodRescue;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.Timestamp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddFoodRescueCaseFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MAP_REQUEST_CODE = 2;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText locationEditText;
    private Button expiryTimeButton, selectImage;
    private EditText quantityEditText;
    private ImageView imageView;
    private Button uploadButton;

    private Uri imageUri;
    private Calendar expiryDateCalendar; // Added Calendar instance

    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_food_rescue_case, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        expiryTimeButton = view.findViewById(R.id.expiryTimeButton);
        quantityEditText = view.findViewById(R.id.quantityEditText);
        imageView = view.findViewById(R.id.imageView);
        locationEditText = view.findViewById(R.id.locationEditText);
        selectImage = view.findViewById(R.id.selectImageButton);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        Button selectLocationButton = view.findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch map activity here
                Intent mapIntent = new Intent(getContext(), MapActivity.class);
                startActivityForResult(mapIntent, MAP_REQUEST_CODE);
            }
        });

        Button submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFoodRescueCase();
            }
        });

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("food_rescue_images");

        expiryDateCalendar = Calendar.getInstance(); // Initialize Calendar instance

        expiryTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();
            }
        });

        return view;
    }

    private void showDateTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        expiryDateCalendar.set(Calendar.YEAR, year);
                        expiryDateCalendar.set(Calendar.MONTH, monthOfYear);
                        expiryDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        expiryDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        expiryDateCalendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String formattedDateTime = sdf.format(expiryDateCalendar.getTime());

                        expiryTimeButton.setText(formattedDateTime);
                    }
                }, hour, minute, false);

                timePickerDialog.show();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void openFileChooser() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            Log.d("ImageSelection", "Image URI: " + imageUri.toString());
        } else if (requestCode == MAP_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            String selectedLocation = latitude + ", " + longitude;

            // Initialize locationEditText before setting its text
            locationEditText = requireView().findViewById(R.id.locationEditText);
            locationEditText.setText(selectedLocation);
        }
    }

    private void uploadFoodRescueCase() {
        final String title = titleEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String[] latLong = location.split(",");
        double latitude = Double.parseDouble(latLong[0]);
        double longitude = Double.parseDouble(latLong[1]);
        GeoPoint geoPoint = new GeoPoint(latitude, longitude);

        final String quantity = quantityEditText.getText().toString().trim();

        if (imageUri == null) {
            // Handle the case where no image is selected
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        fileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Once the image is successfully uploaded, get its download URL
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                String donorId = "user";
                                String status = "available";
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                String expiryTimeString = sdf.format(expiryDateCalendar.getTime());
                                Timestamp expiryTime = null;
                                try {
                                    expiryTime = new Timestamp(sdf.parse(expiryTimeString));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                Timestamp timePosted = new Timestamp(new Date());

                                Map<String, Object> foodRescueCase = new HashMap<>();
                                foodRescueCase.put("title", title);
                                foodRescueCase.put("description", description);
                                foodRescueCase.put("location", geoPoint);
                                foodRescueCase.put("expiryTime", expiryTime);
                                foodRescueCase.put("quantity", quantity);
                                foodRescueCase.put("donor_id", donorId);
                                foodRescueCase.put("status", status);
                                foodRescueCase.put("timePosted", timePosted); // Use the timestamp
                                foodRescueCase.put("imageUrl", imageUrl);

                                db.collection("food_listings")
                                        .add(foodRescueCase)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getContext(), "Food rescue case added successfully", Toast.LENGTH_SHORT).show();
                                                // Clear the form fields after successful upload
                                                clearFormFields();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Failed to add food rescue case", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFormFields() {
        // Clear all form fields after successful upload
        titleEditText.setText("");
        descriptionEditText.setText("");
        expiryTimeButton.setText("");
        quantityEditText.setText("");
        imageView.setImageResource(android.R.color.transparent); // Clear the image view
        locationEditText.setText("");
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }
}
