package com.olamide.findartt.di.component;

import com.olamide.findartt.FindArttApplication;
import com.olamide.findartt.di.modules.AppModule;
import com.olamide.findartt.di.modules.BuilderModule;
import com.olamide.findartt.di.modules.ApiModule;
import com.olamide.findartt.di.modules.ExoModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;


@Component(modules = {AndroidInjectionModule.class, AppModule.class, BuilderModule.class,
        ApiModule.class, ExoModule.class})
@Singleton
public interface AppComponent {

    void inject(FindArttApplication application);


}

