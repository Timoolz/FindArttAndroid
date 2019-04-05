package com.olamide.findartt.di;

import com.olamide.findartt.activity.SignInActivity;
import com.olamide.findartt.fragment.LogInFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, BuilderModule.class})
@Singleton
public interface AppComponent {

    void doInjection(SignInActivity signInActivity);
    void doInjection(LogInFragment logInFragment);

}

