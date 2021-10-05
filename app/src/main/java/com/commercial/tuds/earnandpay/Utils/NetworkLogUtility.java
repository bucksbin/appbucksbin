package com.commercial.tuds.earnandpay.Utils;

import android.text.TextUtils;
import android.util.Log;

import okhttp3.HttpUrl;
import okhttp3.Request;
import retrofit2.Call;

/**
 * Created by Abhimanoj on 12-03-2018.
 */

public class NetworkLogUtility {
    public static void logFailure(Call call, Throwable throwable) {
        if (call != null) {
            if (call.isCanceled())
                Log.d("errorCall", "Request was cancelled");

            Request request = call.request();
            if (request != null) {
                HttpUrl httpUrl = request.url();
                if (httpUrl != null) {
                    Log.d("Timber", "" + String.format("logFailure() : %s : failed", httpUrl));

                }
            }
        }

        if (throwable != null) {
            Throwable cause = throwable.getCause();
            String message = throwable.getMessage();

            if (cause != null) {
                //Timber.e(String.format("logFailure() : cause.toString() : %s", cause.toString()));
            }

            if (!TextUtils.isEmpty(message)) {
                Log.d("Timber", "" + String.format("logFailure() : message : %s", message));
            }

            throwable.printStackTrace();
        }
    }
}
