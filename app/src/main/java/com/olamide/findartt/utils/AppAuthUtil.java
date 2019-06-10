package com.olamide.findartt.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.olamide.findartt.service.LogoutService;
import com.olamide.findartt.ui.activity.SignInActivity;
import com.olamide.findartt.models.UserResult;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;

import static com.olamide.findartt.AppConstants.ACCESS_TOKEN_STRING;

public final class AppAuthUtil {

    private Application application;
    private Context context;

    @Inject
    public AppAuthUtil(Application application) {
        this.application = application;
        this.context = application;
    }

    public AppAuthUtil(Context context) {
        this.context = context;
    }

    public @Nullable
    UserResult authorize() {
        UserResult userResult = TempStorageUtils.getActiveUser(context);
        if (userResult == null) {
            logout();
            return null;
        }
        return userResult;
    }

    public void logout() {
        if (context != null) {
            logoutOnServer();
            //remove current Active User
            TempStorageUtils.removeActiveUser(context);
            //re direct to sign in activity
            Intent intent = new Intent(context, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }


    }

    private void logoutOnServer() {
        UserResult userResult = TempStorageUtils.getActiveUser(context);
        if (userResult != null) {
            Intent i = new Intent(context, LogoutService.class);
            i.putExtra(ACCESS_TOKEN_STRING, userResult.getTokenInfo().getAccessToken());
            i.setAction(LogoutService.ACTION_LOGOUT_FROM_SERVER);
            context.startService(i);
        }

    }

}
