package com.olamide.findartt.utils;

import android.content.Context;
import android.util.Base64;

import com.olamide.findartt.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralUtils {

    private static final long MINUTE_MILLIS = 1000 * 60;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long WEEK_MILLIS = 7 * DAY_MILLIS;
    private static final long YEAR_MILLIS = 365 * DAY_MILLIS;


    public static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static boolean isLand(Context context) {
        return context.getResources().getBoolean(R.bool.isLand);
    }

    public static String dateFromNowFormat(long dateMillis) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM" , Locale.ENGLISH);
        String date = "";
        long now = System.currentTimeMillis();

        // Change how the date is displayed depending on whether it was written in the last minute,
        // the hour, etc.
        if (now - dateMillis < (WEEK_MILLIS)) {
            if (now - dateMillis < (HOUR_MILLIS)) {
                long minutes = Math.round((now - dateMillis) / MINUTE_MILLIS);
                date = String.valueOf(minutes) + "m" ;
            } else if (now - dateMillis < (DAY_MILLIS)) {
                long hours = Math.round((now - dateMillis) / HOUR_MILLIS);
                date = String.valueOf(hours) + "h";
            } else {
                long hours = Math.round((now - dateMillis) / DAY_MILLIS);
                date = String.valueOf(hours) + "d";
            }
        } else {
            Date dateDate = new Date(dateMillis);
            if (now - dateMillis > (YEAR_MILLIS)) {
                sDateFormat = new SimpleDateFormat("dd MMM yyyy" , Locale.ENGLISH);
            }
            date = sDateFormat.format(dateDate);

        }

        // Add a dot to the date string
        date = "\u2022 " + date;
        return date;
    }
}
