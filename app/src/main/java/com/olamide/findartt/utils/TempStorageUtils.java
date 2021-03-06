package com.olamide.findartt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.olamide.findartt.AppConstants;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;

import timber.log.Timber;

import static com.olamide.findartt.AppConstants.ACCESS_TOKEN_STRING;
import static com.olamide.findartt.AppConstants.CURRENT_USER;
import static com.olamide.findartt.AppConstants.USEREMAIL_STRING;
import static com.olamide.findartt.AppConstants.USERPASSWORD_STRING;

public class TempStorageUtils {


    protected final static int DEFAULT_INT = 0;
    protected final static String DEFAULT_STRING = "";
    static int tempInt = 0;
    static String tempString = "";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new GsonBuilder().create();

    public static int readSharedPreferenceNumber(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        tempInt = preferences.getInt(key, DEFAULT_INT);
        return tempInt;
    }

    public static String readSharedPreferenceString(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        tempString = preferences.getString(key, DEFAULT_STRING);
        return tempString;
    }

    public static void writeSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void writeSharedPreferenceNumber(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static void writeSharedPreferenceNumber(Context context, String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, (int) value);
        editor.apply();
    }


    public static void removeSharedPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }


    public static void storeUserLogin(Context context, UserLogin userLogin) {
        writeSharedPreferenceString(context, AppConstants.USEREMAIL_STRING, GeneralUtils.encrypt(userLogin.getEmail()));
        writeSharedPreferenceString(context, AppConstants.USERPASSWORD_STRING, GeneralUtils.encrypt(userLogin.getPassword()));

    }

    public static UserLogin getUserLogin(Context context) {

        UserLogin userLogin = new UserLogin();
        String userEmail = TempStorageUtils.readSharedPreferenceString(context, USEREMAIL_STRING);
        String userPass = TempStorageUtils.readSharedPreferenceString(context, USERPASSWORD_STRING);
        userLogin.setEmail(GeneralUtils.decrypt(userEmail));
        userLogin.setPassword(GeneralUtils.decrypt(userPass));
        return userLogin;
    }

    public static void removeUserLogin(Context context) {

        TempStorageUtils.removeSharedPreference(context, USEREMAIL_STRING);
        TempStorageUtils.removeSharedPreference(context, USERPASSWORD_STRING);

    }


    public static void storeActiveUser(Context context, UserResult userResult) {

        Gson gson = new GsonBuilder().create();
        try {
            writeSharedPreferenceString(context, AppConstants.CURRENT_USER, new GsonBuilder().create().toJson(userResult.getUser()));
            writeSharedPreferenceString(context, AppConstants.ACCESS_TOKEN_STRING, userResult.getTokenInfo().getAccessToken());
        } catch (Exception e) {
            Timber.e(e);
        }

    }

    public static UserResult getActiveUser(Context context) {
        try {
            UserResult userResult = new UserResult();
            String user = readSharedPreferenceString(context, CURRENT_USER);
            String accessToken = readSharedPreferenceString(context, ACCESS_TOKEN_STRING);
            userResult.setUser(new GsonBuilder().create().fromJson(user, User.class));
            userResult.setTokenInfo(new TokenInfo(accessToken));
            return userResult;
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    public static void removeActiveUser(Context context) {

        removeSharedPreference(context, CURRENT_USER);
        removeSharedPreference(context, ACCESS_TOKEN_STRING);

    }


}
