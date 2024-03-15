package com.example.parikramaapp.foodRescue;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parikramaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddFoodRescueCaseFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText locationEditText;
    private EditText expiryTimeEditText;
    private EditText quantityEditText;
    private ImageView imageView;
    private Button uploadButton;

    private Uri imageUri;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_food_rescue_case, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        expiryTimeEditText = view.findViewById(R.id.expiryTimeEditText);
        quantityEditText = view.findViewById(R.id.quantityEditText);
        imageView = view.findViewById(R.id.imageView);
        uploadButton = view.findViewById(R.id.uploadButton);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("food_rescue_images");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFoodRescueCase();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadFoodRescueCase() {
        final String title = titleEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();
        final String location = locationEditText.getText().toString().trim();
        final String expiryTime = expiryTimeEditText.getText().toString().trim();
        final String quantity = quantityEditText.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || expiryTime.isEmpty() || quantity.isEmpty() || imageUri == null) {
            Toast.makeText(getContext(), "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        fileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                String donorId = "user1"; // hardcoded donor_id
                                String status = "available"; // default status

                                Map<String, Object> foodRescueCase = new HashMap<>();
                                foodRescueCase.put("title", title);
                                foodRescueCase.put("description", description);
                                foodRescueCase.put("location", location);
                                foodRescueCase.put("expiryTime", expiryTime);
                                foodRescueCase.put("quantity", quantity);
                                foodRescueCase.put("donor_id", donorId);
                                foodRescueCase.put("status", status);
                                foodRescueCase.put("timePosted", new Date().getTime()); // current time
                                foodRescueCase.put("imageUrl", imageUrl);

                                db.collection("food_rescue_cases")
                                        .add(foodRescueCase)
                                        .addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Toast.makeText(getContext(), "Food rescue case added successfully", Toast.LENGTH_SHORT).show();
                                                // Clear form fields and image view
                                                titleEditText.setText("");
                                                descriptionEditText.setText("");
                                                locationEditText.setText("");
                                                expiryTimeEditText.setText("");
                                                quantityEditText.setText("");
                                                imageView.setImageResource(android.R.color.transparent);
                                                imageUri = null;
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

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }
}
