package com.commercial.tuds.earnandpay;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
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

public class LoanHistoryActivity extends AppCompatActivity {
    TextView totalLoanAmount,amountPaid,dueAmount;
    DatabaseReference databaseReference;
    AVLoadingIndicatorView loadingIndicatorView;
    NestedScrollView scrollView;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_history);

        totalLoanAmount = findViewById(R.id.totalLoanAmount);
        amountPaid = findViewById(R.id.paidLoanAmount);
        dueAmount = findViewById(R.id.dueLoanAmount);
        loadingIndicatorView = findViewById(R.id.loader);
        scrollView = findViewById(R.id.scrollView);
        loadingIndicatorView.show();
        scrollView.setAlpha(0f);
        backIcon = findViewById(R.id.back_icon);
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                totalLoanAmount.setText("₹ "+userDetails.getTotalLoanCredited());
                amountPaid.setText("₹ "+userDetails.getLoanPaidAmount());
                dueAmount.setText("₹ "+userDetails.getDueLoanAmount());
                loadingIndicatorView.hide();
                scrollView.setAlpha(1f);
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
