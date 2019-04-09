package com.olamide.findartt.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.olamide.findartt.FindArttApplication;
import com.olamide.findartt.activity.SignInActivity;
import com.olamide.findartt.models.UserResult;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;

public class AuthUtil {

    private Context context;


    public AuthUtil(Context context) {
        this.context = context;
    }

    public @Nullable UserResult authorize(){
        UserResult userResult = TempStorageUtils.getActiveUser(context);

        return userResult;
    }

    public void logout() {

        //remove current Active User
        TempStorageUtils.removeActiveUser(context);
        //re direct to sign in activity
        Intent intent = new Intent(context, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
