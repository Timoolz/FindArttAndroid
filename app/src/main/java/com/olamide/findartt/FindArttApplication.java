package com.olamide.findartt;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;


import com.olamide.findartt.di.component.AppComponent;
import com.olamide.findartt.di.component.DaggerAppComponent;
import com.olamide.findartt.di.modules.ApiModule;
import com.olamide.findartt.di.modules.AppModule;


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
                 .apiModule( new ApiModule(Constants.FINDARTT_BASE_URL))
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
