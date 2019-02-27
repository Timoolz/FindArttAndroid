package com.olamide.findartt.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.olamide.findartt.R;
import com.olamide.findartt.activity.ArtworkActivity;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.network.FindArttService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.olamide.findartt.Constants.ACCESS_TOKEN_STRING;
import static com.olamide.findartt.Constants.ARTWORK_STRING;
import static com.olamide.findartt.Constants.CURRENT_USER;

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
    private Call<FindArttResponse<User>> loginResponseCall;

    private User user;


    public ArttViewsFactory(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void onCreate() {
        getUserFromToken(TempStorageUtils.readSharedPreferenceString(mContext,ACCESS_TOKEN_STRING));
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

        Bundle extras = new Bundle();
        extras.putParcelable(ARTWORK_STRING, mArtts.get(i));
        extras.putParcelable(CURRENT_USER, user);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.iv_art, fillInIntent);

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


    void getUserFromToken(String accessToken) {


        loginResponseCall = FindArttService.getUserFromToken(accessToken);
        loginResponseCall.enqueue(new Callback<FindArttResponse<User>>() {

            @Override
            public void onResponse(Call<FindArttResponse<User>> call, Response<FindArttResponse<User>> response) {


                if (response.body() != null) {
                    FindArttResponse<User> arttResponse = response.body();
                     user = arttResponse.getData();

                } else {
                    TempStorageUtils.removeSharedPreference(mContext,ACCESS_TOKEN_STRING);
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<User>> call, Throwable t) {
                Timber.e(t);
            }
        });


    }
}
