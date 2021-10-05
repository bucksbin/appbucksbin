package com.commercial.tuds.earnandpay.constants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Abhimanoj on 15-02-2018.
 */
public class ConstantVariables {

    private Context context;
    public static final int SET_DATABASE_VERSION = 10;
    public static final String SET_DATABASE_NAME = "infinityDataLocal";
    public static final String TIMESTAMP_FORMAT_NEW = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String TIMESTAMP_DATE_ONLY = "yyyy-MM-dd";
    public static final String TIMESTAMP_TIME_ONLY = "HH:mm:ss";
    public static final String OLD_TIMESTAMP_FORMAT = "yyyy-MM-dd kk:mm:ss";

    public static final String SERVER_IP = "http://aspirantstudyzone.com/";
    public static final String PAY2ALL_IP = "https://www.pay2all.in/web-api/get-provider";

    public static final String URL_GET_RESULT_DATA_IN_DETAIL_CONTROLLER_SERVER = SERVER_IP + "get-calculated-result/";
    public static final String URL_PAY2ALL_DETAIL_CONTROLLER_SERVER = PAY2ALL_IP + "";
    public static final String PAY2ALL_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyMDgiLCJqdGkiOiJkNjA1ZDIwYTIxNDQ2MDZlOTViYjdiYjg3N2U1MTBkYjkyMWFmMGZiOGYzYjFjMGQ5YmY5ZjNkMTFlNGQ4YjdmNGJkNDJhMWZjNjkxYTIzMyIsImlhdCI6MTYyODQ0Nzk0MC4xMzQzNDYwMDgzMDA3ODEyNSwibmJmIjoxNjI4NDQ3OTQwLjEzNDM1MDA2MTQxNjYyNTk3NjU2MjUsImV4cCI6MTY0NDM0NTU0MC4wODY4MzcwNTMyOTg5NTAxOTUzMTI1LCJzdWIiOiIxODc0OSIsInNjb3BlcyI6W119.hN7uy7BFkfc6EO6gsggpOv4M2WXXmvwlLWOIwHK2mBlayFbte6LGF8Xzhdkl95hA96lGBZDJfz3wj9thQS6WpBPkxx7KTwikx3jp5AdfesUNoS3XbnKl-RhUujwkKPzQrW3EmCqdOsXetGsH_97MMS7pmFPJVWcm7ahcz5s3L6u5Noi-krDVMbqSlNit0ro9fCzK_vKXXNOLZfvjYGeg9TjhuDHLMHpJfLeyb1GsFyTeKib7zwx52kOvZQyBo8nRplDUFfq-AFQyD1Rwqbh8yy2SDG_ZmNUoIlUhOTyG9jUfRIxj1K3CcDJR7UlDtd75EQwJZhc_e3vKFQaP_pYbY8DZhtGYFJI9bxtO-SgOFo89L1cl2TBhEZMFuSpNeoAEp0hQadIA_gnsdqwg7_1jUNlrZfc8I0ezAc8HGwF5AdIgokbmBUX4ZyyVHP2zfIPWJYA80EG3tTLbzLW-SpRx03cFbZkwJpRCz6ulAUepxnd-MMA4wNWDcd6ILGfrmDbPJQVDvSAO5UzGjBBwShiMEf2_Jmwt8bzgmXSxQmDU-Y-ssor4LCs0lvXJ9zm47fZXQsRCnevRH0MEfRTaVKeXB0ycsngK40Fxogv9QMEQHXRDSvTN0ls3pcSUD8V08FnRS5Jnb99StDfOZPgmrz9HETbWAaHk4qXGf4FlZvy5GjE";
//    public static final String PAY2ALL_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjMxOGRiMDEzNjdmOWY3NDc0NDgzNTE3ODFjOGMzNjMyZGI2NmEyNzZlYzMzMGFiMzhkNmQxMGM2NjZkMjcxY2ZhNTc1Y2VkYWM4ZDg3YjQzIn0.eyJhdWQiOiIxIiwianRpIjoiMzE4ZGIwMTM2N2Y5Zjc0NzQ0ODM1MTc4MWM4YzM2MzJkYjY2YTI3NmVjMzMwYWIzOGQ2ZDEwYzY2NmQyNzFjZmE1NzVjZWRhYzhkODdiNDMiLCJpYXQiOjE2Mjc1ODEwNTUsIm5iZiI6MTYyNzU4MTA1NSwiZXhwIjoxNjU5MTE3MDU1LCJzdWIiOiI0ODEiLCJzY29wZXMiOltdfQ.CnmawB_t7M5xySBsXeCU4xZnvLRQ81JRvAYnxzF9ctp9bm_gxjla9je62w9XS_j1vp4ZEnPL1XDnTijRZ1-D_LZab_DAAI_Y8P5DGYJR-QHjRemT0u3Pb58AUMzOENnIZSjI_kRHgfjbYlwsIfRjvUHe_j1Yw39SHbYL03-ilMGeCb4T_Ax20Pu12gxqN15AbZKW7XXWIIyWxhBTsOqDvmqRbqyw7KoRbz9K91fwg5vOBxqic8K84ZtRZoJEMM85MIXLGd2iRVI50qRHzmQscMskcM4PEqd_jRRxt2gVx5Pee5tXDGbzj6HX1N1953jUuPCMhDY5V3b7gWunp-T4wYXCoHpU16ihJ2effAT_OlOU5VpH0IeFoIM926V7xJF7HNXrcxBLRB5LOyUXosOrmGRfsbs16qXnKcI2NmjfsUE9BUeXd4egiC3D42sNboFSmmuSAHlwyELfojDfMUKfJOObLSQ9ZT5M_LsmyjXe-P5boz-L7Urlx5kctVYYsrbNI9ph2BEbn3T5K0kwRxC4dTxVhim309plur9P2SV5FJQkY_As_NL9AB8FUoFcdCYrwvRyhx4QZ_tMWwFjN7Lg1OV4q_lHwjzxknz2qGgsgh-P0J4sczqUII9GmEzQ4zZbcgwwbHCFa2b2LiBpBKCssWQeqaG5VcNgGxXxeTmfiFQ";


    //DEVICE's WIDTH and HEIGHT ....
    public static int DEVICE_WIDTH = -1;
    public static int DEVICE_HEIGHT = -1;

    // Font Typefaces to be used throughout the Application and are initialized inside the APP-MANAGER Class....
    public static Typeface ROBOTO_REGULAR_TYPEFACE;
    public static Typeface ROBOTO_BOLD_TYPEFACE;
    public static Typeface ROBOTO_MEDIUM_TYPEFACE;
    public static Typeface ROBOTO_LIGHT_TYPEFACE;
    public static Typeface ROBOTO_THIN_TYPEFACE;


    /**
     * Round to certain number of decimals
     *
     * @param d
     * @return
     */
    public static float round(float d) {
        int decimalPlace = 0;
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }


    /**
     * Convert to numbers in rupee format..
     *
     * @param amount
     * @return
     */
    public static String rupeeFormat(float amount) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        BigDecimal bigDecimal4 = new BigDecimal(Math.round(amount));
        String totalAmountIs = (format.format(bigDecimal4.toBigInteger()).replaceAll("\\.0*$", ""));
        return totalAmountIs;
    }

    /**
     * Convert to numbers in USD format..
     *
     * @param amount
     * @return
     */
    public static String usdFormat(float amount) {

        return ("USD " + String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(round(findExchangeRateAndConvert("INR", "USD", amount)))));
    }

    /**
     * remove .0 f
     *
     * @param amount
     * @return
     */
    public static String removeZeroAfterDecimal(float amount) {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        return format.format(amount);
    }

    private static float findExchangeRateAndConvert(String from, String to, float amount) {
        try {
            //Yahoo Finance API
            // URL url = new URL("http://finance.yahoo.com/d/quotes.csv?f=l1&s=" + from + to + "=X");
            //BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            // String line = reader.readLine();
            //TODO jst
            float line = 0.0156f;

            return (line * amount);

            //reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }


    public static String convertDate(String args) {
        DateFormat df = new SimpleDateFormat(TIMESTAMP_FORMAT);
        Date objDate = null;
        try {
            objDate = df.parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strDateFormat = "MMM dd yyyy, hh:mm a"; //Date format is Specified
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); //Date format string is passed as an argument to the Date format object
        return (objSDF.format(objDate)); //Date formatting is applied to the current date

    }

    public static String convertDateNewFormat(String args) {
        DateFormat df = new SimpleDateFormat(TIMESTAMP_FORMAT_NEW);
        Date objDate = null;
        try {
            objDate = df.parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strDateFormat = "MMM dd yyyy, hh:mm a"; //Date format is Specified
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); //Date format string is passed as an argument to the Date format object
        return (objSDF.format(objDate)); //Date formatting is applied to the current date

    }

    public static String getDateFormat(String args) {
        DateFormat df = new SimpleDateFormat(TIMESTAMP_FORMAT);
        Date objDate = null;
        try {
            objDate = df.parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strDateFormat = "MMM dd yyyy, hh:mm a"; //Date format is Specified
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); //Date format string is passed as an argument to the Date format object
        return (objSDF.format(objDate)); //Date formatting is applied to the current date

    }


    public static String getTimeFormat(String args) {
        DateFormat df = new SimpleDateFormat(TIMESTAMP_TIME_ONLY);
        Date objDate = null;
        try {
            objDate = df.parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strDateFormat = "hh:mm a"; //Date format is Specified
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); //Date format string is passed as an argument to the Date format object
        return (objSDF.format(objDate)); //Date formatting is applied to the current date

    }

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        //View focusedView = activity.getCurrentFocus();

        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static boolean compareDates(String d1, String d2) {
        boolean answer = false;

        try {

            Log.d("qwer", "####" + d1 + " -- " + d2);
            if (d1 == null || d2 == null)
                return false;

            // Create 2 dates starts
            DateFormat sdf = new SimpleDateFormat(TIMESTAMP_FORMAT);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            if (date1.compareTo(date2) == 1) {
                answer = true;//"Date1 is after Date2";

            } else {
                answer = false;//"Date1 is before Date2";

            }
            // before() will return true if and only if date1 is before date2
//            if(date1.before(date2)){
//                answer = false;//"Date1 is before Date2";
//
//            }

            //equals() returns true if both the dates are equal
//            if(date1.equals(date2)){
//                answer = false;
//            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return answer;
    }



    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("Provider.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

