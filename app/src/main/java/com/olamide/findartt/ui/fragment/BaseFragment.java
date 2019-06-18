package com.olamide.findartt.ui.fragment;

import com.olamide.findartt.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    @Inject
    ViewModelFactory viewModelFactory;
}
