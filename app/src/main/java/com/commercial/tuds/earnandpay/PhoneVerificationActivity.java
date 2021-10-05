package com.commercial.tuds.earnandpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.Address;
import com.commercial.tuds.earnandpay.Models.Notification;
import com.commercial.tuds.earnandpay.Models.UserBasicDetails;
import com.commercial.tuds.earnandpay.Models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mukesh.OtpView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {
    String TAG = "phoneVerificationActivity";
    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    RelativeLayout verifyBtn;
    LinearLayout resendOTPLayout;
    OtpView otpView;
    TextView timer, resendOTPLink;
    CountDownTimer countDownTimer;
    private String mVerificationId, mobile;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        FirebaseApp.initializeApp(this);
        otpView = findViewById(R.id.otp_view);
        verifyBtn = findViewById(R.id.verifyBtn);
        resendOTPLayout = findViewById(R.id.resendOTPLayout);
        resendOTPLink = findViewById(R.id.resendOTPLink);
        timer = findViewById(R.id.timer);

        Intent intent = getIntent();
        mobile = intent.getStringExtra("phone");
        sendVerificationCode(mobile);

        verifyBtn.setOnClickListener(v -> {
            String enteredCode = otpView.getText().toString().trim();
            verifyVerificationCode(enteredCode.trim());
        });
        resendOTPLink.setOnClickListener(v -> resendVerificationCode(mobile));

    }

    private void sendVerificationCode(String mobile) {
        mobile = "+91" + mobile;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String mobile) {
        mobile = "+91" + mobile;
        if (mResendToken == null) {
            Toast.makeText(PhoneVerificationActivity.this, "Unable to resend OTP.Please go back and try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpView.setText(code);
                countDownTimer.cancel();
                timer.setText("Verifying OTP...");
                verifyVerificationCode(code);
                verifyBtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
            resendOTPLayout.setVisibility(View.INVISIBLE);
            timer.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    timer.setText(String.valueOf("00:" + millisUntilFinished / 1000));
                }

                public void onFinish() {
                    timer.setVisibility(View.INVISIBLE);
                    resendOTPLayout.setVisibility(View.VISIBLE);
                }
            };
            countDownTimer.start();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            super.onCodeAutoRetrievalTimeOut(s);
        }
    };

    private void verifyVerificationCode(String otp) {
        if (mVerificationId == null || mVerificationId.matches("")) {
            Toast.makeText(PhoneVerificationActivity.this, "Unable to send OTP. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(PhoneVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences loginPreferences = getSharedPreferences(SplashActivity.LoginPreference, MODE_PRIVATE);
                            SharedPreferences.Editor editor = loginPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.commit();
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists() || dataSnapshot.getValue() == null) {
                                        Notification notification = new Notification("Welcome to BucksBin!", "The new way of payment.");
                                        ArrayList<Notification> notificationArrayList = new ArrayList<>();
                                        notificationArrayList.add(notification);
                                        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
                                        dateFormatter.setLenient(false);
                                        Date today = new Date();
                                        String time = dateFormatter.format(today);
                                        UserDetails userDetails = new UserDetails("", "", mobile, true, new Address(), "", notificationArrayList, 0.0f, time, time, 0.0f, false, 0.0f, 0.0f, 0.0f, "false", 0);
                                        databaseReference.setValue(userDetails);
                                        FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mainBalance").setValue(0f);
                                        FirebaseDatabase.getInstance().getReference().child("meta_data").child("phoneNumbers").child(mobile).setValue(FirebaseAuth.getInstance().getUid());
                                        FirebaseDatabase.getInstance().getReference().child("meta_data").child("user").child(mobile).setValue(new UserBasicDetails("N/A", mobile, FirebaseAuth.getInstance().getCurrentUser().getUid()));
                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                .addOnCompleteListener(task1 -> {
                                                    if (!task1.isSuccessful()) {
                                                        Log.w(TAG, "getInstanceId failed", task1.getException());
                                                        return;
                                                    }
                                                    String token = task1.getResult().getToken();
                                                    Log.e("token", token);
                                                    FirebaseDatabase.getInstance().getReference().child("meta_data").child("device_tokens").child(FirebaseAuth.getInstance().getUid()).setValue(token);

                                                });
                                    } else
                                        GenericMethod.showMessage(PhoneVerificationActivity.this, "Welcome back");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Intent intent = new Intent(PhoneVerificationActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            String message = "Something is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
//                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                        }
                    }
                });
    }
}
