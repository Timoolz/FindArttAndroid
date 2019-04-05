package com.olamide.findartt.utils;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.olamide.findartt.Constants;
import com.olamide.findartt.R;
import com.olamide.findartt.activity.SignInActivity;
import com.olamide.findartt.enums.ErrorCode;
import com.olamide.findartt.models.FindArttResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import timber.log.Timber;

public class ErrorUtils {



    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int INTERNAL_SERVER_ERROR = 500;


    public static void showErrorSnack(String message, Context context, View view){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context,R.color.failure));
        //make snackbar appear at the top
        view = snackbar.getView();
        CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.show();
    }


    public static void handleError( Context context, View view){
        String error = context.getString(R.string.error);

        showErrorSnack(error,context,view);
    }

    public static void handleApiError(ResponseBody responseBody, Context context, View view){
        Gson gson = new GsonBuilder().create();
        FindArttResponse findArttResponse = new FindArttResponse();
        try {
            findArttResponse= gson.fromJson(responseBody.string(),FindArttResponse.class);

            if(findArttResponse.getCode()==INTERNAL_SERVER_ERROR){
                handleError(context, view);
            }else if(findArttResponse.getErrorCode().equals(ErrorCode.TOKEN_NOT_FOUND)){
                handleError(context, view);
                //remove current Active User
                TempStorageUtils.removeActiveUser(context);
                //re direct to sign in activity
                Intent intent = new Intent(context, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else {
                //Toast.makeText(context, findArttResponse.getMessage(), Toast.LENGTH_LONG).show();
                showErrorSnack(findArttResponse.getMessage(),context,view);
            }
            //Toast.makeText(context, findArttResponse.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // handle failure to read error
            handleError(context,view);
            Timber.e(e);
        }

    }

    public static void handleUserError(String message, Context context, View view){
        String error = context.getString(R.string.error);
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        showErrorSnack(message,context,view);
    }



    public static void handleInternetError( Context context, View view){
        String error = context.getString(R.string.internet_error);
        //Toast.makeText(context, error, Toast.LENGTH_LONG).show();

        showErrorSnack(error,context,view);
    }



    public static void handleThrowable(Throwable throwable, Context context, View view){

        try {

            if(throwable instanceof HttpException){
                HttpException exception = objectMapper.convertValue(throwable,HttpException.class);
                ResponseBody responseBody = exception.response().errorBody();
                ErrorUtils.handleApiError(responseBody,context, view);
            }else if(throwable instanceof retrofit2.HttpException){
                retrofit2.HttpException exception = objectMapper.convertValue(throwable,retrofit2.HttpException.class);
                ResponseBody responseBody = exception.response().errorBody();
                ErrorUtils.handleApiError(responseBody,context, view);
            }else{
                ErrorUtils.handleError(context, view);
            }

        }catch (Exception e){
            Timber.e(e);
            ErrorUtils.handleError(context, view);
        }



    }

}
