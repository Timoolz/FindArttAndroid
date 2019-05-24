package com.olamide.findartt.utils;

import android.app.Activity;
import android.content.Intent;

import com.olamide.findartt.ui.activity.DashboardActivity;

public class NavigationUtils {

    public static void goToDashboard(Activity activity) {
        Intent intent = new Intent(activity, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }
}
