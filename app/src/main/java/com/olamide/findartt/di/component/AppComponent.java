package com.olamide.findartt.di.component;

import com.olamide.findartt.FindArttApplication;
import com.olamide.findartt.di.modules.AppModule;
import com.olamide.findartt.di.modules.BuilderModule;
import com.olamide.findartt.di.modules.ApiModule;
//import com.olamide.findartt.di.modules.FragmentBuilderModule;

import javax.inject.Singleton;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjection;


@Component(modules = { AndroidInjectionModule.class, AppModule.class, BuilderModule.class,  ApiModule.class})
@Singleton
public interface AppComponent {

    void inject(FindArttApplication application);




}
