package com.commercial.tuds.earnandpay;

import static com.commercial.tuds.earnandpay.constants.ConstantVariables.loadJSONFromAsset;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.commercial.tuds.earnandpay.Models.MobileRecharge;
import com.commercial.tuds.earnandpay.Models.ProviderModel;
import com.google.android.material.textfield.TextInputEditText;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.commercial.tuds.earnandpay.GenericMethods.GenericMethod;
import com.commercial.tuds.earnandpay.Models.RechargeResponse;
import com.commercial.tuds.earnandpay.Utils.Api;
import com.commercial.tuds.earnandpay.Utils.DecimalFilter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wang.avi.AVLoadingIndicatorView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DthRechargeActivity extends AppCompatActivity {

    TextInputEditText dthNumberET, amountET;
    MaterialBetterSpinner operatorSpinner;
    RelativeLayout submitBtn;
    LinearLayout btnText;
    String dthCustomerId;
    int operatorCode = 0;
    float amount;
    DatabaseReference databaseReference;
    AVLoadingIndicatorView loader, btnLoader;
    private String TAG = "DthRechargeActivity";
    ImageView backIcon;
    ArrayList<String> providersNameList;
    NestedScrollView scrollView;
    HashMap<String, String> providersList;
    Retrofit retrofit;
    String clientId;
    private float remainingBalance;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dth_recharge);

        operatorSpinner = findViewById(R.id.dthOperator);
        dthNumberET = findViewById(R.id.customerIdET);
        amountET = findViewById(R.id.amountET);
        submitBtn = findViewById(R.id.payNowBtn);
        backIcon = findViewById(R.id.back_icon);
        loader = findViewById(R.id.loaderAnimation);
        scrollView = findViewById(R.id.scrollView);
        btnLoader = findViewById(R.id.buttonLoader);
        btnText = findViewById(R.id.buttonText);

        loader.show();
        loader.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

        //Loading Ads
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        amountET.setFilters(new InputFilter[]{new DecimalFilter(6, 2)});


        //Loading Operators List
        providersList = new HashMap<>();
//        String URl = "https://api.pay2all.in/v1/app/providers";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URl,
//                response -> {
//                    try {
//                        providersNameList = new ArrayList<>();
//                        JSONObject obj = new JSONObject(response);
//                        JSONArray providersArray = obj.getJSONArray("providers");
//                        for (int i = 0; i < providersArray.length(); i++) {
//                            JSONObject provider = providersArray.getJSONObject(i);
//
//                            if (provider.getString("service_id").equals("2") && provider.getString("status").equals("1")) {
//                                providersList.put(provider.getString("provider_name"),String.valueOf(provider.get("id")));
//                                providersNameList.add(provider.getString("provider_name"));
//                            }
//                        }
//                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DthRechargeActivity.this,android.R.layout.simple_dropdown_item_1line,providersNameList);
//                        operatorSpinner.setAdapter(arrayAdapter);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        GenericMethod.showMessage(DthRechargeActivity.this,"Sorry unable to load operators. Please try later.");
//                    }
//                    loader.setVisibility(View.INVISIBLE);
//                    scrollView.setVisibility(View.VISIBLE);
//                },
//                error -> {
//                    GenericMethod.showMessage(DthRechargeActivity.this, "Sorry unable to load operators. Please try later.");
//                    loader.setVisibility(View.INVISIBLE);
//                    scrollView.setVisibility(View.VISIBLE);
//                }){
//                    @Override
//                    public Map<String, String> getHeaders() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Authorization", "Bearer " + getResources().getString(R.string.pay2all_token));
//                        params.put("Content-Type", "application/json");
//                        return params;
//                    }
//                };
//        try {
//            RequestQueue requestQueue = Volley.newRequestQueue(DthRechargeActivity.this);
//            requestQueue.add(stringRequest);
//        } catch (Exception e) {
//            Toast.makeText(DthRechargeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }


        //setData
        ProviderModel providerModel = new Gson().fromJson(loadJSONFromAsset(this), ProviderModel.class);
        providersNameList = new ArrayList<>();

        for (int i = 0; i < providerModel.getProviders().size(); i++) {

            if (providerModel.getProviders().get(i).getService_id() == 2 && providerModel.getProviders().get(i).getStatus() == 1) {

                providersNameList.add(providerModel.getProviders().get(i).getProvider_name());
                providersList.put(providerModel.getProviders().get(i).getProvider_name(), String.valueOf(providerModel.getProviders().get(i).getId()));
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DthRechargeActivity.this, android.R.layout.simple_dropdown_item_1line, providersNameList);
        operatorSpinner.setAdapter(arrayAdapter);
        loader.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);

        //Setting click Listener on spinner item.
        operatorSpinner.setOnItemClickListener((parent, view, position, id) -> operatorCode = Integer.parseInt(Objects.requireNonNull(providersList.get(providersNameList.get(position)))));

        //Setting Click Listener on Submit Btn.
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag", "clicking button");
                dthCustomerId = dthNumberET.getText().toString();
                if (!amountET.getText().toString().matches(""))
                    amount = Float.parseFloat(amountET.getText().toString());
                else
                    amount = 0f;
                clientId = FirebaseAuth.getInstance().getUid().substring(0, 12);
                if (validateValues(dthCustomerId, operatorCode, amount)) {
                    submitBtn.setEnabled(false);
                    btnText.setVisibility(View.INVISIBLE);
                    btnLoader.show();
                    btnLoader.setVisibility(View.VISIBLE);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("meta_data").child("balances").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mainBalance");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("CheckResult")
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            final float walletBalance = dataSnapshot.getValue(Float.class);
                            if (amount > walletBalance) {
                                submitBtn.setEnabled(true);
                                btnLoader.hide();
                                btnLoader.setVisibility(View.GONE);
                                btnText.setVisibility(View.VISIBLE);
                                GenericMethod.showMessage(DthRechargeActivity.this, "Your wallet balance is low than recharge amount.");
                            } else {
                                Log.e(TAG, "Balance before deduction:" + walletBalance);
                                remainingBalance = walletBalance - amount;
                                databaseReference.setValue(remainingBalance);
                                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                        .connectTimeout(1, TimeUnit.MINUTES)
                                        .readTimeout(30, TimeUnit.SECONDS)
                                        .writeTimeout(15, TimeUnit.SECONDS)
                                        .build();


                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();


                                retrofit = new Retrofit.Builder()
                                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .baseUrl(Api.BaseURL)
                                        .client(okHttpClient)
                                        .build();
                                Api api = retrofit.create(Api.class);

                                MobileRecharge mobileRecharge = new MobileRecharge();
                                mobileRecharge.setNumber(dthCustomerId);
                                mobileRecharge.setProvider_id(String.valueOf(operatorCode));
                                mobileRecharge.setAmount(String.valueOf(amount));
                                mobileRecharge.setClient_id(String.valueOf(clientId));


                                api.getRechargeResponse(mobileRecharge)
                                        // Make request in the background thread
                                        .subscribeOn(Schedulers.io())
                                        .doOnNext(rechargeResponse -> System.out.println("RechargeResponse " + rechargeResponse.toString()))
                                        // Change the thread to main thread inorder to make changes to UI
                                        .observeOn(AndroidSchedulers.mainThread())
                                        // subscribe is called inorder to start network request
                                        .subscribe(this::getResponse, Throwable::printStackTrace);
                            }
                        }

                        private void getResponse(RechargeResponse rechargeResponse) {
                            Bundle bundle = new Bundle();
                            bundle.putString("payId", rechargeResponse.payid);
                            bundle.putFloat("amount", amount);
                            bundle.putString("clientId", clientId);
                            bundle.putString("mobile", dthCustomerId);
                            bundle.putString("operatorRef", rechargeResponse.operator_ref);
                            bundle.putFloat("closingBalance", remainingBalance);
                            bundle.putString("status", rechargeResponse.status);
                            bundle.putString("message", rechargeResponse.message);
                            bundle.putString("type", "DTH Recharge");
                            Intent intent = new Intent(DthRechargeActivity.this, RechargeStatusActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        //Final tasks
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

    public boolean validateValues(String dthCustomerId, int operatorCode, float amount) {
        if (dthCustomerId == null || dthCustomerId.matches(""))
            dthNumberET.setError("Enter a valid Customer ID.");
        else if (operatorCode == 0)
            GenericMethod.showMessage(DthRechargeActivity.this, "This network operator is not supported yet.");
        else if (amount == 0)
            amountET.setError("Enter a valid amount.");
        else if (amount < 100)
            amountET.setError("Min Recharge amount is 100");
        else
            return true;
        return false;
    }


}
