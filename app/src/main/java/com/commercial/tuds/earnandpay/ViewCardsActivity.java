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

import com.commercial.tuds.earnandpay.Adapter.ViewCardsRecyclerAdapter;
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

public class ViewCardsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private ArrayList<DiscountCardPojo> DCList;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    ImageView backIcon;
    String strDestination;
    TextView toolbarTitleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cards);

        loadingView = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerview);
        backIcon = findViewById(R.id.back_icon);
        scrollView = findViewById(R.id.scrollView);
        toolbarTitleTV = findViewById(R.id.toolbarTitle);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        toolbarTitleTV.setText("View Cards");

        scrollView.setAlpha(0f);
        loadingView.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("discount_card");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DCList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    for (DataSnapshot ds1 : data.getChildren()) {

                        if (ds1.child("destination_id").getValue().equals(currentFirebaseUser.getUid())) {

                            DiscountCardPojo pojo = ds1.getValue(DiscountCardPojo.class);
                            DCList.add(pojo);
                        }
                    }
                }

                loadingView.hide();
                scrollView.setAlpha(1f);
                ViewCardsRecyclerAdapter adapter = new ViewCardsRecyclerAdapter(DCList, ViewCardsActivity.this);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ViewCardsActivity.this, 1);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
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