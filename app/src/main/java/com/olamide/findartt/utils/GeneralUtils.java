package com.olamide.findartt.utils;

import android.content.Context;
import android.util.Base64;

import com.olamide.findartt.R;

public class GeneralUtils {


    public static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    public static boolean isTablet (Context context) {
        return  context.getResources().getBoolean(R.bool.isTablet);
    }

    public static boolean isLand (Context context) {
        return  context.getResources().getBoolean(R.bool.isLand);
    }
}
