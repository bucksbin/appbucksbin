package com.commercial.tuds.earnandpay;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.commercial.tuds.earnandpay.Adapter.FaqRecyclerAdapter;
import com.commercial.tuds.earnandpay.Models.FAQ;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class FAQActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private ArrayList<FAQ> FaqList;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        loadingView = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerview);
        backIcon = findViewById(R.id.back_icon);
        scrollView = findViewById(R.id.scrollView);
        scrollView.setAlpha(0f);
        loadingView.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("faq");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FaqList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    FAQ faq = dataSnapshot1.getValue(FAQ.class);
                    FaqList.add(faq);
                }
                Log.d("msg","###"+FaqList.toString());
                loadingView.hide();
                scrollView.setAlpha(1f);
                FaqRecyclerAdapter adapter = new FaqRecyclerAdapter(FaqList,FAQActivity.this);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(FAQActivity.this,1);
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
