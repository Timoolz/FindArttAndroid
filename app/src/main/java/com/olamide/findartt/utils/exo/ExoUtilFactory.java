package com.olamide.findartt.utils.exo;

import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.PlayerView;

import javax.inject.Inject;

/**
 * A factory pattern that initializes the {@link ExoUtil}
 */
public class ExoUtilFactory {

    private ExoUtil exoUtil;

    /**
     * A default constructor that injects dependencies
     *
     * @param exoUtil represents an instance of {@link ExoUtil}
     */
    @Inject
    ExoUtilFactory(ExoUtil exoUtil) {
        this.exoUtil = exoUtil;
    }

    /**
     * Returns an instance of {@link ExoUtil}
     *
     * @return an instance of {@link ExoUtil}
     */
    public ExoUtil getExoUtil(Activity rootActivity, ExoUtil.PlayerStateListener mPlayerStateListener,
                               FrameLayout mPlayerViewWrapper,
                               PlayerView mPlayerView,
                               ImageView mFullScreenIcon,
                               boolean shouldAutoPlay) {
        exoUtil.setActivity(rootActivity);
        exoUtil.setListener(mPlayerStateListener);
        exoUtil.setPlayerViewWrapper(mPlayerViewWrapper);
        exoUtil.setPlayerView(mPlayerView);
        exoUtil.setmFullScreenIcon(mFullScreenIcon);
        exoUtil.setmShouldAutoPlay(shouldAutoPlay);
        return exoUtil;
    }

    public ExoUtil getExoUtil() {
        return exoUtil;
    }
}
