package com.olamide.findartt.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.olamide.findartt.Constants;
import com.olamide.findartt.models.Artwork;

import java.util.List;

public class ArttUpdateService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    private List<Artwork> mArtworks;
    public static final String ACTION_UPDATE_ARTT_WIDGET = "com.olamide.findartt.widget.action.update_artt_widget";



    public ArttUpdateService() {
        super("ArttUpdateService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction().equals(ACTION_UPDATE_ARTT_WIDGET))
        {
            Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
            mArtworks = bundle.getParcelableArrayList(Constants.ARTWORK_STRING);


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ArttWidget.class));
            ArttWidget.updateAppWidget(this, appWidgetManager, appWidgetIds,mArtworks);
        }
    }


}
