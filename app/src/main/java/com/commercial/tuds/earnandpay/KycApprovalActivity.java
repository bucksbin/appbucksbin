package com.commercial.tuds.earnandpay;

import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.KYCApplication;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KycApprovalActivity extends AppCompatActivity {

    TextInputEditText nameET,aadharNoET,panNoET;
    RelativeLayout submitBtn;
    DatabaseReference databaseReference;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_approval);

        nameET = findViewById(R.id.nameET);
        aadharNoET = findViewById(R.id.aadharET);
        panNoET = findViewById(R.id.panNumberET);
        submitBtn = findViewById(R.id.submitBtn);
        backIcon = findViewById(R.id.back_icon);
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final AlphaAnimation buttonClickAnimation = new AlphaAnimation(1.0f, 0.8f);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setAnimation(buttonClickAnimation);
                String name = nameET.getText().toString().trim();
                String aadharNo = aadharNoET.getText().toString().trim();
                String panNo = panNoET.getText().toString().trim();

                if(name.matches("")) {
                    nameET.setError("Please enter name.");
                    return;
                }
                if(aadharNo.matches("")) {
                    aadharNoET.setError("Please enter aadhar no.");
                    return;
                }
                if(panNo.matches("")) {
                    panNoET.setError("Please enter pan no.");
                    return;
                }
                KYCApplication kycApplication = new KYCApplication(name,aadharNo,panNo);
                databaseReference = FirebaseDatabase.getInstance().getReference().child("kyc_application").child(FirebaseAuth.getInstance().getUid());
                databaseReference.setValue(kycApplication);
                GenericMethod.showMessage(KycApprovalActivity.this,"Your KYC application has been received.");
                finish();
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
