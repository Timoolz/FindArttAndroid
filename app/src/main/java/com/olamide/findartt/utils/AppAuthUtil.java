package com.olamide.findartt.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.olamide.findartt.FindArttApplication;
import com.olamide.findartt.activity.SignInActivity;
import com.olamide.findartt.models.UserResult;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;

public class AppAuthUtil {

    private Application application;
    private Context context;

    @Inject
    public AppAuthUtil(Application application) {
        this.application = application;
    }

    public AppAuthUtil(Context context) {

        this.context = context;
    }

    public @Nullable UserResult authorize(){
        UserResult userResult = TempStorageUtils.getActiveUser(application);
        if (userResult == null) {
            logout();
            return null;
        }
        return userResult;
    }

    public void logout() {

        if(context!=null){
            //remove current Active User
            TempStorageUtils.removeActiveUser(context);
            //re direct to sign in activity
            Intent intent = new Intent(context, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else {
            //remove current Active User
            TempStorageUtils.removeActiveUser(application);
            //re direct to sign in activity
            Intent intent = new Intent(application, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            application.startActivity(intent);
        }

    }

}