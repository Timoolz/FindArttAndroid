package com.olamide.findartt.utils;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import timber.log.Timber;

public class RecyclerViewUtils {





    public static int getSpanCount(final RecyclerView recyclerView, final float cardWidth ){

        WindowManager wm = (WindowManager) recyclerView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        int newSpanCount = (int) Math.floor(width / cardWidth);

        Timber.i( "width  %s", width);
        Timber.i( "cardWidth  %s", cardWidth);
        Timber.i( "Spancount  %s", newSpanCount);

        return newSpanCount;
    }



}
