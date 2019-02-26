package com.olamide.findartt.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.olamide.findartt.R;
import com.olamide.findartt.models.Artwork;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class ArttWidget extends AppWidgetProvider {

    public static List<Artwork> mArtworks;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetIds[], List<Artwork> artworks) {

        mArtworks = artworks;
        for (int appWidgetId : appWidgetIds)
        {
            Intent intent = new Intent(context, arttViewsService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.artt_widget);
            views.setRemoteAdapter(R.id.lv_artt, intent);
            ComponentName component = new ComponentName(context, ArttWidget.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_artt);
            appWidgetManager.updateAppWidget(component, views);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

