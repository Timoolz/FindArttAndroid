package com.olamide.findartt.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.olamide.findartt.R;
import com.squareup.picasso.Picasso;

public class UiUtils {



    public static void loadAvatarView(String imageUrl, ImageView imageView, Context context){
        Picasso.with(context)
                .load(imageUrl)
                .transform(ImageUtils.defaultAvatarTransformation(imageView))
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_avatar)
                .fit()
                .into(imageView);

    }


    public static ProgressDialog getProgressDialog(Context context, String msg, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(cancelable);
        return progressDialog;
    }


    public static ViewGroup getDummyFrame(Activity activity) {

        return activity.findViewById(android.R.id.content);
    }


    public static void showSuccessSnack(String message, Context context, ViewGroup viewGroup) {
        int color = ContextCompat.getColor(context, R.color.success);
        displaySnackBar(message, viewGroup, color, Snackbar.LENGTH_LONG, true);
    }


    public static void showErrorSnack(String message, Context context, ViewGroup viewGroup) {
        int color = ContextCompat.getColor(context, R.color.failure);
        UiUtils.displaySnackBar(message, viewGroup, color, Snackbar.LENGTH_LONG, true);
    }

    public static void displaySnackBar(String message, ViewGroup viewGroup, @ColorInt int color, int length, boolean top) {

        Snackbar snackbar = Snackbar.make(viewGroup, message, length);
        snackbar.getView().setBackgroundColor(color);
        //make snackbar appear at the top
        if (top) {

            if (viewGroup instanceof FrameLayout) {
                View view = snackbar.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                params.gravity = Gravity.BOTTOM;
                view.setLayoutParams(params);
            }
            if (viewGroup instanceof CoordinatorLayout) {
                View view = snackbar.getView();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
            }
        }
        if (length == Snackbar.LENGTH_INDEFINITE) {
            snackbar.setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.getView().setOnClickListener(v -> snackbar.dismiss());
        }
        snackbar.show();

    }

}
