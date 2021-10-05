package com.commercial.tuds.earnandpay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.UserDetails;
import com.commercial.tuds.earnandpay.Utils.DecimalFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvestMoney extends AppCompatActivity {
    TextView mainBalance, fixedBalance;
    RelativeLayout addBtn, withdrawBtn;
    AVLoadingIndicatorView mainBalanceLoader, fixedBalanceLoader;
    TextInputEditText amountEt;
    private String TAG = "InvestMoneyActivity";
    private DatabaseReference databaseReference;
    private AlertDialog dialog;
    ImageView backIcon;
    String URl = "http://worldtimeapi.org/api/timezone/Asia/Kolkata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_money);
        mainBalance = findViewById(R.id.availableBalance);
        fixedBalance = findViewById(R.id.fixedBalance);
        addBtn = findViewById(R.id.addMoneyBtn);
        withdrawBtn = findViewById(R.id.withdrawMoneyBtn);
        mainBalanceLoader = findViewById(R.id.loader);
        fixedBalanceLoader = findViewById(R.id.loader1);
        amountEt = findViewById(R.id.amountET);
        mainBalanceLoader.show();
        fixedBalanceLoader.show();
        mainBalanceLoader.setVisibility(View.VISIBLE);
        fixedBalanceLoader.setVisibility(View.VISIBLE);
        backIcon = findViewById(R.id.back_icon);

        amountEt.setFilters(new InputFilter[]{new DecimalFilter(6, 2)});

        //Setting main Balance and fixed Balance

        final String myUid = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(myUid).child("mainBalance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mainBalanceLoader.setVisibility(View.INVISIBLE);
                String balance = new DecimalFormat("##.##").format(dataSnapshot.getValue(float.class));
                mainBalance.setText("₹ " + balance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid()).child("fixedBalance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fixedBalanceLoader.setVisibility(View.INVISIBLE);
                String balance = new DecimalFormat("##.##").format(dataSnapshot.getValue(float.class));
                fixedBalance.setText("₹ " + balance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });

        addBtn.setOnClickListener(v -> {
            String amount = amountEt.getText().toString();
            if (amount.matches("")) {
                amountEt.setError("Please enter a valid amount");
            } else {
                final float amountValue = Float.parseFloat(amount);
                if (amountValue > 250) {
                    GenericMethod.showMessage(InvestMoney.this, "You could not invest more than 100 rs.");
                } else {
                    amountEt.setText("");
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(myUid).child("mainBalance");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final float currentMainBalance = dataSnapshot.getValue(float.class);
                            final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid());
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                                    float currentFixedBalance = userDetails.getFixedBalance();
                                    if (amountValue <= currentMainBalance) {
                                        if (currentFixedBalance < 2500) {
                                            StringRequest stringRequest = new StringRequest(Request.Method.GET, URl,
                                                    response -> {
                                                        try {
                                                            JSONObject obj = new JSONObject(response);
                                                            String rawDate = obj.getString("datetime").substring(0, 10);
                                                            String date = rawDate.substring(8, 10) + "/" + rawDate.substring(5, 7) + "/" + rawDate.substring(0, 4);
                                                            float fixedAmountToBeInvested = amountValue * 10;
                                                            databaseReference.setValue(currentMainBalance - amountValue);
                                                            userDetails.setFixedBalance(Math.min(currentFixedBalance + fixedAmountToBeInvested, 2500f));
                                                            userDetails.setFixedBalanceDate(date);
                                                            databaseReference1.setValue(userDetails);
                                                            GenericMethod.showMessage(InvestMoney.this, "You have succesfully invested Rs." + amountValue + " into your fixed wallet.");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    },
                                                    error -> {
                                                        Toast.makeText(InvestMoney.this, "This service is currently unavailable. Please try later.", Toast.LENGTH_SHORT).show();
                                                        Log.e("tag", error.toString());
                                                    });
                                            try {
                                                RequestQueue requestQueue = Volley.newRequestQueue(InvestMoney.this);
                                                requestQueue.add(stringRequest);
                                            } catch (Exception e) {
                                                Toast.makeText(InvestMoney.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else
                                            GenericMethod.showMessage(InvestMoney.this, "You have already invested the maximum amount possible.");
                                    } else
                                        GenericMethod.showMessage(InvestMoney.this, "You have insufficient balance in your wallet.");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e(TAG, databaseError.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        withdrawBtn.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(InvestMoney.this);
            builder.setTitle("Withdraw Money");
            builder.setMessage("Are you sure you want to withdraw all money to your wallet?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(myUid).child("mainBalance");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final float currentMainBalance = dataSnapshot.getValue(float.class);
                        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid());
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                StringRequest stringRequest = new StringRequest(Request.Method.GET, URl,
                                        response -> {
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                                                String rawDate = obj.getString("datetime").substring(0, 10);
                                                String date = rawDate.substring(8, 10) + "/" + rawDate.substring(5, 7) + "/" + rawDate.substring(0, 4);
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                                                Date fixedBalanceDate, currentdate = null;
                                                try {
                                                    currentdate = dateFormat.parse(date);
                                                    fixedBalanceDate = dateFormat.parse(userDetails.getFixedBalanceDate());

                                                } catch (ParseException e) {
                                                    Log.e("tag", e.getLocalizedMessage());
                                                    e.printStackTrace();
                                                    fixedBalanceDate = currentdate;
                                                }
                                                float fixedBalanceValue;
                                                int diff = GenericMethod.calculateDateDifference(currentdate, fixedBalanceDate);
                                                Log.e(TAG, String.valueOf(diff));
                                                if (diff >= 5) {
                                                    fixedBalanceValue = userDetails.getFixedBalance();
                                                    databaseReference.setValue(currentMainBalance + fixedBalanceValue);
                                                    userDetails.setFixedBalance(0f);
                                                    databaseReference1.setValue(userDetails);
                                                    GenericMethod.showMessage(InvestMoney.this, "You have successfully withdraw all the money from fixed wallet.");
                                                } else {
                                                    Toast.makeText(InvestMoney.this, "you have invested money on " + dateFormat.format(fixedBalanceDate) + " .Hence you can not withdraw it before 5 years.", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        error -> {
                                            Toast.makeText(InvestMoney.this, "This service can not be used now.", Toast.LENGTH_SHORT).show();
                                            Log.e("tag", error.toString());
                                        });
                                try {
                                    RequestQueue requestQueue = Volley.newRequestQueue(InvestMoney.this);
                                    requestQueue.add(stringRequest);
                                } catch (Exception e) {
                                    Toast.makeText(InvestMoney.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog1) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            });
            dialog.show();
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
