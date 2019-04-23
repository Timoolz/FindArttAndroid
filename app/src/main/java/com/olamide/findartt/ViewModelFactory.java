package com.olamide.findartt;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.utils.network.FindArttRepository;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private FindArttRepository findArttRepository;
    private SchedulersFactory schedulersFactory;
    private Application application;

    @Inject
    public ViewModelFactory(Application application, FindArttRepository findArttRepository, SchedulersFactory schedulersFactory) {
        this.findArttRepository = findArttRepository;
        this.schedulersFactory = schedulersFactory;
        this.application= application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(findArttRepository, schedulersFactory);
        }

        if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel(findArttRepository, schedulersFactory);
        }

        if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            return (T) new DashboardViewModel(application, findArttRepository, schedulersFactory);
        }

        if (modelClass.isAssignableFrom(ArtworkNewViewModel.class)) {
            return (T) new ArtworkNewViewModel(application, findArttRepository, schedulersFactory);
        }
        throw new IllegalArgumentException("Unknown class name");    }
}
