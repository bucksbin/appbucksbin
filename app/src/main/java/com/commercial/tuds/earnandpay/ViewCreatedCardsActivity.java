package com.commercial.tuds.earnandpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.Adapter.ViewCreatedCardsRecyclerAdapter;
import com.commercial.tuds.earnandpay.Models.DiscountCardPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;

public class ViewCreatedCardsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private ArrayList<DiscountCardPojo> DCList;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    ImageView backIcon;
    TextView toolbarTitleTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_created_cards);
        loadingView = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerview);
        backIcon = findViewById(R.id.back_icon);
        scrollView = findViewById(R.id.scrollView);
        toolbarTitleTV = findViewById(R.id.toolbarTitle);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        toolbarTitleTV.setText("Created Cards");
        scrollView.setAlpha(0f);
        loadingView.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("discount_card").child(currentFirebaseUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DCList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    DiscountCardPojo pojo = dataSnapshot1.getValue(DiscountCardPojo.class);
                    DCList.add(pojo);
                }
                loadingView.hide();
                scrollView.setAlpha(1f);
                Collections.reverse(DCList);
                ViewCreatedCardsRecyclerAdapter adapter = new ViewCreatedCardsRecyclerAdapter(DCList,ViewCreatedCardsActivity.this);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ViewCreatedCardsActivity.this,1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator( new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
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