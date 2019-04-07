package com.olamide.findartt;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.utils.network.FindArttRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private FindArttRepository findArttRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<MVResponse> responseLiveData = new MutableLiveData<>();
    private  SchedulersFactory schedulersFactory;


    public LoginViewModel(FindArttRepository findArttRepository, SchedulersFactory schedulersFactory) {
        this.findArttRepository = findArttRepository;
        this.schedulersFactory = schedulersFactory;
    }

    public MutableLiveData<MVResponse> loginResponse() {
        return responseLiveData;
    }

    /*
     * method to call normal login api with $(mobileNumber + password)
     * */
    public void hitLogin(UserLogin userLogin) {

        disposables.add(findArttRepository.login(userLogin)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> responseLiveData.setValue(MVResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(MVResponse.success(result)),
                        throwable -> responseLiveData.setValue(MVResponse.error(throwable))
                ));

    }

    public void hitGoogleLogin(TokenInfo tokenInfo) {

        disposables.add(findArttRepository.loginGoogle(tokenInfo)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> responseLiveData.setValue(MVResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(MVResponse.success(result)),
                        throwable -> responseLiveData.setValue(MVResponse.error(throwable))
                ));

    }

    public void getUserFromToken(String accessToken) {

        disposables.add(findArttRepository.getUserFromToken(accessToken)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> responseLiveData.setValue(MVResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(MVResponse.success(result)),
                        throwable -> responseLiveData.setValue(MVResponse.error(throwable))
                ));

    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}

