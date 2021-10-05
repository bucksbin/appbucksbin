package com.commercial.tuds.earnandpay;

import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.commercial.tuds.earnandpay.GenericMethods.Validation;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText mobileET;
    RelativeLayout verifyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mobileET = findViewById(R.id.mobileET);
        verifyBtn = findViewById(R.id.verifyBtn);

        final AlphaAnimation buttonClickAnimation = new AlphaAnimation(1.0f,0.8f);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                String mobile = mobileET.getText().toString().trim();
                if(!Validation.validateMobile(LoginActivity.this,mobile))
                    return;
                Intent intent = new Intent(LoginActivity.this,PhoneVerificationActivity.class);
                intent.putExtra("phone",mobile);
                startActivity(intent);
            }
        });
    }
}
