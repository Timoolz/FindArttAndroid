package com.olamide.findartt.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.olamide.findartt.AppConstants;
import com.olamide.findartt.utils.network.FindArttRepository;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class LogoutService extends IntentService {


    public static final String ACTION_LOGOUT_FROM_SERVER = "com.olamide.findartt.widget.action.server.logout";

    @Inject
    FindArttRepository findArttRepository;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    public LogoutService() {
        super("LogoutService");
    }

    private final CompositeDisposable disposables = new CompositeDisposable();


    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null && Objects.equals(intent.getAction(), ACTION_LOGOUT_FROM_SERVER)) {
            Timber.i(ACTION_LOGOUT_FROM_SERVER);
            String mAccess = intent.getStringExtra(AppConstants.ACCESS_TOKEN_STRING);
            disposables.add(findArttRepository.logout(mAccess).subscribe());

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
