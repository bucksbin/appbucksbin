package com.commercial.tuds.earnandpay.GenericMethods;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpClientRequest {
    OkHttpClient client = new OkHttpClient();

    public void run(String url) throws IOException {
        final Request request = new Request.Builder().url(url).build();

        try {
            client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful())
                {
                    Log.e("response",response.body().string());
                }
            }
            });
        }
        catch (Exception e) {

            Log.e("exception", e.toString());
        }

    }
}
