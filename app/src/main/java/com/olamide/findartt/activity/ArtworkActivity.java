package com.olamide.findartt.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.olamide.findartt.R;
import com.olamide.findartt.enums.ConnectionStatus;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.User;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.FindArttService;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.olamide.findartt.Constants.ACCESS_TOKEN_STRING;
import static com.olamide.findartt.Constants.ARTWORK_STRING;
import static com.olamide.findartt.Constants.CURRENT_USER;
import static com.olamide.findartt.utils.network.ConnectionUtils.getConnectionStatus;

public class ArtworkActivity extends AppCompatActivity {

    private String accessToken;
    private User user;
    private Artwork artwork;
    private ArtworkSummary artworkSummary;


    private Call<FindArttResponse<ArtworkSummary>> responseCall;

    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;

    @BindView(R.id.tv_art_name)
    TextView tvArtName;

    @BindView(R.id.iv_art)
    ImageView ivArt;

    @BindView(R.id.pv_art)
    SimpleExoPlayerView pvArt;

    @BindView(R.id.tv_date)
    TextView tvDate;

    @BindView(R.id.tv_description)
    TextView tv_description;

    @BindView(R.id.detail_frame)
    FrameLayout detailFrame;

    @BindView(R.id.player_frame)
    FrameLayout playerFrame;

    @BindView(R.id.exo_fullscreen_button)
    FrameLayout exoFullscreenButton;

    @BindView(R.id.exo_fullscreen_icon)
    ImageView mFullScreenIcon;

//    @BindView(R.id.cl_root)
//    CoordinatorLayout clRoot;
//



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork);

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        accessToken = TempStorageUtils.readSharedPreferenceString(getApplicationContext(), ACCESS_TOKEN_STRING);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            artwork = extras.getParcelable(ARTWORK_STRING);
            user = extras.getParcelable(CURRENT_USER);
        }

        getArtSummary();


    }


    void getArtSummary() {
        ConnectionStatus connectionStatus = getConnectionStatus(getApplicationContext());
        if (connectionStatus.connectionStatus.equals(ConnectionStatus.NONE)) {
            ErrorUtils.handleInternetError(this, clRoot);

            return;
        }


        responseCall = FindArttService.getArtSummary(accessToken, artwork.getId());
        responseCall.enqueue(new Callback<FindArttResponse<ArtworkSummary>>() {

            @Override
            public void onResponse(Call<FindArttResponse<ArtworkSummary>> call, Response<FindArttResponse<ArtworkSummary>> response) {


                if (response.body() != null) {
                    FindArttResponse<ArtworkSummary> arttResponse = response.body();
                     artworkSummary = arttResponse.getData();
                    artworkSummary.getBids();

                    displayUi();

                } else {
                    ErrorUtils.handleApiError(response.errorBody(), getApplicationContext(), clRoot);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<ArtworkSummary>> call, Throwable t) {
                ErrorUtils.handleInternetError(getApplicationContext(), clRoot);
                Timber.e(t);
            }
        });


    }



    void displayUi(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-MMMM-yyyy", Locale.ENGLISH);
//        signup.setDateOfBirth(simpleDateFormat.format(new Date()));
        tvArtName.setText(artworkSummary.getName());
//        tvDate.setText(simpleDateFormat.format(artworkSummary.getCreatedDate()));
        tv_description.setText(artworkSummary.getDescription());

        Picasso.with(this)
                .load(artworkSummary.getImageUrl())
                .fit()
                .placeholder(R.drawable.img_load)
                .error(R.drawable.img_error)
                .into(ivArt);

        if(artworkSummary.getVideoUrl()!=null&& !artworkSummary.getVideoUrl().isEmpty()){
            UiUtils.showSuccessSnack("this one has video",this,clRoot);
        }


    }

}
