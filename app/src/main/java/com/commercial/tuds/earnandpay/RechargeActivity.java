package com.commercial.tuds.earnandpay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.commercial.tuds.earnandpay.Models.MobileRecharge;
import com.commercial.tuds.earnandpay.Models.ProviderModel;
import com.google.android.material.textfield.TextInputEditText;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

import static com.commercial.tuds.earnandpay.constants.ConstantVariables.PAY2ALL_TOKEN;
import static com.commercial.tuds.earnandpay.constants.ConstantVariables.loadJSONFromAsset;

public class RechargeActivity extends AppCompatActivity {
    RadioButton prepaidBtn, postpaidBtn;
    TextInputEditText mobileNumberET, amountET;
    MaterialBetterSpinner operatorSpinner;
    RelativeLayout submitBtn;
    LinearLayout btnText;
    String mobile;
    int operatorCode = 0;
    float amount;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    DatabaseReference databaseReference;
    View subView;
    AVLoadingIndicatorView loader, btnLoader;
    private String TAG = "RechargeActivity";
    private final int PICK_CONTACT = 101;
    ImageView backIcon;
    ArrayList<String> providersNameList = new ArrayList<>();
    NestedScrollView scrollView;
    HashMap<String, String> providersList = new HashMap<>();
    Retrofit retrofit;
    String clientId;
    private float remainingBalance;
    private Context context;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        context = RechargeActivity.this;
        operatorSpinner = findViewById(R.id.networkOperator);
        prepaidBtn = findViewById(R.id.prepaidButton);
        postpaidBtn = findViewById(R.id.postPaidButton);
        mobileNumberET = findViewById(R.id.mobileET);
        amountET = findViewById(R.id.amountET);
        submitBtn = findViewById(R.id.rechargeBtn);
        backIcon = findViewById(R.id.back_icon);
        loader = findViewById(R.id.loaderAnimation);
        scrollView = findViewById(R.id.scrollView);
        btnLoader = findViewById(R.id.buttonLoader);
        btnText = findViewById(R.id.buttonText);
        loader.show();
        loader.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        builder = new AlertDialog.Builder(RechargeActivity.this);
        builder.setView(subView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        Intent intent = getIntent();
        String simType = "Prepaid";

        if (intent.getStringExtra("rechargeType") != null && intent.getStringExtra("rechargeType").equals("Postpaid"))
            simType = intent.getStringExtra("rechargeType");

        Toast.makeText(this, "" + simType, Toast.LENGTH_SHORT).show();

        //initial task
        amountET.setFilters(new InputFilter[]{new DecimalFilter(6, 2)});
        mobileNumberET.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (mobileNumberET.getRight() - mobileNumberET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    Dexter.withActivity(RechargeActivity.this)
                            .withPermission(Manifest.permission.READ_CONTACTS)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                    startActivityForResult(intent1, PICK_CONTACT);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            })
                            .onSameThread()
                            .check();

                    return true;
                }
            }
            return false;
        });

        prepaidBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {

            providersList = new HashMap<>();

//            String URl = "https://api.pay2all.in/v1/app/providers";
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, URl,
//                    response -> {
//                        try {
//                            providersNameList = new ArrayList<>();
//                            JSONObject obj = new JSONObject(response);
//                            JSONArray providersArray = obj.getJSONArray("providers");
//                            String service_id = "3";
//                            if (isChecked)
//                                service_id = "1";
//
//                            for (int i = 0; i < providersArray.length(); i++) {
//                                JSONObject provider = providersArray.getJSONObject(i);
//
//                                if (provider.getString("service_id").equals(service_id) && provider.getString("status").equals("1")) {
//
//                                    providersList.put(provider.getString("provider_name"), String.valueOf(provider.get("id")));
//                                    providersNameList.add(provider.getString("provider_name"));
//                                }
//                            }
//
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RechargeActivity.this, android.R.layout.simple_dropdown_item_1line, providersNameList);
//                            operatorSpinner.setAdapter(arrayAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            GenericMethod.showMessage(RechargeActivity.this, "Sorry unable to load operators. Please try later");
//                        }
//                        loader.setVisibility(View.INVISIBLE);
//                        scrollView.setVisibility(View.VISIBLE);
//                    },
//                    error -> {
//
//                        GenericMethod.showMessage(RechargeActivity.this, "Sorry unable to load operators. Please try later..");
//                        loader.setVisibility(View.INVISIBLE);
//                        scrollView.setVisibility(View.VISIBLE);
//                    }) {
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Authorization", "Bearer " + PAY2ALL_TOKEN);
//                    params.put("Content-Type", "application/json");
//                    return params;
//                }
//            };
//
//
//            try {
//                RequestQueue requestQueue = Volley.newRequestQueue(RechargeActivity.this);
//                requestQueue.add(stringRequest);
//            } catch (Exception e) {
//                Toast.makeText(RechargeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
        });

        if (simType.matches("Prepaid")) {
            prepaidBtn.setChecked(true);
        } else {
            prepaidBtn.setChecked(true);
            postpaidBtn.setChecked(true);

        }

        final String serviceNeeded = "Mobile";

        //setData
        ProviderModel providerModel = new Gson().fromJson(loadJSONFromAsset(this), ProviderModel.class);

        for (int i = 0; i < providerModel.getProviders().size(); i++) {

            if (providerModel.getProviders().get(i).getService_id() == 1 && providerModel.getProviders().get(i).getStatus() == 1) {

                providersNameList.add(providerModel.getProviders().get(i).getProvider_name());
                providersList.put(providerModel.getProviders().get(i).getProvider_name(), String.valueOf(providerModel.getProviders().get(i).getId()));
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RechargeActivity.this, android.R.layout.simple_dropdown_item_1line, providersNameList);
        operatorSpinner.setAdapter(arrayAdapter);
        loader.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);

        //Mid-tasks
        operatorSpinner.setOnItemClickListener((parent, view, position, id) -> operatorCode = Integer.parseInt(providersList.get(providersNameList.get(position))));

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobile = mobileNumberET.getText().toString();
                if (amountET.getText().toString().matches("")) {
                    Toast.makeText(context, "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                amount = Float.parseFloat(amountET.getText().toString());

                clientId = FirebaseAuth.getInstance().getUid().substring(0, 12);

                if (validateValues(mobile, operatorCode, amount)) {
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
                                GenericMethod.showMessage(RechargeActivity.this, "Your wallet balance is low than recharge amount.");
                            } else {
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
                                        .baseUrl(Api.BaseURLPay2All)
                                        .client(okHttpClient)
                                        .build();
                                Api api = retrofit.create(Api.class);

                                MobileRecharge mobileRecharge = new MobileRecharge();
                                mobileRecharge.setNumber(mobile);
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
                            Toast.makeText(context, "" + rechargeResponse, Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("payId", rechargeResponse.payid);
                            bundle.putFloat("amount", amount);
                            bundle.putString("clientId", clientId);
                            bundle.putString("mobile", mobile);
                            bundle.putString("operatorRef", rechargeResponse.operator_ref);
                            bundle.putFloat("closingBalance", remainingBalance);
                            bundle.putString("status", rechargeResponse.status);
                            bundle.putString("message", rechargeResponse.message);
                            bundle.putString("type", "Mobile Recharge");
                            Intent intent = new Intent(RechargeActivity.this, RechargeStatusActivity.class);
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


    public boolean validateValues(String mobile, int operatorCode, float amount) {
        if (mobile == null || mobile.matches("") || mobile.length() != 10)
            mobileNumberET.setError("Enter a valid mobile number");
        else if (operatorCode == 0)
            GenericMethod.showMessage(RechargeActivity.this, "This network operator is not supported yet.");
        else if (amount == 0)
            amountET.setError("Enter a valid amount.");
        else if (amount < 10)
            amountET.setError("Min Recharge amount is 10");
        else
            return true;
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            if (cNumber.matches(""))
                                GenericMethod.showMessage(RechargeActivity.this, "Unable to fetch number.Prefer typing it manually");
                            cNumber = cNumber.replace(" ", "").replace("-", "");
                            if (cNumber.length() != 10)
                                try {
                                    cNumber = cNumber.substring(3, 13);
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            mobileNumberET.setText(cNumber);
                        }
                    }
                }
                break;
        }
    }
}
