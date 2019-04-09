package com.olamide.findartt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.olamide.findartt.R;
import com.olamide.findartt.activity.SignInActivity;
import com.olamide.findartt.enums.ErrorCode;
import com.olamide.findartt.models.api.FindArttResponse;

import okhttp3.ResponseBody;
import timber.log.Timber;


public class ErrorUtils {


    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int INTERNAL_SERVER_ERROR = 500;


    public static void handleError(Context context, ViewGroup viewGroup) {
        String error = context.getString(R.string.error);

        UiUtils.showErrorSnack(error, context, viewGroup);
    }

    public static void handleApiError(ResponseBody responseBody, Context context, ViewGroup viewGroup) {
        Gson gson = new GsonBuilder().create();
        FindArttResponse findArttResponse = new FindArttResponse();
        try {
            findArttResponse = gson.fromJson(responseBody.string(), FindArttResponse.class);

            if (findArttResponse.getCode() == INTERNAL_SERVER_ERROR) {
                handleError(context, viewGroup);
            } else if (findArttResponse.getErrorCode().equals(ErrorCode.TOKEN_NOT_FOUND)) {
                handleError(context, viewGroup);
                new AuthUtil(context).logout();
            } else {
                //Toast.makeText(context, findArttResponse.getMessage(), Toast.LENGTH_LONG).show();
                UiUtils.showErrorSnack(findArttResponse.getMessage(), context, viewGroup);
            }
            //Toast.makeText(context, findArttResponse.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // handle failure to read error
            handleError(context, viewGroup);
            Timber.e(e);
        }

    }

    public static void handleUserError(String message, Context context, ViewGroup viewGroup) {
        String error = context.getString(R.string.error);
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        UiUtils.showErrorSnack(message, context, viewGroup);
    }


    public static void handleInternetError(Activity activity, ViewGroup viewGroup) {
        String error = activity.getString(R.string.internet_error);
        //Toast.makeText(context, error, Toast.LENGTH_LONG).show();

        UiUtils.showErrorSnack(error, activity, viewGroup);
    }


    public static void handleThrowable(Throwable throwable, Context context, ViewGroup viewGroup) {

        try {

            if (throwable instanceof HttpException) {
                HttpException exception = objectMapper.convertValue(throwable, HttpException.class);
                ResponseBody responseBody = exception.response().errorBody();
                ErrorUtils.handleApiError(responseBody, context, viewGroup);
            } else if (throwable instanceof retrofit2.HttpException) {
                retrofit2.HttpException exception = objectMapper.convertValue(throwable, retrofit2.HttpException.class);
                ResponseBody responseBody = exception.response().errorBody();
                ErrorUtils.handleApiError(responseBody, context, viewGroup);
            } else {
                ErrorUtils.handleError(context, viewGroup);
            }

        } catch (Exception e) {
            Timber.e(e);
            ErrorUtils.handleError(context, viewGroup);
        }


    }

}
