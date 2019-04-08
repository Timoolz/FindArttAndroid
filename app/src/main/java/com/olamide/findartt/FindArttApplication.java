package com.olamide.findartt;

import android.app.Application;
import android.content.Context;

import com.olamide.findartt.di.AppComponent;
import com.olamide.findartt.di.AppModule;
import com.olamide.findartt.di.BuilderModule;
import com.olamide.findartt.di.DaggerAppComponent;

import timber.log.Timber;

public class FindArttApplication extends Application {
    AppComponent appComponent;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        context = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).builderModule(new BuilderModule()).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }
}
