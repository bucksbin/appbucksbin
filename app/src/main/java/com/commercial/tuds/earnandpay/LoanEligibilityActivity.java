package com.commercial.tuds.earnandpay;

import android.content.Intent;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.LoanEligibilityApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoanEligibilityActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    ImageView backIcon;
    TextInputEditText usernameET,organisationNameET,employeeStatusET,annualIncomeET,PanNumberET;
    RelativeLayout submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_eligibility);

        backIcon = findViewById(R.id.back_icon);
        usernameET = findViewById(R.id.nameET);
        organisationNameET = findViewById(R.id.organisationNameET);
        employeeStatusET = findViewById(R.id.statusET);
        annualIncomeET = findViewById(R.id.annualIncomeET);
        PanNumberET = findViewById(R.id.panNumberET);
        submitBtn = findViewById(R.id.checkEligibilityBtn);

        final AlphaAnimation buttonClickAnimation = new AlphaAnimation(1.0f, 0.8f);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setAnimation(buttonClickAnimation);
                String username = usernameET.getText().toString();
                String annualIncome = annualIncomeET.getText().toString();
                String panNo = PanNumberET.getText().toString();

                if(username.matches("")) {
                    usernameET.setError("Name can't be empty.");
                    return;
                }
                if(annualIncome.matches("")) {
                    annualIncomeET.setError("Annual Income can't be empty.");
                    return;
                }
                if(panNo.matches("")) {
                    PanNumberET.setError("Pan Number can't be empty.");
                    return;
                }
                LoanEligibilityApplication loanEligibilityApplication = new LoanEligibilityApplication(username,organisationNameET.getText().toString(),employeeStatusET.getText().toString(),annualIncome,panNo);
                databaseReference = FirebaseDatabase.getInstance().getReference().child("loan_eligibility_application").child(FirebaseAuth.getInstance().getUid());
                databaseReference.setValue(loanEligibilityApplication);
                GenericMethod.showMessage(LoanEligibilityActivity.this,"Your application has been received.");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid()).child("loanEligibilityStatus");
                databaseReference.setValue("pending");
                finish();
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
