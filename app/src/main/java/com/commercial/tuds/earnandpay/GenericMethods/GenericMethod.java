package com.commercial.tuds.earnandpay.GenericMethods;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.commercial.tuds.earnandpay.Adapter.SlidingImageAdapter;
import com.commercial.tuds.earnandpay.Models.Transaction;
import com.commercial.tuds.earnandpay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;

public class GenericMethod {

    public static AlphaAnimation buttonClickAnimation = new AlphaAnimation(1.0f, 0.8f);

    public static boolean isNetworkAvailable(AppCompatActivity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static File getImageFile() {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File file = null;
        try {
            file = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            Log.e("fileCreationError", e.toString());
        }
        if (file == null) {
            Log.e("generic", "file is null");
        }
        return file;
    }

    public static int calculateDateDifference(Date currentDate, Date fixedBalanceDate) {
        long diff = currentDate.getTime() - fixedBalanceDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        Log.e("tag: days:", String.valueOf(diffDays));
        int year = (int) diffDays / 365;

        return year;
    }

    public static void showOfferPhotos(final Context context, final ViewPager mPager, final CircleIndicator circleIndicator) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("poster_images");
        final ArrayList<String> offerPhotosList = new ArrayList<>();

        final Boolean[] gotData = {false};
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    offerPhotosList.add(dataSnapshot1.getValue(String.class));
                }
                mPager.setAdapter(new SlidingImageAdapter(context, offerPhotosList));
                circleIndicator.setViewPager(mPager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static HashMap<String, String> getRechargeProviders(final Context context, Boolean isPrepaid) {

        final String serviceNeeded;
        final HashMap<String, String> providersList = new HashMap<>();

        if (isPrepaid)
            serviceNeeded = "Mobile Recharge";
        else
            serviceNeeded = "Postpaid Bill Payment";

        String URl = "https://www.pay2all.in/web-api/get-provider?api_token=" + context.getResources().getString(R.string.pay2all_token);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("response", response);
                            JSONObject obj = new JSONObject(response);
                            JSONArray providersArray = obj.getJSONArray("providers");
                            for (int i = 0; i < providersArray.length(); i++) {
                                JSONObject provider = providersArray.getJSONObject(i);
                                if (provider.getString("service").matches(serviceNeeded) && provider.getString("status").matches("Success")) {
                                    providersList.put(String.valueOf(provider.get("provider_id")), provider.getString("provider_name"));
                                }
                            }
                            Log.e("inside generic method", providersList.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GenericMethod.showMessage(context, "Sorry!! Unable to fetch Operators List");
                    }
                });
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.e("inside generic method", providersList.toString());
        return providersList;
    }

    public static void submitTransaction(final Transaction transaction) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("transactions").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Transaction> transactionArrayList = new ArrayList<>();
                transactionArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transaction1 = snapshot.getValue(Transaction.class);
                    transactionArrayList.add(transaction1);
                }
                transactionArrayList.add(transaction);
                databaseReference.setValue(transactionArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
