package com.olamide.findartt.utils;

import androidx.room.TypeConverter;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olamide.findartt.enums.PurchaseType;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime toDateTime(Long timestamp) {
        return timestamp == null ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static Long toEpochLong(LocalDateTime date) {
        return date == null ? null : date.toInstant(ZoneOffset.UTC).toEpochMilli();
    }




    @TypeConverter
    public static List<String> stringToList(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String stringListToString(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static PurchaseType stringToPurchaseType(String value){
        return (value!=null ||!value.isEmpty())? PurchaseType.valueOf(value): null;
    }

    @TypeConverter
    public static String purchaseTypeToString(PurchaseType purchaseType){
        return purchaseType.getName();
    }


    @TypeConverter
    public static List<Integer> integerToList(String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String integerListToString(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
