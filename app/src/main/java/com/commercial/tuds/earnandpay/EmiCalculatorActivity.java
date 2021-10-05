package com.commercial.tuds.earnandpay;

import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.Utils.DecimalFilter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;

public class EmiCalculatorActivity extends AppCompatActivity {

    TextInputEditText amountET,timeET;
    RelativeLayout calculateBtn;
    TextView emiAmountTV;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emi_calculator);

        amountET = findViewById(R.id.amountET);
        timeET = findViewById(R.id.timeET);
        calculateBtn = findViewById(R.id.calculateBtn);
        emiAmountTV = findViewById(R.id.resultAmount);
        backIcon = findViewById(R.id.back_icon);
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        amountET.setFilters(new InputFilter[] {new DecimalFilter(6,2)});



        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float amount = Float.parseFloat(amountET.getText().toString().trim());
                int time = Integer.parseInt(timeET.getText().toString().trim());
                float interest = 10f;
                DecimalFormat df = new DecimalFormat("#.##");
                String emiAmount = "₹ " + df.format((amount+interest)/time);
                emiAmountTV.setText(emiAmount);
                findViewById(R.id.perMonthText).setVisibility(View.VISIBLE);
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
