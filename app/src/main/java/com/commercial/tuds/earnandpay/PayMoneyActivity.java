package com.commercial.tuds.earnandpay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.Transaction;
import com.commercial.tuds.earnandpay.Utils.DecimalFilter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PayMoneyActivity extends AppCompatActivity {
    ImageView backBtn;
    TextInputEditText amountET, mobileEt, remarksEt;
    RelativeLayout submitBtn;
    RelativeLayout payBtn;
    float amount;
    String mobile, remarks, transId;
    AVLoadingIndicatorView loader;
    View subView;
    ImageView resultImage;
    TextView resultStatus, okayBtnText, resultDetail;
    RelativeLayout mainLayout;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    NestedScrollView scrollView;
    private String ToPersonUID;
    String toUID;
    private final int PICK_CONTACT = 103;
    private float closingBalance;
    private String TAG = "PayMoneyActivity";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_money);

        amountET = findViewById(R.id.amountET);
        mobileEt = findViewById(R.id.mobileET);
        remarksEt = findViewById(R.id.remarksET);
        backBtn = findViewById(R.id.back_icon);
        payBtn = findViewById(R.id.payNowBtn);
        subView = getLayoutInflater().inflate(R.layout.action_result_card, null);
        resultImage = subView.findViewById(R.id.resultIcon);
        resultStatus = subView.findViewById(R.id.resultStatus);
        okayBtnText = subView.findViewById(R.id.okayBtnText);
        resultDetail = subView.findViewById(R.id.resultDetail);
        submitBtn = subView.findViewById(R.id.okayBtn);
        loader = subView.findViewById(R.id.loaderAnimation);
        final AVLoadingIndicatorView loaderView = findViewById(R.id.loader);
        mainLayout = subView.findViewById(R.id.mainLayout);
        scrollView = findViewById(R.id.scrollView);
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        builder = new AlertDialog.Builder(PayMoneyActivity.this);
        builder.setView(subView);
        dialog = builder.create();
        amountET.setFilters(new InputFilter[]{new DecimalFilter(6, 2)});


        mobileEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && mobileEt.getText().toString().length() == 10) {
                    mobile = mobileEt.getText().toString();
                    scrollView.setVisibility(View.INVISIBLE);
                    loaderView.show();
                    loaderView.setVisibility(View.VISIBLE);
                    DatabaseReference metadataRef = FirebaseDatabase.getInstance().getReference("meta_data/phoneNumbers").child(mobile);
                    metadataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            toUID = dataSnapshot.getValue(String.class);
                            loaderView.hide();
                            loaderView.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                            if (toUID == null || toUID.matches("")) {
                                GenericMethod.showMessage(PayMoneyActivity.this, "This number is not on BucksBin yet.");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("tag", "database error");
                            loaderView.setVisibility(View.INVISIBLE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });


        mobileEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (mobileEt.getRight() - mobileEt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        GenericMethod.showMessage(PayMoneyActivity.this, "Clicked");
                        Dexter.withActivity(PayMoneyActivity.this)
                                .withPermission(Manifest.permission.READ_CONTACTS)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                        startActivityForResult(intent, PICK_CONTACT);
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                })
                                .onSameThread()
                                .check();
                        return true;
                    }
                }
                return false;
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setAnimation(GenericMethod.buttonClickAnimation);
                if (amountET.getText() != null && !amountET.getText().toString().matches(""))
                    amount = Integer.parseInt(amountET.getText().toString());
                else
                    amount = 0;
                mobile = mobileEt.getText().toString();
                remarks = remarksEt.getText().toString();
                if (validateValues(mobile, amount)) {
                    if (toUID == null || toUID.matches("")) {
                        GenericMethod.showMessage(PayMoneyActivity.this, "This number is not on BucksBin yet.");
                        return;
                    }
                    dialog.show();
                    loader.setVisibility(View.VISIBLE);
                    mainLayout.setVisibility(View.INVISIBLE);
                    DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
                    dateFormatter.setLenient(false);
                    Date today = new Date();
                    final String transactionTime = dateFormatter.format(today);
                    int time = (int) new Date().getTime();
                    String timeCopy = String.valueOf(time).replace("-", "");
                    Log.e("time", timeCopy);
                    transId = FirebaseAuth.getInstance().getUid().substring(0, 5) + timeCopy;
                    transId = transId.substring(0, 10);
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mainBalance");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            float walletBalance = dataSnapshot.getValue(float.class);
                            if (amount > walletBalance) {
                                loader.setVisibility(View.INVISIBLE);
                                mainLayout.setVisibility(View.VISIBLE);
                                resultImage.setVisibility(View.VISIBLE);
                                resultImage.setImageDrawable(getResources().getDrawable(R.drawable.low_balance_icon));
                                resultStatus.setText("Low Balance");
                                resultDetail.setText("You have only Rs" + walletBalance + " in your wallet. Please add some money.");
                                okayBtnText.setText("Load Wallet");
                                submitBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        v.setAnimation(GenericMethod.buttonClickAnimation);
                                        startActivity(new Intent(PayMoneyActivity.this, AddMoneyActivity.class));
                                    }
                                });
                            } else {
                                databaseReference.runTransaction(new com.google.firebase.database.Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public com.google.firebase.database.Transaction.Result doTransaction(@NonNull final MutableData mutableData) {
                                        if (mutableData.getValue() != null) {
                                            final Transaction fromUserTransaction = new Transaction("Money Transfer", amount, true, "debited", "+91"+mobile, transId, "", transactionTime, remarks, 0f);
                                            final Transaction toUserTransaction = new Transaction("Money Transfer", amount, true, "credited", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), transId, "", transactionTime, remarks, 0f);
                                            final float mainBalance = mutableData.getValue(float.class);
                                            mutableData.setValue(mainBalance - amount);
                                            DatabaseReference toPersonRef = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(toUID).child("mainBalance");
                                            toPersonRef.runTransaction(new com.google.firebase.database.Transaction.Handler() {
                                                float currentBalance;

                                                @NonNull
                                                @Override
                                                public com.google.firebase.database.Transaction.Result doTransaction(@NonNull MutableData mutableData1) {
                                                    if (mutableData1.getValue() != null) {
                                                        currentBalance = mutableData1.getValue(float.class);
                                                        mutableData1.setValue(currentBalance + amount);
                                                    }
                                                    return com.google.firebase.database.Transaction.success(mutableData1);
                                                }
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, boolean commited, @Nullable DataSnapshot dataSnapshot) {
                                                    if (commited) {
                                                        fromUserTransaction.setClosingBalance(mainBalance - amount);
                                                        toUserTransaction.setClosingBalance(currentBalance + amount);
                                                        final DatabaseReference toTransactionRef = FirebaseDatabase.getInstance().getReference().child("transactions").child(toUID);
                                                        toTransactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                ArrayList<Transaction> transactionArrayList = new ArrayList<>();
                                                                transactionArrayList.clear();
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    Transaction transaction1 = snapshot.getValue(Transaction.class);
                                                                    if (!transaction1.getTransactionReferenceNumber().matches(transId))
                                                                        transactionArrayList.add(transaction1);
                                                                }
                                                                transactionArrayList.add(toUserTransaction);
                                                                toTransactionRef.setValue(transactionArrayList);
                                                                mainLayout.setVisibility(View.VISIBLE);
                                                                loader.setVisibility(View.INVISIBLE);
                                                                resultImage.setVisibility(View.VISIBLE);
                                                                resultImage.setImageDrawable(getResources().getDrawable(R.drawable.check_icon));
                                                                resultStatus.setText("Money Transfered");
                                                                okayBtnText.setText("Okay");
                                                                resultDetail.setText("Rs " + amount + " has been transfered to +91-" + mobile + " successfully.");
                                                                submitBtn.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        if (dialog != null)
                                                                            dialog.dismiss();
                                                                        finish();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                        //success
                                                    } else {
                                                        mutableData.setValue(mainBalance);
                                                        fromUserTransaction.setClosingBalance(mainBalance);
                                                        fromUserTransaction.setIsSuccessful(false);
                                                        toUserTransaction.setIsSuccessful(false);
                                                        mainLayout.setVisibility(View.VISIBLE);
                                                        loader.setVisibility(View.INVISIBLE);
                                                        resultImage.setVisibility(View.VISIBLE);
                                                        resultImage.setImageDrawable(getResources().getDrawable(R.drawable.failed_icon));
                                                        resultStatus.setText("Failed");
                                                        okayBtnText.setText("Okay");
                                                        resultDetail.setText("In case amount has been deducted please call our support team.");
                                                        submitBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (dialog != null)
                                                                    dialog.dismiss();
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                    final DatabaseReference fromTransactionRef = FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid());
                                                    fromTransactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            ArrayList<Transaction> transactionArrayList = new ArrayList<>();
                                                            transactionArrayList.clear();
                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                Transaction transaction1 = snapshot.getValue(Transaction.class);
                                                                if (!transaction1.getTransactionReferenceNumber().matches(transId))
                                                                    transactionArrayList.add(transaction1);
                                                            }
                                                            transactionArrayList.add(fromUserTransaction);
                                                            fromTransactionRef.setValue(transactionArrayList);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            });
                                        }
                                        return com.google.firebase.database.Transaction.success(mutableData);

                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                        if (b) {
                                            //succeess
                                        } else {
                                            Log.e("PayMoneyActivity", databaseError.getMessage());
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        //Final tasks
        backBtn.setOnClickListener(v -> {
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

    public boolean validateValues(String mobile, float amount) {
        if (mobile == null || mobile.matches("") || mobile.length() != 10)
            mobileEt.setError("Enter a valid mobile number");
        else if (amount == 0)
            amountET.setError("Enter a valid amount.");
        else
            return true;
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            if (cNumber.matches(""))
                                GenericMethod.showMessage(PayMoneyActivity.this, "Unable to fetch number.Prefer typing it manually");
                            cNumber = cNumber.replace(" ", "").replace("-", "");
                            if (cNumber.length() != 10)
                                try {
                                    cNumber = cNumber.substring(3, 13);
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            mobileEt.setText(cNumber);
                        }
                    }
                }
                break;
        }
    }
}
