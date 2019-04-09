package com.olamide.findartt.di;

import com.olamide.findartt.activity.DashboardActivity;
import com.olamide.findartt.activity.SignInActivity;
import com.olamide.findartt.fragment.LogInFragment;
import com.olamide.findartt.fragment.SignUpFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, BuilderModule.class})
@Singleton
public interface AppComponent {

    void doInjection(SignInActivity signInActivity);
    void doInjection(LogInFragment logInFragment);
    void doInjection(SignUpFragment signUpFragment);
    void doInjection(DashboardActivity dashboardActivity);

}

