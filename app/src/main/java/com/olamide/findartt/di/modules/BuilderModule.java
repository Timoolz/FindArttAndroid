package com.olamide.findartt.di.modules;

//import com.olamide.findartt.ui.activity.ArtworkActivity;
import com.olamide.findartt.service.LogoutService;
import com.olamide.findartt.ui.activity.BaseActivity;
import com.olamide.findartt.ui.activity.DashboardActivity;
import com.olamide.findartt.ui.fragment.ArtworkFragment;
import com.olamide.findartt.ui.fragment.HomeFragment;
import com.olamide.findartt.ui.activity.SignInActivity;
import com.olamide.findartt.ui.fragment.BidFragment;
import com.olamide.findartt.ui.fragment.BuyFragment;
import com.olamide.findartt.ui.fragment.LogInFragment;
import com.olamide.findartt.ui.fragment.MyArtworkFragment;
import com.olamide.findartt.ui.fragment.SignUpFragment;
import com.olamide.findartt.ui.fragment.UserFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class BuilderModule {

    @ContributesAndroidInjector
    abstract SignInActivity bindSignInActivity();

    @ContributesAndroidInjector
    abstract DashboardActivity bindDashboardActivity();

    @ContributesAndroidInjector
    abstract BaseActivity bindBaseActivity();

    @ContributesAndroidInjector
    abstract LogInFragment bindLoginFragment();
    @ContributesAndroidInjector
    abstract SignUpFragment bindSignUpFragment();

//    @ContributesAndroidInjector
//    abstract ArtworkActivity bindArtworkActivity();

    @ContributesAndroidInjector
    abstract BuyFragment bindBuyFragment();

    @ContributesAndroidInjector
    abstract BidFragment bindBidFragment();

    @ContributesAndroidInjector
    abstract HomeFragment bindHomeFragment();

    @ContributesAndroidInjector
    abstract UserFragment bindUserFragment();

    @ContributesAndroidInjector
    abstract ArtworkFragment bindArtworkFragment();

    @ContributesAndroidInjector
    abstract MyArtworkFragment bindMyArtworkFragment();

    @ContributesAndroidInjector
    abstract LogoutService logoutService();



    // Add bindings for other sub-components here




}