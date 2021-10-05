package com.commercial.tuds.earnandpay;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView;
    String eligibilityStatus;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        autoCompleteTextView = findViewById(R.id.SearchText);
        autoCompleteTextView.requestFocus();
        final ArrayList arrayList = new ArrayList(Arrays.asList((getResources().getStringArray(R.array.activity_list))));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        autoCompleteTextView.setAdapter(arrayAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String text = autoCompleteTextView.getText().toString();
                        int index = arrayList.indexOf(text);
                        eligibilityStatus = userDetails.getLoanEligibilityStatus();
                        switch (index){
                            case 0:
                                startActivity(new Intent(SearchActivity.this,MainActivity.class));
                                finish();
                                break;
                            case 1:
                                startActivity(new Intent(SearchActivity.this,ViewProfileActivity.class));
                                finish();
                                break;
                            case 2:
                                startActivity(new Intent(SearchActivity.this,EditProfileActivity.class));
                                finish();
                                break;
                            case 3:
                                startActivity(new Intent(SearchActivity.this,NotificationActivity.class));
                                finish();
                                break;
                            case 4:
                                startActivity(new Intent(SearchActivity.this,FAQActivity.class));
                                finish();
                                break;
                            case 5:
                                startActivity(new Intent(SearchActivity.this,ContactUsActivity.class));
                                finish();
                                break;
                            case 6:
                                startActivity(new Intent(SearchActivity.this,AddMoneyActivity.class));
                                finish();
                                break;
                            case 7:
                                startActivity(new Intent(SearchActivity.this,CheckBalanceActivity.class));
                                finish();
                                break;
                            case 8:
                                startActivity(new Intent(SearchActivity.this,InvestMoney.class));
                                finish();
                                break;
                            case 9:
                                startActivity(new Intent(SearchActivity.this,TransactionHistoryActivity.class));
                                finish();
                                break;
                            case 10:
                                startActivity(new Intent(SearchActivity.this,PayMoneyActivity.class));
                                finish();
                                break;
                            case 11:
                                Intent intent = new Intent(SearchActivity.this,RechargeActivity.class);
                                intent.putExtra("rechargeType","Prepaid");
                                startActivity(intent);
                                finish();
                                break;
                            case 12:
                                Intent intent1 = new Intent(SearchActivity.this,RechargeActivity.class);
                                intent1.putExtra("rechargeType","Postpaid");
                                startActivity(intent1);
                                finish();
                                break;
                            case 13:
                                startActivity(new Intent(SearchActivity.this,DthRechargeActivity.class));
                                finish();
                                break;
                            case 14:
                                startActivity(new Intent(SearchActivity.this,GasBillPaymentActivity.class));
                                finish();
                                break;
                            case 15:
                                startActivity(new Intent(SearchActivity.this,ElectricityBillActivity.class));
                                finish();
                                break;
                            case 16:
                                startActivity(new Intent(SearchActivity.this,BankTransferActivity.class));
                                finish();
                                break;
                            case 17:
                                if(eligibilityStatus.matches("true")) {
                                    GenericMethod.showMessage(SearchActivity.this, "You are already eligible for a loan");
                                    startActivity(new Intent(SearchActivity.this,FundGenerationActivity.class));
                                }
                                else if(eligibilityStatus.matches("pending")){
                                    GenericMethod.showMessage(SearchActivity.this, "Your application is under processing.Please check later.");
                                }
                                else if(eligibilityStatus.matches("false"))
                                    startActivity(new Intent(SearchActivity.this,LoanEligibilityActivity.class));
                                finish();
                                break;
                            case 18:
                                if(eligibilityStatus.matches("true"))
                                    startActivity(new Intent(SearchActivity.this, LoanApplicationActivity.class));
                                else if(userDetails.getLoanEligibilityStatus().matches("pending"))
                                    GenericMethod.showMessage(SearchActivity.this, "Your application is under processing.Please check later.");
                                else
                                    GenericMethod.showMessage(SearchActivity.this, "Please check your loan eligibility first.");
                                break;
                            case 19:
                                if(eligibilityStatus.matches("true")) {
                                    startActivity(new Intent(SearchActivity.this,FundGenerationActivity.class));
                                }
                                else if(eligibilityStatus.matches("pending")){
                                    GenericMethod.showMessage(SearchActivity.this, "Your application is under processing.Please check later.");
                                }
                                else if(eligibilityStatus.matches("false")) {
                                    GenericMethod.showMessage(SearchActivity.this, "Please check your loan eligibility first.");
                                    startActivity(new Intent(SearchActivity.this, LoanEligibilityActivity.class));
                                }
                                finish();
                                break;
                            case 20:
                                startActivity(new Intent(SearchActivity.this,EmiCalculatorActivity.class));
                                finish();
                                break;
                            case 21:
                                if(userDetails.getKycStatus())
                                    GenericMethod.showMessage(SearchActivity.this,"Your KYC has been completed successfully");
                                else
                                    startActivity(new Intent(SearchActivity.this,KycApprovalActivity.class));
                                finish();
                                break;
                            case 22:
                                startActivity(new Intent(SearchActivity.this,LoanHistoryActivity.class));
                                finish();
                                break;
                            case 23:
                                startActivity(new Intent(SearchActivity.this,DiscountCardActivity.class));
                                finish();
                                break;
                            case 24:
                                startActivity(new Intent(SearchActivity.this,BuyDiscountCardsActivity.class));
                                finish();
                                break;
                            case 25:
                                startActivity(new Intent(SearchActivity.this,ViewCardsActivity.class));
                                finish();
                                break;
                            case 26:
                                startActivity(new Intent(SearchActivity.this,ViewCreatedCardsActivity.class));
                                finish();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(SearchActivity.this,MainActivity.class));
                        finish();
                        break;
                    case 1:
                        startActivity(new Intent(SearchActivity.this,ViewProfileActivity.class));
                        finish();
                        break;
                    case 2:
                        startActivity(new Intent(SearchActivity.this,EditProfileActivity.class));
                        finish();
                        break;
                    case 3:
                        startActivity(new Intent(SearchActivity.this,NotificationActivity.class));
                        finish();
                        break;
                    case 4:
                        startActivity(new Intent(SearchActivity.this,FAQActivity.class));
                        finish();
                        break;
                    case 5:
                        startActivity(new Intent(SearchActivity.this,ContactUsActivity.class));
                        finish();
                        break;
                    case 6:
                        startActivity(new Intent(SearchActivity.this,AddMoneyActivity.class));
                        finish();
                        break;
                    case 7:
                        startActivity(new Intent(SearchActivity.this,CheckBalanceActivity.class));
                        finish();
                        break;
                    case 8:
                        startActivity(new Intent(SearchActivity.this,InvestMoney.class));
                        finish();
                        break;
                    case 9:
                        startActivity(new Intent(SearchActivity.this,TransactionHistoryActivity.class));
                        finish();
                        break;
                    case 10:
                        startActivity(new Intent(SearchActivity.this,PayMoneyActivity.class));
                        finish();
                        break;
                    case 11:
                        Intent intent = new Intent(SearchActivity.this,RechargeActivity.class);
                        intent.putExtra("rechargeType","Prepaid");
                        startActivity(intent);
                        finish();
                        break;
                    case 12:
                        Intent intent1 = new Intent(SearchActivity.this,RechargeActivity.class);
                        intent1.putExtra("rechargeType","Postpaid");
                        startActivity(intent1);
                        finish();
                        break;
                    case 13:
                        startActivity(new Intent(SearchActivity.this,DthRechargeActivity.class));
                        finish();
                        break;
                    case 14:
                        startActivity(new Intent(SearchActivity.this,GasBillPaymentActivity.class));
                        finish();
                        break;
                    case 15:
                        startActivity(new Intent(SearchActivity.this,ElectricityBillActivity.class));
                        finish();
                        break;
                    case 16:
                        startActivity(new Intent(SearchActivity.this,BankTransferActivity.class));
                        finish();
                        break;
                    case 17:
                        startActivity(new Intent(SearchActivity.this,LoanEligibilityActivity.class));
                        finish();
                        break;
                    case 18:
                        startActivity(new Intent(SearchActivity.this,LoanApplicationActivity.class));
                        finish();
                        break;
                    case 19:
                        startActivity(new Intent(SearchActivity.this,FundGenerationActivity.class));
                        finish();
                        break;
                    case 20:
                        startActivity(new Intent(SearchActivity.this,EmiCalculatorActivity.class));
                        finish();
                        break;
                    case 21:
                        startActivity(new Intent(SearchActivity.this,KycApprovalActivity.class));
                        finish();
                        break;
                    case 22:
                        startActivity(new Intent(SearchActivity.this,LoanHistoryActivity.class));
                        finish();
                        break;


                }
            }
        });

        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        String activityName = autoCompleteTextView.getText().toString();
                        switch (activityName) {
                            case "":
                                GenericMethod.showMessage(SearchActivity.this,"Please enter some text to search.");
                                break;
                            default:
                                GenericMethod.showMessage(SearchActivity.this,"Sorry, No such feature available yet.");
                                break;
                        }
                    }
                }
                return false;
            }
        });



    }
}
