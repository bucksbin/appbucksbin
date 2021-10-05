package com.commercial.tuds.earnandpay;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.commercial.tuds.earnandpay.Utils.Api;
import com.commercial.tuds.earnandpay.Utils.GenerateTokenPojo;
import com.commercial.tuds.earnandpay.Utils.NetworkLogUtility;
import com.commercial.tuds.earnandpay.Utils.NetworkUtility;
import com.commercial.tuds.earnandpay.Utils.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Utils.DecimalFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.gocashfree.cashfreesdk.CFPaymentService;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;


public class AddMoneyActivity extends AppCompatActivity //implements PaymentResultListener
{
    TextView availableBalance;
    DatabaseReference databaseReference;
    AVLoadingIndicatorView loadingView;
    ImageView backBtn;
    ViewPager mPager;
    float newWalletBal;
    int amount;
    String transId, timeCopy;
    EditText amountEt;
    RelativeLayout addMoneyBtn, parentLayout;
    CircleIndicator circleIndicator;
    private String transTime;
    private int currentPage, NUM_PAGES = 3;

    //production...
    String stage = "PROD";
    String appId = "27577fc9386810cb3fad7e83d77572";
    String orderNote = "bucksbin Order";
    String customerName = "bucksbin user";
    String customerPhone;
    String customerEmail = "bucksbin@gmail.com";
    Map<String, String> params = new HashMap<>();
    ProgressDialog progressDialog;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        amountEt = findViewById(R.id.amountET);
        addMoneyBtn = findViewById(R.id.addMoneyBtn);
        loadingView = findViewById(R.id.loader);
        parentLayout = findViewById(R.id.parentLayout);
        loadingView.show();
        final TextInputEditText amountEt = findViewById(R.id.amountET);
        backBtn = findViewById(R.id.back_icon);
        mPager = findViewById(R.id.pager);
        circleIndicator = findViewById(R.id.indicator);
        circleIndicator.setViewPager(mPager);
        GenericMethod.showOfferPhotos(AddMoneyActivity.this, mPager, circleIndicator);
        amountEt.setFilters(new InputFilter[]{new DecimalFilter(6, 2)});
        availableBalance = findViewById(R.id.availableBalance);
        String myUid = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(myUid).child("mainBalance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingView.setVisibility(View.INVISIBLE);
                String mainBalance = new DecimalFormat("##.##").format(dataSnapshot.getValue(float.class));
                availableBalance.setText("₹ " + mainBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user_details").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("mobile");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerPhone = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addMoneyBtn.setOnClickListener(v -> {
            if (amountEt.getText() == null || amountEt.getText().toString().length() == 0 || Integer.parseInt(amountEt.getText().toString()) == 0) {
                GenericMethod.showMessage(AddMoneyActivity.this, "Enter Valid Amount.");
                return;
            }
            amount = Integer.parseInt(amountEt.getText().toString());

//            Checkout checkout = new Checkout();
//            checkout.setImage(R.drawable.logo);
            @SuppressLint("SimpleDateFormat") DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
            dateFormatter.setLenient(false);
            Date today = new Date();
            transTime = dateFormatter.format(today);
            int time = (int) new Date().getTime();
            timeCopy = String.valueOf(time).replace("-", "");
            transId = FirebaseAuth.getInstance().getUid().substring(0, 5) + timeCopy;


            try {
                JSONObject options = new JSONObject();
                options.put("name", "BucksBin");
                options.put("description", transId);
                options.put("currency", "INR");
                options.put("amount", amount * 100);
                //checkout.open(AddMoneyActivity.this, options);
            } catch (Exception e) {
                Log.e("tag", "Error in starting CashFree Checkout", e);
            }


            GenerateTokenPojo generateTokenPojo = new GenerateTokenPojo();
            generateTokenPojo.setOrderId(transId);
            generateTokenPojo.setOrderAmount(String.valueOf(amount));
            generateTokenPojo.setOrderCurrency("INR");


            progressDialog = ProgressDialog.show(context, "Loading ....", "Please wait", true, false);
            Api infinityService = RetrofitClient.getInstance("https://api.cashfree.com/").create(Api.class);
            Call<GenerateTokenPojo> call = infinityService.getTokenFromServer(generateTokenPojo);
            call.enqueue(serverRequestCall);

        });

        backBtn.setOnClickListener(v -> {
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

    public Callback<GenerateTokenPojo> serverRequestCall = new Callback<GenerateTokenPojo>() {
        @Override
        public void onResponse(Call<GenerateTokenPojo> call, Response<GenerateTokenPojo> response) {


            progressDialog.dismiss();
            if (!response.isSuccessful()) {
                int responseCode = response.code();
                if (responseCode == 504) { // 504 Unsatisfiable Request (only-if-cached)

                    Toast.makeText(context, "Error Please Try Again..", Toast.LENGTH_SHORT).show();
                }
                return;
            } else {
                GenerateTokenPojo generateTokenPojo = response.body();
                CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
                cfPaymentService.setOrientation(0);


                params.put(PARAM_APP_ID, appId);
                params.put(PARAM_ORDER_ID, transId);
                params.put(PARAM_ORDER_AMOUNT, String.valueOf(amount));
                params.put(PARAM_ORDER_NOTE, orderNote);
                params.put(PARAM_CUSTOMER_NAME, customerName);
                params.put(PARAM_CUSTOMER_PHONE, customerPhone);
                params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
                params.put(PARAM_ORDER_CURRENCY, "INR");

                cfPaymentService.doPayment(AddMoneyActivity.this, params, generateTokenPojo.getCftoken(), stage, "#274474", "#4B79a8", false);

            }

        }

        @Override
        public void onFailure(retrofit2.Call call, Throwable t) {
            NetworkLogUtility.logFailure(call, t);
            progressDialog.dismiss();

            if (!call.isCanceled()) {
                if (NetworkUtility.isKnownException(t)) {

                    Toast.makeText(context, "Can't load data.\nCheck your network connection.", Toast.LENGTH_SHORT).show();

                }
            }

        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();

        if (bundle.getString("txStatus").matches("SUCCESS")) {      //On payment SUCCESS
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(FirebaseAuth.getInstance().getUid()).child("mainBalance");
            databaseReference.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    if (mutableData.getValue() != null) {
                        float walletBal = mutableData.getValue(float.class);
                        newWalletBal = walletBal + amount;
                        mutableData.setValue(newWalletBal);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    GenericMethod.showMessage(AddMoneyActivity.this, "Money is successfully added to your account.");
                    final DatabaseReference TransactionRef = FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid());
                    final com.commercial.tuds.earnandpay.Models.Transaction transaction = new com.commercial.tuds.earnandpay.Models.Transaction("Add Money", amount, true, "credited", "BucksBin", transId, "", transTime, "Add Money", newWalletBal);
                    TransactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<com.commercial.tuds.earnandpay.Models.Transaction> transactionArrayList = new ArrayList<>();
                            transactionArrayList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                com.commercial.tuds.earnandpay.Models.Transaction transaction1 = snapshot.getValue(com.commercial.tuds.earnandpay.Models.Transaction.class);
                                if (!transaction1.getTransactionReferenceNumber().matches(transId))
                                    transactionArrayList.add(transaction1);
                            }
                            transactionArrayList.add(transaction);
                            TransactionRef.setValue(transactionArrayList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        } else {                            //On payment CANCELED/FAILED
            if (bundle.getString("txStatus").matches("CANCELLED")) {
                Toast.makeText(this, "Payment CANCELED", Toast.LENGTH_SHORT).show();
            } else {
                GenericMethod.showMessage(AddMoneyActivity.this, "The Payment is failed due to some technical issue.");

                Snackbar.make(parentLayout, "The payment is failed.In case money has been deducted from your acocunt,It will be returned soon.", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
            final DatabaseReference TransactionRef = FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid());
            final com.commercial.tuds.earnandpay.Models.Transaction transaction = new com.commercial.tuds.earnandpay.Models.Transaction("Add Money", amount, false, "credited", "BucksBin", transId, "", transTime, "Add Money", newWalletBal);
            TransactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<com.commercial.tuds.earnandpay.Models.Transaction> transactionArrayList = new ArrayList<>();
                    transactionArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        com.commercial.tuds.earnandpay.Models.Transaction transaction1 = snapshot.getValue(com.commercial.tuds.earnandpay.Models.Transaction.class);
                        if (!transaction1.getTransactionReferenceNumber().matches(transId))
                            transactionArrayList.add(transaction1);
                    }
                    transactionArrayList.add(transaction);
                    TransactionRef.setValue(transactionArrayList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
