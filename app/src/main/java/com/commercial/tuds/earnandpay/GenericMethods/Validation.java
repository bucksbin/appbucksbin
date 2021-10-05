package com.commercial.tuds.earnandpay.GenericMethods;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

public class Validation {

    public static Boolean validateMobile(Context context, String mobile)
    {
        Log.e("tag",mobile);
        if(mobile==null || mobile.length()!=10 || !Patterns.PHONE.matcher(mobile).matches()){
            Toast.makeText(context,"Please enter a valid mobile number",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }


}
