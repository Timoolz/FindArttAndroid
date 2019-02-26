package com.olamide.findartt.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.olamide.findartt.R;
import com.olamide.findartt.models.Artwork;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public class arttViewsService extends RemoteViewsService {

    /**
     * @param intent intent that triggered this service
     * @return new ListViewsFactory Object with the appropriate implementation
     */
    public ArttViewsFactory onGetViewFactory(Intent intent) {
        return new ArttViewsFactory(this.getApplicationContext());
    }
}


class ArttViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    private Context mContext;
    private List<Artwork> mArtts;

    public ArttViewsFactory(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mArtts = ArttWidget.mArtworks;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mArtts == null)
            return 0;
        return mArtts.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.artt_widget_item);

        try {
            Bitmap b = Picasso.with(mContext).load(mArtts.get(i).getImageUrl())
                    .placeholder(R.drawable.img_load)
                    .error(R.drawable.img_error).get();
            views.setImageViewBitmap(R.id.iv_art, Bitmap.createScaledBitmap(b, 1000, 1000, false));
        } catch (Exception e) {
            Timber.e(e);
            e.printStackTrace();

        }
        views.setTextViewText(R.id.tv_title, mArtts.get(i).getName());
        views.setTextViewText(R.id.tv_description, mArtts.get(i).getDescription());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

