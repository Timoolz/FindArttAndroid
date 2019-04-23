package com.olamide.findartt.activity;

import android.app.Dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.olamide.findartt.ArtworkNewViewModel;
import com.olamide.findartt.R;
import com.olamide.findartt.ViewModelFactory;
//import com.olamide.findartt.fragment.BidFragment;
//import com.olamide.findartt.fragment.BuyFragment;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.AppAuthUtil;
import com.olamide.findartt.utils.Converters;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.GeneralUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;
//import com.olamide.findartt.viewmodels.ArtworkViewModel;
//import com.olamide.findartt.viewmodels.ArtworkViewModelFactory;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import timber.log.Timber;

import static com.olamide.findartt.AppConstants.ARTWORK_STRING;


public class ArtworkActivity extends AppCompatActivity implements Player.EventListener {

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    AppAuthUtil appAuthUtil;
    @Inject
    ConnectionUtils connectionUtils;

    private final ObjectMapper objectMapper = new ObjectMapper();

    ArtworkNewViewModel artworkNewViewModel;

    private UserResult userResult;
    private Artwork artwork;
    private ArtworkSummary artworkSummary;

    private boolean favouriteArt = false;
//    private FindArttDatabase mDb;


    private Uri videoUri;

    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";

    private boolean startAutoPlay;
    private int startWindow;
    private long startPosition;

    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen = false;


    private SimpleExoPlayer mExoPlayer;

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;


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

    @BindView(R.id.bt_favourite)
    ImageButton btFavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork);
        ButterKnife.bind(this);
        userResult = appAuthUtil.authorize();

        //mDb = FindArttDatabase.getInstance(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            artwork = extras.getParcelable(ARTWORK_STRING);
        }

        artworkNewViewModel = ViewModelProviders.of(this, viewModelFactory).get(ArtworkNewViewModel.class);
        artworkNewViewModel.getArtWorkResponse().observe(this, this::showUi);
        artworkNewViewModel.getArtWorkFavourite().observe(this, this::showFavUi);

        if (savedInstanceState == null) {
            getArtSummary();
        }
        if (savedInstanceState != null) {
            getSavedStartPosition(savedInstanceState);
        }


    }

    private void showFavUi(MVResponse mvResponse) {
        switch (mvResponse.status) {
            case LOADING:

                break;
            case SUCCESS:

                try {
                    Artwork favArtwork = objectMapper.convertValue(mvResponse.data, Artwork.class);
                    if (favArtwork != null) {
                        favouriteArt = true;
                        btFavourite.setImageResource(R.drawable.favourite);
                    }else{
                        favouriteArt = false;
                        btFavourite.setImageResource(R.drawable.not_favourite2);
                    }


                } catch (Exception e) {
                    Timber.e(e);
                    favouriteArt = false;
                    btFavourite.setImageResource(R.drawable.not_favourite2);
                    //ErrorUtils.handleError((this), clRoot);
                }
                break;

            case ERROR:

                favouriteArt = false;
                btFavourite.setImageResource(R.drawable.not_favourite2);

                break;

            default:
                break;
        }
    }

    private void showUi(MVResponse mvResponse) {

        switch (mvResponse.status) {

            case LOADING:

                break;
            case SUCCESS:
//                displayUi();
                FindArttResponse arttResponse = new FindArttResponse();
                try {
                    arttResponse = objectMapper.convertValue(mvResponse.data, FindArttResponse.class);
                    artworkSummary = objectMapper.convertValue(arttResponse.getData(), ArtworkSummary.class);
                    displayUi();

                } catch (Exception e) {
                    Timber.e(e);
                    ErrorUtils.handleError((this), clRoot);
                }
                break;

            case ERROR:
                ErrorUtils.handleThrowable(mvResponse.error, this, clRoot);
                break;

            default:
                break;
        }

    }


    @OnClick(R.id.bt_favourite)
    public void setFavourite() {

        if (favouriteArt) {
            artworkNewViewModel.deleteFavouriteArt(artworkSummary);
//            btFavourite.setImageResource(R.drawable.not_favourite2);
        }else {
            artworkNewViewModel.insertFavouriteArt(artworkSummary);
//            btFavourite.setImageResource(R.drawable.favourite);
        }

    }



    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);


        updateStartPosition();
        savedInstanceState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
        savedInstanceState.putInt(KEY_WINDOW, startWindow);
        savedInstanceState.putLong(KEY_POSITION, startPosition);

    }

    void getArtSummary() {
        if (connectionUtils.handleNoInternet(this)) {
            artworkNewViewModel.findArtSummary(userResult.getTokenInfo().getAccessToken(), artwork.getId());
        }
        artworkNewViewModel.findArtFavourite(artwork.getId());
    }


    void displayUi() {
//        checkFavourite();
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM-d-yyyy", Locale.ENGLISH);
        Date date = Converters.toDate(artworkSummary.getCreatedDateEpoch());
        tvArtName.setText(artworkSummary.getName());
        tvDate.setText(outputFormat.format(date));
        tv_description.setText(artworkSummary.getDescription());

        Picasso.with(this)
                .load(artworkSummary.getImageUrl())
                .fit()
                .placeholder(R.drawable.img_load)
                .error(R.drawable.img_error)
                .into(ivArt);

        if (artworkSummary.getVideoUrl() != null && !artworkSummary.getVideoUrl().isEmpty()) {
            UiUtils.showSuccessSnack("this one has video", this, clRoot);
            loadVideo();
        }

//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(ARTWORK_STRING, artworkSummary);
//        bundle.putParcelable(CURRENT_USER, user);
//        if (artworkSummary.getPurchaseType().equals(PurchaseType.BID)) {
//            BidFragment bidFragment = new BidFragment();
//            bidFragment.setArguments(bundle);
//            fragmentManager.beginTransaction()
//                    .add(R.id.detail_frame, bidFragment)
//                    .commit();
//        } else {
//            BuyFragment buyFragment = new BuyFragment();
//            buyFragment.setArguments(bundle);
//            fragmentManager.beginTransaction()
//                    .add(R.id.detail_frame, buyFragment)
//                    .commit();
//        }

    }


    void loadVideo() {
        initFullscreenDialog();
        pvArt.setVisibility(View.VISIBLE);
        initializeMediaSession();
        videoUri = Uri.parse(artworkSummary.getVideoUrl());
        initializePlayer(videoUri);
        if (!GeneralUtils.isTablet(getApplicationContext()) && GeneralUtils.isLand(getApplicationContext())) {
            openFullscreenDialog();
        }
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, getLocalClassName().getClass().getSimpleName());

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            //LoadControl loadControl = new DefaultLoadControl();
            //mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            pvArt.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, getLocalClassName().getClass().getSimpleName());
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);

            boolean haveStartPosition = startWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                mExoPlayer.seekTo(startWindow, startPosition);
            }

            mExoPlayer.prepare(mediaSource, !haveStartPosition, false);
            mExoPlayer.setPlayWhenReady(startAutoPlay);


        }
    }


    private void releasePlayer() {
        clearStartPosition();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            updateStartPosition();

        }

    }

    private void releasePlayerPartially() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
            updateStartPosition();

        }

    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        ((ViewGroup) pvArt.getParent()).removeView(pvArt);
        mFullScreenDialog.addContentView(pvArt, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_shrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) pvArt.getParent()).removeView(pvArt);
        playerFrame.addView(pvArt);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_expand));
    }

    @OnClick(R.id.exo_fullscreen_button)
    void processFullScreen() {
        if (!mExoPlayerFullscreen)
            openFullscreenDialog();
        else
            closeFullscreenDialog();

    }


    @Override
    public void onResume() {
        super.onResume();

        if (videoUri != null) {
            initializePlayer(videoUri);

        }

    }


    private void updateStartPosition() {
        if (mExoPlayer != null) {
            startAutoPlay = mExoPlayer.getPlayWhenReady();
            startWindow = mExoPlayer.getCurrentWindowIndex();
            startPosition = Math.max(0, mExoPlayer.getContentPosition());
        }
    }

    private void clearStartPosition() {
        startAutoPlay = true;
        startWindow = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    private void getSavedStartPosition(Bundle savedInstanceState) {

        //trackSelectorParameters = savedInstanceState.getParcelable(KEY_TRACK_SELECTOR_PARAMETERS);
        startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
        startWindow = savedInstanceState.getInt(KEY_WINDOW);
        startPosition = savedInstanceState.getLong(KEY_POSITION);
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());


    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

        if (isBehindLiveWindow(error)) {
            clearStartPosition();
            initializePlayer(videoUri);
        } else {
            updateStartPosition();

        }

    }

    @Override
    public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {

//        if (mExoPlayer.get != null) {
//            // The user has performed a seek whilst in the error state. Update the resume position so
//            // that if the user then retries, playback resumes from the position to which they seeked.
//            updateStartPosition();
//        }

    }


    @Override
    public void onStop() {
        super.onStop();
        releasePlayerPartially();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }


    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }


}
