package com.commercial.tuds.earnandpay;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class CheckBalanceActivity extends AppCompatActivity {
    TextView MainBalanceTV, FixedBalanceTV;
    DatabaseReference databaseReference;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    ImageView backIcon;
    ViewPager mPager;
    private int currentPage, NUM_PAGES = 3;
    CircleIndicator circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);

        MainBalanceTV = findViewById(R.id.mainBalance);
        FixedBalanceTV = findViewById(R.id.fixedBalance);
        backIcon = findViewById(R.id.back_icon);
        loadingView = findViewById(R.id.loader);
        scrollView = findViewById(R.id.scrollView);
        mPager = findViewById(R.id.pager);
        circleIndicator = findViewById(R.id.indicator);
        circleIndicator.setViewPager(mPager);
        GenericMethod.showOfferPhotos(CheckBalanceActivity.this, mPager, circleIndicator);
        scrollView.setAlpha(0f);
        loadingView.show();
        loadingView.setVisibility(View.VISIBLE);

        String myUid = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(myUid).child("mainBalance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingView.setVisibility(View.INVISIBLE);
                String mainBalance = new DecimalFormat("##.##").format(dataSnapshot.getValue(float.class));
                MainBalanceTV.setText("₹ " + mainBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid()).child("fixedBalance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FixedBalanceTV.setText("₹ " + dataSnapshot.getValue(float.class));
                loadingView.setVisibility(View.INVISIBLE);
                scrollView.setAlpha(1f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        });

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        circleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}
