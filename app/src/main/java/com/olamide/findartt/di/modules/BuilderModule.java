package com.olamide.findartt.di.modules;

import com.olamide.findartt.activity.ArtworkActivity;
import com.olamide.findartt.activity.DashboardActivity;
import com.olamide.findartt.activity.SignInActivity;
import com.olamide.findartt.fragment.LogInFragment;
import com.olamide.findartt.fragment.SignUpFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class BuilderModule {

    @ContributesAndroidInjector
    abstract SignInActivity bindSignInActivity();

    @ContributesAndroidInjector
    abstract DashboardActivity bindDashboardActivity();

    @ContributesAndroidInjector
    abstract LogInFragment bindLoginFragment();
    @ContributesAndroidInjector
    abstract SignUpFragment bindSignUpFragment();

    @ContributesAndroidInjector
    abstract ArtworkActivity bindArtworkActivity();


    // Add bindings for other sub-components here




}