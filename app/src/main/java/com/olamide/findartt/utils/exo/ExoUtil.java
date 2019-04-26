package com.olamide.findartt.utils.exo;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.olamide.findartt.R;
import com.olamide.findartt.utils.GeneralUtils;
import com.olamide.findartt.utils.TempStorageUtils;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.olamide.findartt.AppConstants.CURRENT_POSITION_KEY;


public class ExoUtil implements PlaybackPreparer, Player.EventListener {

    private Application application;
    private SimpleExoPlayer simpleExoPlayer;
    private Provider<SimpleExoPlayer> exoPlayer;
    private ExtractorMediaSource.Factory mediaFactory;


    private PlayerStateListener mPlayerStateListener;
    private FrameLayout mPlayerViewWrapper;
    private PlayerView mPlayerView;
    private ImageView mFullScreenIcon;
    private Activity rootActivity;


    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen = false;

    private boolean mShouldAutoPlay;
    private String mUrl;


    /**
     * A default constructor that injects dependencies
     *
     * @param exoPlayer      represents an instance of {@link SimpleExoPlayer}
     * @param mediaFactory        represents an instance of {@link ExtractorMediaSource.Factory}
     */
    @Inject
    ExoUtil(@NonNull Provider<SimpleExoPlayer> exoPlayer,
            @NonNull ExtractorMediaSource.Factory mediaFactory,
            @NonNull Application application) {
        this.exoPlayer = exoPlayer;
        this.mediaFactory = mediaFactory;
        this.application = application;

        mShouldAutoPlay = true;
    }

    /**
     * Helps build a {@link ExtractorMediaSource.Factory}
     *
     * @param uri represents a url to be played
     * @return an instance of {@link MediaSource}
     */
    @NonNull
    private MediaSource buildMediaSource(@Nullable Uri uri) {
        return mediaFactory.createMediaSource(uri);
    }

    public void setListener(@NonNull PlayerStateListener playerStateListener) {
        this.mPlayerStateListener = playerStateListener;
    }

    /**
     * Helps set a {@link PlayerView} in order to play media
     *
     * @param playerView represents a {@link PlayerView}
     */
    public void setPlayerView(@NonNull PlayerView playerView) {
        this.mPlayerView = playerView;
    }


    public void setPlayerViewWrapper(@NonNull FrameLayout playerViewWrapper) {
        this.mPlayerViewWrapper = playerViewWrapper;
    }
    /**
     * Helps set a URL in order to access the media
     *
     * @param url indicates the media url
     */
    public void setUrl(@NonNull String url) {
        this.mUrl = url;
    }

    /**
     * Initializes the {@link ExoUtil#exoPlayer}
     */

    public void setmFullScreenIcon(@NonNull ImageView imageView) {
        this.mFullScreenIcon = imageView;
    }

    public void setActivity(Activity activity) {
        this.rootActivity = activity;
    }





    private void initializePlayer() {

        simpleExoPlayer = exoPlayer.get();
        mPlayerView.setPlayer(simpleExoPlayer);
        mPlayerView.setPlaybackPreparer(this);
        simpleExoPlayer.addListener(this);
        simpleExoPlayer.setPlayWhenReady(mShouldAutoPlay);

        if (mUrl != null) {
            simpleExoPlayer.prepare(buildMediaSource(Uri.parse(mUrl)));
            simpleExoPlayer.seekTo(TempStorageUtils.readSharedPreferenceNumber(application,CURRENT_POSITION_KEY ));
        }
        initFullscreenDialog();
        if (!GeneralUtils.isTablet(application) && GeneralUtils.isLand(application)) {
            openFullscreenDialog();
        }
    }

    /**
     * Releases the {@link ExoUtil#exoPlayer}
     */
    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer.removeListener(this);
            mShouldAutoPlay = simpleExoPlayer.getPlayWhenReady();
            TempStorageUtils.writeSharedPreferenceNumber(application,CURRENT_POSITION_KEY, simpleExoPlayer.getCurrentPosition());
            simpleExoPlayer = null;
        }
    }

    /**
     * Gets called when Exo prepares its playback
     */
    @Override
    public void preparePlayback() {
        initializePlayer();
    }

    /**
     * Gets called when there is an error to play video
     *
     * @param error represents an error
     */
    @Override
    public void onPlayerError(ExoPlaybackException error) {
        mPlayerStateListener.onPlayerError();
    }

    /**
     * Gets called when video is ready to be played.
     *
     * @param playWhenReady indicates whether or not video is ready to be played
     * @param playbackState indicates the status of video
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        mPlayerStateListener.onPlayerStateChanged(playbackState);
    }

    /**
     * Clears references
     */
    public void onStart() {
        if (Util.SDK_INT > 23) {
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }
    }

    /**
     * Regains references
     */
    public void onResume() {
        if (Util.SDK_INT <= 23 || mPlayerView == null) {
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }
    }

    /**
     * Clears references
     */
    public void onPause() {
        if (Util.SDK_INT <= 23) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
            closeFullscreenDialog();
        }
    }

    /**
     * Regains references
     */
    public void onStop() {
        if (Util.SDK_INT > 23) {
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
            closeFullscreenDialog();
        }
    }





    /**
     * A listener that handles media playback
     */
    public interface PlayerStateListener {

        /**
         * Gets called playback state has been changed
         *
         * @param playbackState indicates the playback state
         */
        void onPlayerStateChanged(int playbackState);

        /**
         * Gets called when there is an error to play media
         */
        void onPlayerError();
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(rootActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(application, R.drawable.ic_fullscreen_shrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mPlayerViewWrapper.addView(mPlayerView);
        mPlayerViewWrapper.setVisibility(View.VISIBLE);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(application, R.drawable.ic_fullscreen_expand));
    }

    public void processFullScreen() {
        if (!mExoPlayerFullscreen)
            openFullscreenDialog();
        else
            closeFullscreenDialog();

    }

}
