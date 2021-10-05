package com.commercial.tuds.earnandpay.Utils;

import com.commercial.tuds.earnandpay.Models.MobileRecharge;
import com.commercial.tuds.earnandpay.Models.RechargeResponse;


import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.commercial.tuds.earnandpay.constants.ConstantVariables.PAY2ALL_TOKEN;
import static com.commercial.tuds.earnandpay.constants.ConstantVariables.URL_PAY2ALL_DETAIL_CONTROLLER_SERVER;

public interface Api {

    String BaseURL = "https://api.pay2all.in/v1/";
    String BaseURLPay2All = "https://www.pay2all.in/api/v1/";

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer "+PAY2ALL_TOKEN
    })
    @POST("transaction")
    Observable<RechargeResponse> getRechargeResponse(@Body MobileRecharge body);

    @GET("paynow")
    Observable<RechargeResponse> getRechargeResponseR(@Query("api_token") String api_token, @Query("number") String number, @Query("provider_id") String providerId, @Query("amount") String amount, @Query("client_id") String ClientId);

    @GET("paynow")
    Observable<RechargeResponse> getElectricityResponse(@Query("api_token") String api_token, @Query("number") String number, @Query("provider_id") String providerId, @Query("amount") String amount, @Query("client_id") String ClientId, @Query("optional1") String optional1);

    @GET("paynow")
    Observable<RechargeResponse> getGasBillResponse(@Query("api_token") String api_token, @Query("number") String number, @Query("provider_id") String providerId, @Query("amount") String amount, @Query("client_id") String ClientId, @Query("cnumber") String cnumber, @Query("cemail") String cemail);

    @Headers({
            "Content-Type: application/json",
            "x-client-id:27577fc9386810cb3fad7e83d77572",
            "x-client-secret:af7ec7f8bd96d2c0b21253a0e50d4ad695e5d71e"
    })
    @POST("https://api.cashfree.com/api/v2/cftoken/order")
    Call<GenerateTokenPojo> getTokenFromServer(@Body GenerateTokenPojo body);

//    @GET(URL_PAY2ALL_DETAIL_CONTROLLER_SERVER)
//    Call<GetLoginDataPojo> getAndSetClassAttendanceData(@Query("owner") int user_id);


}