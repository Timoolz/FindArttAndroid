package com.olamide.findartt;

import android.app.Activity;
import android.app.Application;
import androidx.fragment.app.Fragment;


import com.olamide.findartt.di.component.DaggerAppComponent;
import com.olamide.findartt.di.modules.ApiModule;
import com.olamide.findartt.di.modules.AppModule;
import com.olamide.findartt.di.modules.ExoModule;
import com.olamide.findartt.di.modules.FindArttGlideModule;


import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class FindArttApplication extends Application implements HasActivityInjector, HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;



    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
         DaggerAppComponent
                 .builder()
                 .appModule( new AppModule(this))
                 .apiModule( new ApiModule(AppConstants.FINDARTT_BASE_URL))
                 .exoModule(new ExoModule())
                 .build().inject(this);
    }



    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}
