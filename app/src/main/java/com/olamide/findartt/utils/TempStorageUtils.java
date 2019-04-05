package com.olamide.findartt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamide.findartt.Constants;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;

import java.io.IOException;

import timber.log.Timber;

import static com.olamide.findartt.Constants.ACCESS_TOKEN_STRING;
import static com.olamide.findartt.Constants.CURRENT_USER;
import static com.olamide.findartt.Constants.USEREMAIL_STRING;
import static com.olamide.findartt.Constants.USERPASSWORD_STRING;

public class TempStorageUtils {


    protected final static int DEFAULT_INT = 0;
    protected final static String DEFAULT_STRING = "";
    static int tempInt = 0;
    static String tempString = "";

    public static int readSharedPreferenceInt(Context context, String key) {
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

    public static void writeSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public static void removeSharedPreference(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }


    public static void storeUserLogin(Context context, UserLogin userLogin) {
        writeSharedPreferenceString(context, Constants.USEREMAIL_STRING, GeneralUtils.encrypt(userLogin.getEmail()));
        writeSharedPreferenceString(context, Constants.USERPASSWORD_STRING, GeneralUtils.encrypt(userLogin.getPassword()));

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


    public static void storeActiveUser(Context context, UserResult userResult)  {

        try {
            writeSharedPreferenceString(context, Constants.CURRENT_USER, new ObjectMapper().writeValueAsString(userResult.getUser()));
            writeSharedPreferenceString(context, Constants.ACCESS_TOKEN_STRING, userResult.getTokenInfo().getAccessToken());
        }catch (Exception e){
            Timber.e(e);
        }

    }

    public static UserResult getActiveUser(Context context)  {
try{
        UserResult userResult = new UserResult();
        String user = TempStorageUtils.readSharedPreferenceString(context, CURRENT_USER);
        String accessToken = TempStorageUtils.readSharedPreferenceString(context, ACCESS_TOKEN_STRING);
        userResult.setUser(new ObjectMapper().readValue(user, User.class));
        userResult.setTokenInfo(new TokenInfo(accessToken));
        return userResult;
    }catch (Exception e){
        Timber.e(e);
    }
    return null;
    }

    public static void removeActiveUser(Context context) {

        TempStorageUtils.removeSharedPreference(context, CURRENT_USER);
        TempStorageUtils.removeSharedPreference(context, ACCESS_TOKEN_STRING);

    }





}
