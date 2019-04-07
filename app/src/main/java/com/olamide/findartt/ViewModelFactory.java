package com.olamide.findartt;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.olamide.findartt.utils.network.FindArttRepository;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private FindArttRepository findArttRepository;
    private SchedulersFactory schedulersFactory;

    @Inject
    public ViewModelFactory(FindArttRepository findArttRepository, SchedulersFactory schedulersFactory) {
        this.findArttRepository = findArttRepository;
        this.schedulersFactory = schedulersFactory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(findArttRepository, schedulersFactory);
        }
        throw new IllegalArgumentException("Unknown class name");    }
}
