package com.commercial.tuds.earnandpay;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.Models.UserDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

public class FundGenerationActivity extends AppCompatActivity {
    TextView amountTV;
    DatabaseReference databaseReference;
    ImageView backIcon;
    AVLoadingIndicatorView loadingIndicatorView;
    RelativeLayout thankBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_generation);
        amountTV = findViewById(R.id.amount);
        loadingIndicatorView = findViewById(R.id.loader);
        backIcon = findViewById(R.id.back_icon);
        thankBtn = findViewById(R.id.thankBtn);
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        amountTV.setAlpha(0f);
        loadingIndicatorView.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                if(userDetails == null)
                    return;
                String amount = "₹ "+userDetails.getLoanEligibilityAmount();
                amountTV.setText(amount);
                thankBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                loadingIndicatorView.setVisibility(View.INVISIBLE);
                amountTV.setAlpha(1f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backIcon.setOnClickListener(v -> {
            startActivity(new Intent(this,MainActivity.class));
            finishAffinity();
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finishAffinity();
    }
}
