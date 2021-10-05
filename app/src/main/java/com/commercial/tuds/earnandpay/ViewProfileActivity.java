package com.commercial.tuds.earnandpay;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commercial.tuds.earnandpay.Models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileActivity extends AppCompatActivity {

    TextView nameTV, emailTV, flatnumberTV, streetNameTV, pincodeTV, cityTV, districtTV, stateTV;
    CircleImageView profileImageView;
    RelativeLayout EditProfileBtn;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    FrameLayout profileLayout;
    ImageView backIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        nameTV = findViewById(R.id.name);
        emailTV = findViewById(R.id.email);
        flatnumberTV = findViewById(R.id.flatNumber);
        streetNameTV = findViewById(R.id.street);
        pincodeTV = findViewById(R.id.pincode);
        cityTV = findViewById(R.id.city);
        districtTV = findViewById(R.id.district);
        stateTV = findViewById(R.id.state);
        profileImageView = findViewById(R.id.profileImage);
        EditProfileBtn = findViewById(R.id.editBtn);
        loadingView = findViewById(R.id.loader);
        backIcon = findViewById(R.id.back_icon);
        scrollView = findViewById(R.id.scrollView);
        profileLayout = findViewById(R.id.profileLayout);
        scrollView.setAlpha(0f);
        profileLayout.setAlpha(0f);
        loadingView.show();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                if (userDetails.getProfile_url() != null && !userDetails.getProfile_url().matches(""))
                    Glide.with(profileImageView.getContext()).load(userDetails.getProfile_url()).into(profileImageView);
                else
                    profileImageView.setImageDrawable(getResources().getDrawable(R.drawable.no_user));

                if (userDetails.getUsername() != null && !userDetails.getUsername().matches(""))
                    nameTV.setText(userDetails.getUsername());
                else
                    nameTV.setHint("Name");

                if (userDetails.getEmail() != null && !userDetails.getEmail().matches(""))
                    emailTV.setText(userDetails.getEmail());
                else
                    emailTV.setHint("Email");

                if (userDetails.getAddress() != null && userDetails.getAddress().getFlatNumber() != null && !userDetails.getAddress().getFlatNumber().matches(""))
                    flatnumberTV.setText(userDetails.getAddress().getFlatNumber());
                else
                    flatnumberTV.setHint("Flat No");

                if (userDetails.getAddress() != null && userDetails.getAddress().getStreet() != null && !userDetails.getAddress().getStreet().matches(""))
                    streetNameTV.setText(userDetails.getAddress().getStreet());
                else
                    streetNameTV.setHint("Street");

                if (userDetails.getAddress() != null && userDetails.getAddress().getPincode() != null && !userDetails.getAddress().getPincode().matches(""))
                    pincodeTV.setText(userDetails.getAddress().getPincode());
                else
                    pincodeTV.setHint("Pincode");

                if (userDetails.getAddress() != null && userDetails.getAddress().getCity() != null && !userDetails.getAddress().getCity().matches(""))
                    cityTV.setText(userDetails.getAddress().getCity());
                else
                    cityTV.setHint("City");

                if (userDetails.getAddress() != null && userDetails.getAddress().getDistrict() != null && !userDetails.getAddress().getDistrict().matches(""))
                    districtTV.setText(userDetails.getAddress().getDistrict());
                else
                    districtTV.setText("District");

                if (userDetails.getAddress() != null && userDetails.getAddress().getState() != null && !userDetails.getAddress().getState().matches(""))
                    stateTV.setText(userDetails.getAddress().getState());
                else
                    stateTV.setText("State");
                scrollView.setAlpha(1f);
                profileLayout.setAlpha(1f);
                loadingView.hide();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
        EditProfileBtn.setOnClickListener(v -> {
            startActivity(new Intent(ViewProfileActivity.this, EditProfileActivity.class));
            databaseReference.removeEventListener(valueEventListener);
            finish();
        });

        backIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}
