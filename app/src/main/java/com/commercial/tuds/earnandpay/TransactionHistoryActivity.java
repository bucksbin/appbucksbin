package com.commercial.tuds.earnandpay;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.Adapter.TransactionHistoryRecyclerAdapter;
import com.commercial.tuds.earnandpay.Models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;

public class TransactionHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private ArrayList<Transaction> TransactionList;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    ImageView backIcon;
    TextView toolbarTitleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        
        loadingView = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerview);
        backIcon = findViewById(R.id.back_icon);
        scrollView = findViewById(R.id.scrollView);
        toolbarTitleTV = findViewById(R.id.toolbarTitle);
        scrollView.setAlpha(0f);
        loadingView.show();
        toolbarTitleTV.setText("Transaction History");
        
        databaseReference = FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TransactionList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    Transaction transaction = dataSnapshot1.getValue(Transaction.class);
                    TransactionList.add(transaction);
                }
                loadingView.hide();
                scrollView.setAlpha(1f);
                Collections.reverse(TransactionList);
                TransactionHistoryRecyclerAdapter adapter = new TransactionHistoryRecyclerAdapter(TransactionList,TransactionHistoryActivity.this);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TransactionHistoryActivity.this,1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator( new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
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
