//package com.olamide.findartt.widget;
//
//import android.app.PendingIntent;
//import android.appwidget.AppWidgetManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.widget.RemoteViews;
//
//import com.olamide.findartt.R;
//import com.olamide.findartt.ui.activity.ArtworkActivity;
//import com.olamide.findartt.models.Artwork;
//
//import java.util.List;
//
///**
// * Implementation of App Widget functionality.
// */
//public class ArttWidget extends AppWidgetProvider {
//
//    public static List<Artwork> mArtworks;
//
//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetIds[], List<Artwork> artworks) {
//
//        mArtworks = artworks;
//        for (int appWidgetId : appWidgetIds)
//        {
//            RemoteViews ui = new RemoteViews(context.getPackageName(), R.layout.artt_widget);
//
//            Intent intent = new Intent(context, arttViewsService.class);
//            ui.setRemoteAdapter(R.id.lv_artt, intent);
//
//
//            // Set the artworkACtivity intent to launch when clicked
//            Intent appIntent = new Intent(context, ArtworkActivity.class);
//            //appIntent.putExtra()
//            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            ui.setPendingIntentTemplate(R.id.lv_artt, appPendingIntent);
//
//            ComponentName component = new ComponentName(context, ArttWidget.class);
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_artt);
//            appWidgetManager.updateAppWidget(component, ui);
//        }
//    }
//
//
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        // There may be multiple widgets active, so update all of them
////        for (int appWidgetId : appWidgetIds) {
////            updateAppWidget(context, appWidgetManager, appWidgetId);
////        }
//    }
//
//    @Override
//    public void onEnabled(Context context) {
//        // Enter relevant functionality for when the first widget is created
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        // Enter relevant functionality for when the last widget is disabled
//    }
//}
//
