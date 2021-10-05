package com.commercial.tuds.earnandpay;

import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.BankTransfer;
import com.commercial.tuds.earnandpay.Models.Transaction;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BankTransferActivity extends AppCompatActivity {
    ImageView backIcon;
    TextInputEditText accountNumberEt,ifscCodeEt,accountNameEt,amountEt;
    String accountNumber,accountName,ifscCode;
    float amount;
    RelativeLayout transferBtn;
    private DatabaseReference databaseReference;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);
        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        accountNumberEt = findViewById(R.id.accountNumberET);
        accountNameEt = findViewById(R.id.accountHolderET);
        ifscCodeEt = findViewById(R.id.IFSCCodeET);
        amountEt = findViewById(R.id.amountET);
        transferBtn = findViewById(R.id.transferAmountBtn);

        transferBtn.setOnClickListener(v ->
        {
            amountEt.clearFocus();
            if(accountNumberEt.getText() == null || accountNameEt.getText() == null || ifscCodeEt.getText() == null || amountEt.getText() == null)
            {
                GenericMethod.showMessage(this,"All fields are mandatory.");
                return;

            }
            accountNumber = accountNumberEt.getText().toString();
            accountName = accountNameEt.getText().toString();
            ifscCode = ifscCodeEt.getText().toString();
            amount = Float.parseFloat(amountEt.getText().toString());
            final AlertDialog.Builder builder = new AlertDialog.Builder(BankTransferActivity.this);
            builder.setTitle("Transfer Money");
            builder.setMessage("Clicking on Yes will automatically deduct Rs" + amount + "+(Rs10 transaction charges) from your wallet. Are you sure you want to transfer the money?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                amount +=10;
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(FirebaseAuth.getInstance().getUid()).child("mainBalance");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        float walletBal = dataSnapshot1.getValue(float.class);
                        if (walletBal < amount) {
                            Snackbar.make(findViewById(R.id.mainLayout), "Your wallet balance is not sufficient. Please recharge before proceeding.", Snackbar.LENGTH_LONG)
                                    .setAction("Recharge", view -> startActivity(new Intent(BankTransferActivity.this, AddMoneyActivity.class)))
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                    .show();
                        } else {
                            databaseReference.setValue(walletBal - amount);
                            BankTransfer bankTransfer = new BankTransfer(accountNumber, accountName, ifscCode, amount);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("bank_transfers").child(FirebaseAuth.getInstance().getUid()+(new Date()).getTime());
                            String key = databaseReference.getKey();
                            bankTransfer.setBankTransferID(key);
                            databaseReference.setValue(bankTransfer);
                            GenericMethod.showMessage(BankTransferActivity.this, "Your bank transfer request has been submitted successfully.The money in the another account will be credited in next 24 hours. ");
                            final DatabaseReference TransactionRef = FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid());
                            DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
                            dateFormatter.setLenient(false);
                            Date today = new Date();
                            String transTime = dateFormatter.format(today);
                            final Transaction transaction = new Transaction("Bank Transfer", amount, true, "debited", accountNumber, key.substring(20), "", transTime, "Bank Transfer", walletBal - amount);
                            TransactionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    ArrayList<Transaction> transactionArrayList = new ArrayList<>();
                                    transactionArrayList.clear();
                                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                                        Transaction transaction1 = snapshot.getValue(Transaction.class);
                                        transactionArrayList.add(transaction1);
                                    }
                                    transactionArrayList.add(transaction);
                                    TransactionRef.setValue(transactionArrayList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                if (dialog != null)
                    dialog.dismiss();
            });
            dialog = builder.create();
            dialog.setOnShowListener(dialog1 -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            });
            dialog.show();
        });

        backIcon = findViewById(R.id.back_icon);
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
