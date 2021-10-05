package com.commercial.tuds.earnandpay;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RechargeStatusActivity extends AppCompatActivity {
    ImageView resultImage;
    TextView resultStatus,resultDetail,okayBtnText;
    RelativeLayout okayBtn,mainLayout;
    AVLoadingIndicatorView loader;
    private float amount;
    private String mobile;
    private String payID;
    private String clientID,rechargeType;
    private float closingBalance;
    private String time,message,operatorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_status);
        resultImage = findViewById(R.id.resultIcon);
        resultStatus = findViewById(R.id.resultStatus);
        resultDetail = findViewById(R.id.resultDetail);
        okayBtn = findViewById(R.id.okayBtn);
        okayBtnText  = findViewById(R.id.okayBtnText);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String status = bundle.getString("status");
        amount = bundle.getFloat("amount");
        mobile = bundle.getString("mobile");
        payID = bundle.getString("payId");
        clientID  = bundle.getString("clientId");
        message = bundle.getString("message");
        operatorRef = bundle.getString("operatorRef");
        closingBalance = bundle.getFloat("closingBalance");
        rechargeType = bundle.getString("type");
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Date today = new Date();
        time = dateFormatter.format(today);
        loadStatus(status);
    }

    public void loadStatus(String status)
    {
        final Transaction transaction = new Transaction(rechargeType,amount,false,"debited",mobile,clientID+payID,operatorRef,time,message,closingBalance);
        switch (status) {
            case "success":
                transaction.setIsSuccessful(true);
                GenericMethod.submitTransaction(transaction);
                resultImage.setImageDrawable(getDrawable(R.drawable.successful_icon));
                resultStatus.setText("Recharge Successful");
                resultDetail.setText("Recharge of Rs" + amount + " on " + mobile + " is successful.");
                okayBtnText.setText("Okay");
                okayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(RechargeStatusActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                });
                break;
            case "pending":
                resultImage.setImageDrawable(getDrawable(R.drawable.pending_image));
                resultStatus.setText("Recharge Pending");
                resultDetail.setText("Recharge of Rs" + amount + " on " + mobile + " is pending. Please take a screenshot and do not close the application.\n\nTrans Id:" + payID + "\n\nClient Id:" + clientID);
                okayBtnText.setText("Refresh Status");
                okayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String refreshURL = "https://www.pay2all.in/web-api/get-status?api_token=" + getString(R.string.pay2all_token) + "&payid=" + payID;
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, refreshURL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject responseObj = new JSONObject(response);
                                            String status = responseObj.getString("status");
                                            loadStatus(status);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        GenericMethod.showMessage(RechargeStatusActivity.this, "Sorry!! Unable to read status.Please try again.");
                                    }
                                });
                        try {
                            RequestQueue requestQueue = Volley.newRequestQueue(RechargeStatusActivity.this);
                            requestQueue.add(stringRequest);
                        } catch (Exception e) {
                            Toast.makeText(RechargeStatusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "initiated":
                resultImage.setImageDrawable(getDrawable(R.drawable.pending_image));
                resultStatus.setText("Recharge Initiated");
                resultDetail.setText("Recharge of Rs" + amount + " on " + mobile + " is initiated. Please take a screenshot and do not close the application.\n\nTrans Id:" + payID + "\n\nClient Id:" + clientID);
                okayBtnText.setText("Refresh Status");
                okayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String refreshURL = "https://www.pay2all.in/web-api/get-status?api_token=" + getString(R.string.pay2all_token) + "&payid=" + payID;
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, refreshURL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject responseObj = new JSONObject(response);
                                            String status = responseObj.getString("status");
                                            loadStatus(status);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        GenericMethod.showMessage(RechargeStatusActivity.this, "Sorry!! Unable to read status.Please try again.");
                                    }
                                });
                        try {
                            RequestQueue requestQueue = Volley.newRequestQueue(RechargeStatusActivity.this);
                            requestQueue.add(stringRequest);
                        } catch (Exception e) {
                            Toast.makeText(RechargeStatusActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "failure":
                transaction.setIsSuccessful(false);
                transaction.setClosingBalance(closingBalance + amount);
                Random rand = new Random();
                long randNumber = rand.nextLong();
                transaction.setTransactionReferenceNumber(clientID + String.valueOf(randNumber).substring(0, 5));
                GenericMethod.submitTransaction(transaction);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mainBalance");
                databaseReference.setValue(closingBalance + amount);
                resultImage.setImageDrawable(getDrawable(R.drawable.failed_icon));
                resultStatus.setText("Recharge Failed");
                resultDetail.setText("Recharge of Rs" + amount + " on " + mobile + " is failed.\n\n Reason:" + message);
                okayBtnText.setText("Okay");
                okayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(RechargeStatusActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                });
                break;
            default:
                break;
        }
    }


}
