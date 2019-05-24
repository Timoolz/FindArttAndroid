//package com.olamide.findartt.ui.activity;
//
//import android.app.Dialog;
//
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//
//import androidx.fragment.app.FragmentManager;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.media.session.MediaButtonReceiver;
//
//import android.support.v4.media.session.MediaSessionCompat;
//import android.support.v4.media.session.PlaybackStateCompat;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.android.exoplayer2.C;
//import com.google.android.exoplayer2.ExoPlaybackException;
//import com.google.android.exoplayer2.Player;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//
//import com.google.android.exoplayer2.source.BehindLiveWindowException;
//
//import com.google.android.exoplayer2.ui.PlayerView;
//
//import com.olamide.findartt.VideoViewModel;
//import com.olamide.findartt.enums.PurchaseType;
//import com.olamide.findartt.ui.fragment.BidFragment;
//import com.olamide.findartt.ui.fragment.BuyFragment;
//import com.olamide.findartt.utils.exo.ExoUtil;
//import com.olamide.findartt.utils.exo.ExoUtilFactory;
//import com.olamide.findartt.viewmodels.ArtworkViewModel;
//import com.olamide.findartt.R;
//import com.olamide.findartt.ViewModelFactory;
//import com.olamide.findartt.models.Artwork;
//import com.olamide.findartt.models.ArtworkSummary;
//import com.olamide.findartt.models.UserResult;
//import com.olamide.findartt.models.api.FindArttResponse;
//import com.olamide.findartt.models.mvvm.MVResponse;
//import com.olamide.findartt.utils.AppAuthUtil;
//import com.olamide.findartt.utils.Converters;
//import com.olamide.findartt.utils.ErrorUtils;
//import com.olamide.findartt.utils.UiUtils;
//import com.olamide.findartt.utils.network.ConnectionUtils;
//
//import com.squareup.picasso.Picasso;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import dagger.Lazy;
//import dagger.android.AndroidInjection;
//import timber.log.Timber;
//
//import static com.olamide.findartt.AppConstants.ARTWORK_STRING;
//import static com.olamide.findartt.AppConstants.CURRENT_USER;
//
//
//public class ArtworkActivity extends AppCompatActivity implements ExoUtil.PlayerStateListener {
//
//    @Inject
//    ViewModelFactory viewModelFactory;
//
//    private ExoUtil exoUtil;
//
//    @Inject
//    Lazy<ExoUtilFactory> exoUtilFactory;
//
//    @Inject
//    AppAuthUtil appAuthUtil;
//    @Inject
//    ConnectionUtils connectionUtils;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    ArtworkViewModel artworkViewModel;
//    VideoViewModel videoViewModel;
//
//    private UserResult userResult;
//    private Artwork artwork;
//    private ArtworkSummary artworkSummary;
//
//    private boolean favouriteArt = false;
//
//    private static final String KEY_WINDOW = "window";
//    private static final String KEY_POSITION = "position";
//    private static final String KEY_AUTO_PLAY = "auto_play";
//
//    private boolean startAutoPlay;
//    private int startWindow;
//    private long startPosition;
//
//    private Dialog mFullScreenDialog;
//    private boolean mExoPlayerFullscreen = false;
//
//
//    private SimpleExoPlayer mExoPlayer;
//
//    private static MediaSessionCompat mMediaSession;
//    private PlaybackStateCompat.Builder mStateBuilder;
//    ProgressDialog progressDialog;
//
//
//    @BindView(R.id.cl_root)
//    CoordinatorLayout clRoot;
//
//    @BindView(R.id.tv_art_name)
//    TextView tvArtName;
//
//    @BindView(R.id.iv_art)
//    ImageView ivArt;
//
//    @BindView(R.id.pv_art)
//    PlayerView pvArt;
//
//    @BindView(R.id.tv_date)
//    TextView tvDate;
//
//    @BindView(R.id.tv_description)
//    TextView tv_description;
//
//    @BindView(R.id.detail_frame)
//    FrameLayout detailFrame;
//
//    @BindView(R.id.player_frame)
//    FrameLayout playerFrame;
//
//    @BindView(R.id.exo_fullscreen_button)
//    FrameLayout exoFullscreenButton;
//
//    @BindView(R.id.exo_fullscreen_icon)
//    ImageView mFullScreenIcon;
//
//    @BindView(R.id.bt_favourite)
//    ImageButton btFavourite;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        AndroidInjection.inject(this);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_artwork);
//        ButterKnife.bind(this);
//        userResult = appAuthUtil.authorize();
//
//        progressDialog = UiUtils.getProgressDialog(this, getString(R.string.loading), false);
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            artwork = extras.getParcelable(ARTWORK_STRING);
//        }
//
//        artworkViewModel = ViewModelProviders.of(this, viewModelFactory).get(ArtworkViewModel.class);
//        artworkViewModel.getArtWorkResponse().observe(this, this::showUi);
//        artworkViewModel.getArtWorkFavourite().observe(this, this::showFavUi);
//
//
//        if (savedInstanceState == null) {
//            getArtSummary();
//        }
////        if (savedInstanceState != null) {
////            getSavedStartPosition(savedInstanceState);
////        }
//
//
//    }
//
//    private void showFavUi(MVResponse mvResponse) {
//        switch (mvResponse.status) {
//            case LOADING:
//
//                break;
//            case SUCCESS:
//
//                try {
//                    Artwork favArtwork = objectMapper.convertValue(mvResponse.data, Artwork.class);
//                    if (favArtwork != null) {
//                        favouriteArt = true;
//                        btFavourite.setImageResource(R.drawable.favourite);
//                    } else {
//                        favouriteArt = false;
//                        btFavourite.setImageResource(R.drawable.not_favourite2);
//                    }
//
//
//                } catch (Exception e) {
//                    Timber.e(e);
//                    favouriteArt = false;
//                    btFavourite.setImageResource(R.drawable.not_favourite2);
//                    //ErrorUtils.handleError((this), clRoot);
//                }
//                break;
//
//            case ERROR:
//
//                favouriteArt = false;
//                btFavourite.setImageResource(R.drawable.not_favourite2);
//
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    private void showUi(MVResponse mvResponse) {
//
//        switch (mvResponse.status) {
//
//            case LOADING:
//                progressDialog.show();
//                break;
//            case SUCCESS:
//                progressDialog.dismiss();
//                FindArttResponse arttResponse = new FindArttResponse();
//                try {
//                    arttResponse = objectMapper.convertValue(mvResponse.data, FindArttResponse.class);
//                    artworkSummary = objectMapper.convertValue(arttResponse.getData(), ArtworkSummary.class);
//                    displayUi();
//
//                } catch (Exception e) {
//                    Timber.e(e);
//                    ErrorUtils.handleError((this), clRoot);
//                }
//                break;
//
//            case ERROR:
//                progressDialog.dismiss();
//                ErrorUtils.handleThrowable(mvResponse.error, this, clRoot);
//                break;
//
//            default:
//                break;
//        }
//
//    }
//
//
//    @OnClick(R.id.bt_favourite)
//    public void setFavourite() {
//
//        if (favouriteArt) {
//            artworkViewModel.deleteFavouriteArt(artworkSummary);
//        } else {
//            artworkViewModel.insertFavouriteArt(artworkSummary);
//        }
//
//    }
//
//
//    void getArtSummary() {
//        if (connectionUtils.handleNoInternet(this)) {
//            artworkViewModel.findArtSummary(userResult.getTokenInfo().getAccessToken(), artwork.getId());
//        }
//        artworkViewModel.findArtFavourite(artwork.getId());
//    }
//
//
//    void displayUi() {
//        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM-d-yyyy", Locale.ENGLISH);
//        Date date = Converters.toDate(artworkSummary.getCreatedDateEpoch());
//        tvArtName.setText(artworkSummary.getName());
//        tvDate.setText(outputFormat.format(date));
//        tv_description.setText(artworkSummary.getDescription());
//
//        Picasso.with(this)
//                .load(artworkSummary.getImageUrl())
//                .fit()
//                .placeholder(R.drawable.img_load)
//                .error(R.drawable.img_error)
//                .into(ivArt);
//
//        if (artworkSummary.getVideoUrl() != null && !artworkSummary.getVideoUrl().isEmpty()) {
////            UiUtils.showSuccessSnack("this one has video", this, clRoot);
//            loadVideo();
//        }
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(ARTWORK_STRING, artworkSummary);
//        bundle.putParcelable(CURRENT_USER, userResult);
//        if (artworkSummary.getPurchaseType().equals(PurchaseType.BID)) {
//            BidFragment bidFragment = new BidFragment();
//            bidFragment.setArguments(bundle);
//            fragmentManager.beginTransaction()
//                    .replace(R.id.detail_frame, bidFragment)
//                    .commit();
//        } else {
//            BuyFragment buyFragment = new BuyFragment();
//            buyFragment.setArguments(bundle);
//            fragmentManager.beginTransaction()
//                    .replace(R.id.detail_frame, buyFragment)
//                    .commit();
//        }
//
//    }
//
//
//    void loadVideo() {
//        playerFrame.setVisibility(View.VISIBLE);
//
//        videoViewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoViewModel.class);
//        exoUtil = exoUtilFactory.get().getExoUtil(this, this, playerFrame, pvArt, mFullScreenIcon, false);
////        exoUtil.setPlayerView(pvArt);
////        exoUtil.setPlayerViewWrapper(playerFrame);
////        exoUtil.setmFullScreenIcon(mFullScreenIcon);
////        exoUtil.setListener(this);
//        videoViewModel.getPlayableContent(artworkSummary.getVideoUrl());
//        videoViewModel.getContent().observe(this, videoUrl -> {
//            exoUtil.setUrl(videoUrl);
//            exoUtil.onStart();
//        });
//
//    }
//
//
//    @Override
//    public void onPlayerError() {
//        //mActivityVideoBinding.pbError.setVisibility(View.GONE);
//    }
//
//    /**
//     * Gets called when video is ready to be played.
//     *
//     * @param playbackState indicates the status of video
//     */
//    @Override
//    public void onPlayerStateChanged(int playbackState) {
//        switch (playbackState) {
//            case Player.STATE_BUFFERING:
//                //      mActivityVideoBinding.pbError.setVisibility(View.VISIBLE);
//                break;
//            case Player.STATE_READY:
//                //    mActivityVideoBinding.pbError.setVisibility(View.GONE);
//                break;
//
//            case Player.STATE_IDLE:
//                //  mActivityVideoBinding.pbError.setVisibility(View.GONE);
//                break;
//        }
//    }
//
//    @OnClick(R.id.exo_fullscreen_button)
//    void processFullScreen() {
//       exoUtil.processFullScreen();
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (exoUtil!= null) exoUtil.onResume();
//
//    }
//
//
//    private void updateStartPosition() {
//        if (mExoPlayer != null) {
//            startAutoPlay = mExoPlayer.getPlayWhenReady();
//            startWindow = mExoPlayer.getCurrentWindowIndex();
//            startPosition = Math.max(0, mExoPlayer.getContentPosition());
//        }
//    }
//
//    private void clearStartPosition() {
//        startAutoPlay = true;
//        startWindow = C.INDEX_UNSET;
//        startPosition = C.TIME_UNSET;
//    }
//
//    private void getSavedStartPosition(Bundle savedInstanceState) {
//
//        //trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
//        startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
//        startWindow = savedInstanceState.getInt(KEY_WINDOW);
//        startPosition = savedInstanceState.getLong(KEY_POSITION);
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (exoUtil!= null) exoUtil.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (exoUtil!= null) exoUtil.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (exoUtil!= null) exoUtil.onStop();
//    }
//
//
//
//
//    private class MySessionCallback extends MediaSessionCompat.Callback {
//        @Override
//        public void onPlay() {
//            mExoPlayer.setPlayWhenReady(true);
//        }
//
//        @Override
//        public void onPause() {
//            mExoPlayer.setPlayWhenReady(false);
//        }
//
//        @Override
//        public void onSkipToPrevious() {
//            mExoPlayer.seekTo(0);
//        }
//    }
//
//
//    public static class MediaReceiver extends BroadcastReceiver {
//
//        public MediaReceiver() {
//        }
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            MediaButtonReceiver.handleIntent(mMediaSession, intent);
//        }
//    }
//
//
//    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
//        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
//            return false;
//        }
//        Throwable cause = e.getSourceException();
//        while (cause != null) {
//            if (cause instanceof BehindLiveWindowException) {
//                return true;
//            }
//            cause = cause.getCause();
//        }
//        return false;
//    }
//
//
//}
