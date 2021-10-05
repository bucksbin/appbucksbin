package com.commercial.tuds.earnandpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;


public class SplashActivity extends AppCompatActivity {
    static String LoginPreference = "LoginPreference";
    public static SharedPreferences loginPreferences;
    private Boolean LoginStatus;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

//        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.admob_app_id));
        if (!isNetworkAvailable())
            Toast.makeText(SplashActivity.this, "No Internet Available", Toast.LENGTH_SHORT).show();
        else {



            loginPreferences = getSharedPreferences(LoginPreference, MODE_PRIVATE);
            LoginStatus = loginPreferences.getBoolean("isLoggedIn", false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (LoginStatus) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 2000);
        }

//        FirebaseApp.initializeApp(context);
//        Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());

//        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//            String token;
//            if (!task.isSuccessful()) {
//                token = task.getException().getMessage();
//                Log.w("FCM TOKEN Failed", task.getException());
//            } else {
//                token = task.getResult().getToken();
//                Log.i("FCM TOKEN", token);
//            }
//        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

