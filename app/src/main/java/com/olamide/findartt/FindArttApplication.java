package com.olamide.findartt;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

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

import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class FindArttApplication extends Application
        implements HasActivityInjector,
        HasSupportFragmentInjector, HasServiceInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Inject
    DispatchingAndroidInjector<Service> serviceInjector;


    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(AppConstants.FINDARTT_BASE_URL))
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

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceInjector;
    }
}
