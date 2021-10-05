package com.commercial.tuds.earnandpay;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import me.relex.circleindicator.CircleIndicator;

public class TransactionResultActivity extends AppCompatActivity {
    TextView TransactionStatusTV,TransactionAmount,TransactionContact,TransactionID,TransactionDate,mainBalance,remarksTv,ClosingBalance,toText;
    ImageView icon;
    AVLoadingIndicatorView loader;
    ImageView backIcon;
    ViewPager mPager;
    CircleIndicator circleIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_result);

        TransactionStatusTV = findViewById(R.id.transactionStatus);
        TransactionAmount = findViewById(R.id.transactionAmount);
        TransactionContact = findViewById(R.id.transactionContact);
        TransactionID = findViewById(R.id.transactionID);
        TransactionDate = findViewById(R.id.date);
        ClosingBalance = findViewById(R.id.closingBalance);
        icon = findViewById(R.id.icon);
        mainBalance = findViewById(R.id.mainBalance);
        loader = findViewById(R.id.loaderAnimation);
        backIcon = findViewById(R.id.back_icon);
        remarksTv =findViewById(R.id.remarks);
        mPager = findViewById(R.id.pager);
        toText =findViewById(R.id.toText);
        circleIndicator = findViewById(R.id.indicator);
        GenericMethod.showOfferPhotos(TransactionResultActivity.this,mPager,circleIndicator);

        loader.show();
        loader.setVisibility(View.VISIBLE);
        mainBalance.setVisibility(View.INVISIBLE);

        Transaction transaction = (Transaction) getIntent().getSerializableExtra("transaction");

        if(transaction.getIsSuccessful()) {
            TransactionStatusTV.setText(transaction.getTransactionType());
            icon.setImageDrawable(getResources().getDrawable(R.drawable.successful_icon));
        }
        else if(transaction.getTransactionType().matches(getResources().getString(R.string.pending))) {
            TransactionStatusTV.setText("Pending");
            icon.setImageDrawable(getResources().getDrawable(R.drawable.result_pending_icon));
        }
        else {
            TransactionStatusTV.setText("Transaction Failed");
            icon.setImageDrawable(getResources().getDrawable(R.drawable.result_failed_icon));
        }
        if(transaction.getTransactionAction().matches("credited"))
            toText.setText("From");
        else
            toText.setText("To");
        TransactionAmount.setText("₹ " + transaction.getTransactionAmount());
        TransactionContact.setText(transaction.getAccountNumber());
        TransactionID.setText(transaction.getTransactionReferenceNumber()+"-"+transaction.getBankReferenceNumber());
        TransactionDate.setText(transaction.getTransactionTime());
        ClosingBalance.setText("Closing Balance: ₹"+transaction.getClosingBalance());
        if(transaction.getTransactionRemark()!= null && !transaction.getTransactionRemark().matches(""))
            remarksTv.setText("Remarks: "+transaction.getTransactionRemark());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mainBalance");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                float walletBalance = dataSnapshot.getValue(Float.class);
                mainBalance.setText("₹ "+walletBalance);
                loader.setVisibility(View.INVISIBLE);
                mainBalance.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        backIcon.setOnClickListener(v -> {
            startActivity(new Intent(this,TransactionHistoryActivity.class));
            finishAffinity();
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,TransactionHistoryActivity.class));
        finishAffinity();
    }

}
