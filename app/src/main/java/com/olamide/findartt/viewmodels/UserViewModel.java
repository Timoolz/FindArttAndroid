package com.olamide.findartt.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.models.UserUpdate;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.network.FindArttRepository;

import io.reactivex.disposables.CompositeDisposable;

public class UserViewModel extends AndroidViewModel {

    private FindArttRepository findArttRepository;
    private SchedulersFactory schedulersFactory;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<MVResponse> userLive = new MutableLiveData<>();

    public UserViewModel(Application application, FindArttRepository findArttRepository, SchedulersFactory schedulersFactory) {
        this(application);
        this.findArttRepository = findArttRepository;
        this.schedulersFactory = schedulersFactory;
    }


    public UserViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<MVResponse> getUpdateResponse() {
        return userLive;
    }

    public void updateUser(String accessToken, UserUpdate userUpdate) {

        disposables.add(findArttRepository.updateUser(accessToken, userUpdate)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> userLive.setValue(MVResponse.loading()))
                .subscribe(
                        result -> userLive.setValue(MVResponse.success(result)),
                        throwable -> userLive.setValue(MVResponse.error(throwable))
                ));

    }




    @Override
    protected void onCleared() {
        disposables.clear();

    }
}
