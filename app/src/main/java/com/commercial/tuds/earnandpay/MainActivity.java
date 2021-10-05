package com.commercial.tuds.earnandpay;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.UserDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle mToggle;
    NavigationView navigationView;
    DatabaseReference databaseReference;
    ImageView addMoneyIcon, checkBalanceIcon, transactionHistoryIcon, payMoneyIcon, prepaidRechargeIcon, postpaidRechargeIcon, sendMoneyIcon, gasBillIcon, upiIcon, dthRechargeIcon,
            electricityBillIcon, bankTransferIcon, loanEligibilityIcon, loanApplicationIcon, fundGenerationIcon, emiCalculatorIcon, kycApprovalIcon, loanHistoryIcon, searchIcon,
            discountCardImg;
    LinearLayout receivedCards, createdCards, buyCards, createCards;
    private AlertDialog dialog;
    private UserDetails userDetails;
    ViewPager mPager;
    CircleIndicator circleIndicator;
    NestedScrollView mainLayout;
    AVLoadingIndicatorView loadingIndicatorView;
    private int currentPage, NUM_PAGES = 3;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        FirebaseApp.initializeApp(this);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("tag", "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    Log.e("token", token);
                    FirebaseDatabase.getInstance().getReference().child("meta_data").child("device_tokens").child(FirebaseAuth.getInstance().getUid()).setValue(token);

                });


        View headerLayout = navigationView.getHeaderView(0);
        final TextView nameTV = headerLayout.findViewById(R.id.name);
        final TextView emailTV = headerLayout.findViewById(R.id.email);
        final TextView phoneTV = headerLayout.findViewById(R.id.phone);
        final CircleImageView profileImageView = headerLayout.findViewById(R.id.user_image);
        mPager = findViewById(R.id.pager);
        circleIndicator = findViewById(R.id.indicator);
        circleIndicator.setViewPager(mPager);
        mainLayout = findViewById(R.id.scrollView);
        loadingIndicatorView = findViewById(R.id.loaderAnimation);
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        loadingIndicatorView.show();
        mainLayout.setVisibility(View.INVISIBLE);
        GenericMethod.showOfferPhotos(MainActivity.this, mPager, circleIndicator);
        discountCardImg = (ImageView) findViewById(R.id.discountCardImg);
        discountCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(MainActivity.this, DiscountCardActivity.class);
                startActivity(i);
            }
        });


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDetails = dataSnapshot.getValue(UserDetails.class);
                if (userDetails != null) {
                    if (userDetails.getProfile_url() != null && !userDetails.getProfile_url().matches(""))
                        Glide.with(getApplicationContext()).load(userDetails.getProfile_url()).into(profileImageView);
                    else
                        profileImageView.setImageDrawable(getResources().getDrawable(R.drawable.no_user));

                    if (userDetails.getUsername() != null && !userDetails.getUsername().matches(""))
                        nameTV.setText(userDetails.getUsername());
                    else
                        nameTV.setText("Anonymous User");

                    if (userDetails.getEmail() != null && !userDetails.getEmail().matches(""))
                        emailTV.setText(userDetails.getEmail());
                    else
                        emailTV.setText("N/A");
                    if (userDetails.getMobile() != null && !userDetails.getMobile().matches(""))
                        phoneTV.setText(userDetails.getMobile());
                    else
                        phoneTV.setText("N/A");
                }
                loadingIndicatorView.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(MainActivity.this, ViewProfileActivity.class));
                    break;
                case R.id.nav_notification:
                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                    break;
                case R.id.nav_benificiary:
                    startActivity(new Intent(MainActivity.this, AddAccountActivity.class));
                    break;
                case R.id.nav_faq:
                    startActivity(new Intent(MainActivity.this, FAQActivity.class));
                    break;
                case R.id.nav_tandc:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.tAndCLink))));
                    break;
                case R.id.nav_aboutus:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.aboutUsLink))));
                    break;
                case R.id.nav_privacyPolicy:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacyPolicyLink))));
                    break;
                case R.id.nav_refund_policy:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.refundPolicyLink))));
                    break;
                case R.id.nav_contact_us:
                    startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                    break;
                case R.id.nav_logout:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Log Out");
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            SharedPreferences loginpreferences = getSharedPreferences(SplashActivity.LoginPreference, MODE_PRIVATE);
                            SharedPreferences.Editor editor = loginpreferences.edit();
                            editor.putBoolean("isLoggedIn", false);
                            editor.commit();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finishAffinity();
                        }
                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    dialog = builder.create();
                    dialog.setOnShowListener(dialog1 -> {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    });
                    dialog.show();
                    break;
            }
            return true;
        });

        addMoneyIcon = findViewById(R.id.add_money_icon);
        checkBalanceIcon = findViewById(R.id.check_balance_icon);
        transactionHistoryIcon = findViewById(R.id.transaction_history_icon);
        payMoneyIcon = findViewById(R.id.pay_money_icon);
        prepaidRechargeIcon = findViewById(R.id.prepaid_recharge_icon);
        postpaidRechargeIcon = findViewById(R.id.postpaid_recharge_icon);
        sendMoneyIcon = findViewById(R.id.send_money_icon);
        gasBillIcon = findViewById(R.id.gas_bill_icon);
        upiIcon = findViewById(R.id.upi_icon);
        dthRechargeIcon = findViewById(R.id.dth_icon);
        electricityBillIcon = findViewById(R.id.electricity_icon);
        bankTransferIcon = findViewById(R.id.bank_transfer_icon);
        loanEligibilityIcon = findViewById(R.id.loan_eligibility_icon);
        loanApplicationIcon = findViewById(R.id.loan_application_icon);
        fundGenerationIcon = findViewById(R.id.fund_generation_icon);
        emiCalculatorIcon = findViewById(R.id.emi_calculator_icon);
        kycApprovalIcon = findViewById(R.id.kyc_approval_icon);
        loanHistoryIcon = findViewById(R.id.loan_history_icon);
        searchIcon = findViewById(R.id.search_icon);
        receivedCards = findViewById(R.id.receivedCards);
        createdCards = findViewById(R.id.createdCards);
        buyCards = findViewById(R.id.buyCards);
        createCards = findViewById(R.id.createCards);

        receivedCards.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewCardsActivity.class)));
        createdCards.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewCreatedCardsActivity.class)));
        buyCards.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BuyDiscountCardsActivity.class)));
        createCards.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DiscountCardActivity.class)));

        addMoneyIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddMoneyActivity.class)));
        checkBalanceIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CheckBalanceActivity.class)));
        transactionHistoryIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TransactionHistoryActivity.class)));
        payMoneyIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PayMoneyActivity.class)));
        prepaidRechargeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RechargeActivity.class);
            intent.putExtra("rechargeType", "Prepaid");
            startActivity(intent);
        });
        postpaidRechargeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RechargeActivity.class);
            intent.putExtra("rechargeType", "Postpaid");
            startActivity(intent);
        });
        sendMoneyIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, InvestMoney.class)));
        gasBillIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GasBillPaymentActivity.class)));
        upiIcon.setOnClickListener(v -> Toast.makeText(MainActivity.this, "This feature is not yet available.", Toast.LENGTH_LONG).show());
        dthRechargeIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DthRechargeActivity.class)));
        electricityBillIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ElectricityBillActivity.class)));
        bankTransferIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BankTransferActivity.class)));
        searchIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));
        loanEligibilityIcon.setOnClickListener(v -> {
            String eligibilityStatus = userDetails.getLoanEligibilityStatus();
            if (eligibilityStatus.matches("true")) {
                GenericMethod.showMessage(MainActivity.this, "You are already eligible for a loan");
                startActivity(new Intent(MainActivity.this, FundGenerationActivity.class));
            } else if (eligibilityStatus.matches("pending")) {
                GenericMethod.showMessage(MainActivity.this, "Your application is under processing.Please check later.");
            } else if (eligibilityStatus.matches("false"))
                startActivity(new Intent(MainActivity.this, LoanEligibilityActivity.class));
        });
        loanApplicationIcon.setOnClickListener(v -> {
            if (userDetails.getLoanEligibilityStatus().matches("true"))
                startActivity(new Intent(MainActivity.this, LoanApplicationActivity.class));
            else if (userDetails.getLoanEligibilityStatus().matches("pending"))
                GenericMethod.showMessage(MainActivity.this, "Your application is under processing.Please check later.");
            else
                GenericMethod.showMessage(MainActivity.this, "Please check your loan eligibility first.");

        });
        fundGenerationIcon.setOnClickListener(v -> {
            String eligibilityStatus = userDetails.getLoanEligibilityStatus();
            if (eligibilityStatus.matches("true")) {
                startActivity(new Intent(MainActivity.this, FundGenerationActivity.class));
            } else if (eligibilityStatus.matches("pending")) {
                GenericMethod.showMessage(MainActivity.this, "Your application is under processing.Please check later.");
            } else if (eligibilityStatus.matches("false")) {
                GenericMethod.showMessage(MainActivity.this, "Please check your loan eligibility first.");
                startActivity(new Intent(MainActivity.this, LoanEligibilityActivity.class));
            }
        });
        emiCalculatorIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EmiCalculatorActivity.class)));
        kycApprovalIcon.setOnClickListener(v -> {
            if (userDetails.getKycStatus())
                GenericMethod.showMessage(MainActivity.this, "Your KYC has been completed successfully");
            else
                startActivity(new Intent(MainActivity.this, KycApprovalActivity.class));
        });
        loanHistoryIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoanHistoryActivity.class)));

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
