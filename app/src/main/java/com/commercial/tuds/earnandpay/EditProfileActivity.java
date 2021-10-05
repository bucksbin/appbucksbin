package com.commercial.tuds.earnandpay;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.Address;
import com.commercial.tuds.earnandpay.Models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.commercial.tuds.earnandpay.GenericMethods.GenericMethod.getImageFile;

public class EditProfileActivity extends AppCompatActivity {
    String profileName, emailId, flatNumber, streetName, pincode, city, district, state;
    TextInputEditText nameET, emailET, flatnumberET, streetNameET, pincodeET, cityET, districtET, stateET;
    CircleImageView profileImageView;
    ImageView ChangeProfilePicBtn;
    RelativeLayout SaveProfileBtn;
    private String currentPhotoPath;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private final int PICK_CONTACT = 105;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    FrameLayout profileLayout;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        flatnumberET = findViewById(R.id.flatNumberET);
        streetNameET = findViewById(R.id.streetET);
        pincodeET = findViewById(R.id.pincodeET);
        cityET = findViewById(R.id.cityET);
        districtET = findViewById(R.id.districtET);
        stateET = findViewById(R.id.stateET);
        profileImageView = findViewById(R.id.profileImage);
        SaveProfileBtn = findViewById(R.id.saveBtn);
        ChangeProfilePicBtn = findViewById(R.id.changeProfileBtn);
        loadingView = findViewById(R.id.loader);
        scrollView = findViewById(R.id.scrollView);
        profileLayout = findViewById(R.id.profileLayout);
        backIcon = findViewById(R.id.back_icon);

        //Setting loading animation
        scrollView.setAlpha(0f);
        profileLayout.setAlpha(0f);
        loadingView.show();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                if (userDetails.getProfile_url() != null && !userDetails.getProfile_url().matches(""))
                    Glide.with(profileImageView.getContext()).load(userDetails.getProfile_url()).into(profileImageView);
                else
                    profileImageView.setImageDrawable(getResources().getDrawable(R.drawable.no_user));

                if (userDetails.getUsername() != null && !userDetails.getUsername().matches(""))
                    nameET.setText(userDetails.getUsername());

                if (userDetails.getEmail() != null && !userDetails.getEmail().matches(""))
                    emailET.setText(userDetails.getEmail());

                if (userDetails.getAddress() != null && userDetails.getAddress().getFlatNumber() != null && !userDetails.getAddress().getFlatNumber().matches(""))
                    flatnumberET.setText(userDetails.getAddress().getFlatNumber());

                if (userDetails.getAddress() != null && userDetails.getAddress().getStreet() != null && !userDetails.getAddress().getStreet().matches(""))
                    streetNameET.setText(userDetails.getAddress().getStreet());

                if (userDetails.getAddress() != null && userDetails.getAddress().getPincode() != null && !userDetails.getAddress().getPincode().matches(""))
                    pincodeET.setText(userDetails.getAddress().getPincode());

                if (userDetails.getAddress() != null && userDetails.getAddress().getCity() != null && !userDetails.getAddress().getCity().matches(""))
                    cityET.setText(userDetails.getAddress().getCity());

                if (userDetails.getAddress() != null && userDetails.getAddress().getDistrict() != null && !userDetails.getAddress().getDistrict().matches(""))
                    districtET.setText(userDetails.getAddress().getDistrict());

                if (userDetails.getAddress() != null && userDetails.getAddress().getState() != null && !userDetails.getAddress().getState().matches(""))
                    stateET.setText(userDetails.getAddress().getState());

                scrollView.setAlpha(1f);
                profileLayout.setAlpha(1f);
                loadingView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);

        SaveProfileBtn.setOnClickListener(v -> {
            profileName = nameET.getText().toString();
            emailId = emailET.getText().toString();
            flatNumber = flatnumberET.getText().toString();
            streetName = streetNameET.getText().toString();
            pincode = pincodeET.getText().toString();
            city = cityET.getText().toString();
            district = districtET.getText().toString();
            state = stateET.getText().toString();

            final Address address = new Address(flatNumber, streetName, city, district, state, pincode);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                    userDetails.setAddress(address);
                    userDetails.setUsername(profileName);
                    userDetails.setEmail(emailId);
                    FirebaseDatabase.getInstance().getReference().child("meta_data").child("user").child(userDetails.getMobile()).child("username").setValue(profileName);
                    databaseReference.setValue(userDetails);
                    startActivity(new Intent(EditProfileActivity.this, ViewProfileActivity.class));
                    databaseReference.removeEventListener(valueEventListener);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        ChangeProfilePicBtn.setOnClickListener(view -> Dexter.withActivity(EditProfileActivity.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            final String[] options = {"Take Photo", "Choose From Gallery", "Remove Photo", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                            builder.setTitle("Choose an option");
                            builder.setItems(options, (dialog, item) -> {
                                switch (item) {
                                    case 0:
                                        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        File file = getImageFile();
                                        currentPhotoPath = "file:" + file.getAbsolutePath();
                                        Uri uri;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                            uri = FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
                                        else
                                            uri = Uri.fromFile(file);
                                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(pictureIntent, PICK_CONTACT);
                                        break;
                                    case 1:
                                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                        galleryIntent.setType("image/*");
                                        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                        String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
                                        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                                        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_CONTACT);
                                        break;
                                    case 2:
                                        databaseReference.child("profile_url").setValue("");
                                        GenericMethod.showMessage(EditProfileActivity.this, "Profile successfully removed");
                                        break;
                                    case 3:
                                        if (dialog != null)
                                            dialog.dismiss();
                                        break;
                                }
                            });
                            builder.show();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check());

        backIcon.setOnClickListener(v -> finish());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap;
            switch (requestCode) {
                case 0:
                    Uri uri = Uri.parse(currentPhotoPath);
                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("tag", "i am here");
                    UCrop.of(uri, uri)
                            .withMaxResultSize(bitmap.getWidth(), bitmap.getHeight())
                            .withAspectRatio(1, 1)
                            .start(this);
                    break;
                case 1:
                    Uri sourceUri = data.getData();
                    File file = getImageFile();
                    Uri destinationUri = Uri.fromFile(file);
                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), sourceUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UCrop.of(sourceUri, destinationUri)
                            .withMaxResultSize(bitmap.getWidth(), bitmap.getHeight())
                            .withAspectRatio(1, 1)
                            .start(this);
                    break;
                case UCrop.REQUEST_CROP:
                    Uri uri1 = UCrop.getOutput(data);
                    File file1 = new File(FileUtils.getPath(this, uri1));
                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(file1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    scrollView.setAlpha(0f);
                    loadingView.setVisibility(View.VISIBLE);
                    storageReference = FirebaseStorage.getInstance().getReference();
                    String Uid = FirebaseAuth.getInstance().getUid();
                    storageReference = storageReference.child("userProfileImages/" + Uid + ".jpg");
                    UploadTask uploadTask = storageReference.putStream(inputStream);
                    Log.e("tag", "uploading");
                    uploadTask.addOnCompleteListener(task -> {
                        loadingView.setVisibility(View.INVISIBLE);
                        scrollView.setAlpha(1f);
                        storageReference.getDownloadUrl().addOnSuccessListener(uri2 -> databaseReference.child("profile_url").setValue(uri2.toString())).addOnFailureListener(e -> GenericMethod.showMessage(EditProfileActivity.this, "Sorry, Image could not be uploaded due to some reasons"));
                    }).addOnFailureListener(e -> {
                        GenericMethod.showMessage(EditProfileActivity.this, "Sorry, Image could not be uploaded due to some reasons");
                        return;
                    }).addOnSuccessListener(taskSnapshot -> GenericMethod.showMessage(EditProfileActivity.this, "Your profile Image has been uploaded successfully."));
                    break;
            }
        }
        backIcon.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

}
