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

import com.commercial.tuds.earnandpay.Adapter.NotificationRecyclerAdapter;
import com.commercial.tuds.earnandpay.Models.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private ArrayList<Notification> NotificationList;
    AVLoadingIndicatorView loadingView;
    NestedScrollView scrollView;
    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        loadingView = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.recyclerview);
        backIcon = findViewById(R.id.back_icon);
        scrollView = findViewById(R.id.scrollView);
        scrollView.setAlpha(0.3f);
        loadingView.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid()).child("notificationList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NotificationList = new ArrayList<>();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    Notification value = dataSnapshot1.getValue(Notification.class);
                    String notification_title = value.getNotificationTitle();
                    String notification_desc = value.getNotificationMessage();
                    Notification notification = new Notification(notification_title,notification_desc);
                    NotificationList.add(notification);
                }
                Log.e("tag",NotificationList.toString());
                loadingView.hide();
                scrollView.setAlpha(1f);
                NotificationRecyclerAdapter adapter = new NotificationRecyclerAdapter(NotificationList,NotificationActivity.this);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(NotificationActivity.this,1);
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
