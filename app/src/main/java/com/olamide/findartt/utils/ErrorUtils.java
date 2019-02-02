package com.olamide.findartt.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.olamide.findartt.R;
import com.olamide.findartt.models.FindArttResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import timber.log.Timber;

public class ErrorUtils {

    private static final int INTERNAL_SERVER_ERROR = 500;

    public static void handleError( Context context, CoordinatorLayout coordinatorLayout){
        String error = context.getString(R.string.error);
//        Toast.makeText(context, error, Toast.LENGTH_LONG).show();

        showErrorSnack(error,context,coordinatorLayout);
    }

    public static void handleApiError(ResponseBody responseBody, Context context, CoordinatorLayout coordinatorLayout){
        Gson gson = new GsonBuilder().create();
        FindArttResponse findArttResponse = new FindArttResponse();
        try {
            findArttResponse= gson.fromJson(responseBody.string(),FindArttResponse.class);

            if(findArttResponse.getCode()==INTERNAL_SERVER_ERROR){
                handleError(context, coordinatorLayout);
            }else {
                //Toast.makeText(context, findArttResponse.getMessage(), Toast.LENGTH_LONG).show();
                showErrorSnack(findArttResponse.getMessage(),context,coordinatorLayout);
            }
            //Toast.makeText(context, findArttResponse.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // handle failure to read error
            Timber.e(e);
        }

    }

    public static void handleUserError(String message, Context context, CoordinatorLayout coordinatorLayout){
        String error = context.getString(R.string.error);
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        showErrorSnack(error,context,coordinatorLayout);
    }



    public static void handleInternetError( Context context, CoordinatorLayout coordinatorLayout){
        String error = context.getString(R.string.internet_error);
        //Toast.makeText(context, error, Toast.LENGTH_LONG).show();

        showErrorSnack(error,context,coordinatorLayout);
    }

    public static void showErrorSnack(String message, Context context, CoordinatorLayout coordinatorLayout){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context,R.color.failure));
        //make snackbar appear at the top
        View view = snackbar.getView();
        CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.show();
    }

}