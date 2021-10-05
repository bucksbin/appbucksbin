package com.commercial.tuds.earnandpay;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.AccountDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddAccountActivity extends AppCompatActivity {
    TextInputEditText accNoEt,accNameEt,accIFSCEt,pincodeEt;
    RelativeLayout submitBtn;
    ImageView backIcon;
    String accNo,accName,accIFSC,pincode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        backIcon = findViewById(R.id.back_icon);
        accNoEt = findViewById(R.id.accountNumberET);
        accNameEt = findViewById(R.id.accountHolderET);
        accIFSCEt = findViewById(R.id.IFSCCodeET);
        pincodeEt = findViewById(R.id.pincodeET);
        submitBtn = findViewById(R.id.submitBtn);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Add account details");



        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("meta_data").child("bank_accounts").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    AccountDetails accDetails = dataSnapshot.getValue(AccountDetails.class);
                    accNoEt.setText(accDetails.getAccountNumber());
                    accNameEt.setText(accDetails.getAccountName());
                    accIFSCEt.setText(accDetails.getIFSCCode());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final AlphaAnimation buttonClickAnimation = new AlphaAnimation(1.0f,0.8f);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                accNo = accNoEt.getText().toString();
                accName = accNameEt.getText().toString();
                accIFSC = accIFSCEt.getText().toString();
                pincode = pincodeEt .getText().toString();
                if(!validateDetails(accNo,accName,accIFSC,pincode)){
                    return;
                }
                AccountDetails accountDetails = new AccountDetails(accNo,accName,accIFSC);
                ref.setValue(accountDetails);
                accNoEt.setText("");
                accNameEt.setText("");
                accIFSCEt.setText("");
                pincodeEt.setText("");
                GenericMethod.showMessage(AddAccountActivity.this,"Details submitted successfully.");
                finish();
            }
        });

        backIcon.setOnClickListener(v -> {
            startActivity(new Intent(AddAccountActivity.this,MainActivity.class));
            finishAffinity();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddAccountActivity.this,MainActivity.class));
        finishAffinity();
    }

    public boolean validateDetails(String accNo, String accName, String IFSC,String Pincode)
    {
        if(accNo == null || accNo.matches("")){
            accNoEt.setError("Enter a valid account number.");
            return false;
        }
        if(accName == null || accName.matches("")){
            accNameEt.setError("Enter a valid account name.");
            return false;
        }
        if(IFSC == null || IFSC.matches("")){
            accIFSCEt.setError("Enter a valid IFSC code.");
            return false;
        }
        if (Pincode == null || Pincode.matches("") || Pincode.length()<6){
            pincodeEt.setError("Enter a valid pincode");
            return false;
        }
        return true;
    }


}
