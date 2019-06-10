package com.olamide.findartt;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.utils.network.FindArttRepository;
import com.olamide.findartt.viewmodels.ArtworkViewModel;
import com.olamide.findartt.viewmodels.HomeViewModel;
import com.olamide.findartt.viewmodels.LoginViewModel;
import com.olamide.findartt.viewmodels.SignUpViewModel;
import com.olamide.findartt.viewmodels.UserViewModel;

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

        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(application, findArttRepository, schedulersFactory);
        }

        if (modelClass.isAssignableFrom(ArtworkViewModel.class)) {
            return (T) new ArtworkViewModel(application, findArttRepository, schedulersFactory);
        }

        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(application, findArttRepository, schedulersFactory);
        }

        if (modelClass.isAssignableFrom(VideoViewModel.class)) {
            return (T) new VideoViewModel(schedulersFactory);
        }
        throw new IllegalArgumentException("Unknown class name");    }
}
