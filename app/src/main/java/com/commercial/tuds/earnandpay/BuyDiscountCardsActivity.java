package com.commercial.tuds.earnandpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.Adapter.BuyDiscountCardRecyclerAdapter;
import com.commercial.tuds.earnandpay.Models.BuyDiscountCardPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class BuyDiscountCardsActivity extends AppCompatActivity {

    int number_of_cards = 0;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private ArrayList<BuyDiscountCardPojo> BDCList;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    ImageView backIcon;
    TextView toolbarTitleTV, NumberOfCardsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_discount_cards);

        loadingView = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerview);
        backIcon = findViewById(R.id.back_icon);
        scrollView = findViewById(R.id.scrollView);
        toolbarTitleTV = findViewById(R.id.toolbarTitleTV);
        NumberOfCardsTV = findViewById(R.id.NumberOfCardsTV);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        toolbarTitleTV.setText("Offers");
        scrollView.setAlpha(0f);
        loadingView.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(currentFirebaseUser.getUid()).child("numberOfCards");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.d("msg","########"+dataSnapshot+"####"+dataSnapshot.getValue());
                    if (dataSnapshot.getValue(Integer.class) == -1) {
                        NumberOfCardsTV.setText("∞");

                    } else {
                        number_of_cards = dataSnapshot.getValue(Integer.class);
                        NumberOfCardsTV.setText(Integer.toString(dataSnapshot.getValue(Integer.class)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("discount_card_offers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BDCList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    BuyDiscountCardPojo pojo = data.getValue(BuyDiscountCardPojo.class);
                    BDCList.add(pojo);
                }
                scrollView.setAlpha(1f);
                BuyDiscountCardRecyclerAdapter adapter = new BuyDiscountCardRecyclerAdapter(BDCList, BuyDiscountCardsActivity.this);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(BuyDiscountCardsActivity.this, 1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        loadingView.hide();
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