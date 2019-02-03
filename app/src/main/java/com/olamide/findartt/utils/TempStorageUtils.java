package com.olamide.findartt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.olamide.findartt.Constants;
import com.olamide.findartt.models.UserLogin;

import static com.olamide.findartt.Constants.USEREMAIL_STRING;
import static com.olamide.findartt.Constants.USERPASSWORD_STRING;

public class TempStorageUtils {


    protected final static int DEFAULT_INT = 0;
    protected final static String DEFAULT_STRING = "";
    static int tempInt = 0;
    static String tempString = "";

    public static int readSharedPreferenceInt(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        tempInt = preferences.getInt(key,DEFAULT_INT);
        return tempInt;
    }

    public static String readSharedPreferenceString(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        tempString = preferences.getString(key,DEFAULT_STRING);
        return tempString;
    }

    public static void writeSharedPreferenceString(Context context,String key,String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void writeSharedPreferenceInt(Context context,String key,int value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public static void storeUserLogin(Context context, UserLogin userLogin){
        writeSharedPreferenceString(context, Constants.USEREMAIL_STRING,GeneralUtils.encrypt(userLogin.getEmail()));
        writeSharedPreferenceString(context,Constants.USERPASSWORD_STRING,GeneralUtils.encrypt(userLogin.getPassword()));

    }

    public static UserLogin getUserLogin(Context context){

        UserLogin userLogin = new UserLogin();
        String userEmail = TempStorageUtils.readSharedPreferenceString(context, USEREMAIL_STRING);
        String userPass = TempStorageUtils.readSharedPreferenceString(context,USERPASSWORD_STRING);
        userLogin.setEmail(GeneralUtils.decrypt(userEmail));
        userLogin.setPassword(GeneralUtils.decrypt(userPass));
        return userLogin;
    }

}
