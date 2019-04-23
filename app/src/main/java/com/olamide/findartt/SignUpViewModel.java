package com.olamide.findartt;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.UserSignup;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.network.FindArttRepository;

import io.reactivex.disposables.CompositeDisposable;

public class SignUpViewModel extends ViewModel {

    private FindArttRepository findArttRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<MVResponse> responseLiveData = new MutableLiveData<>();
    private SchedulersFactory schedulersFactory;


    public SignUpViewModel(FindArttRepository findArttRepository, SchedulersFactory schedulersFactory) {
        this.findArttRepository = findArttRepository;
        this.schedulersFactory = schedulersFactory;
    }

    public MutableLiveData<MVResponse> getSignupResponse() {
        return responseLiveData;
    }


    public void signUp(UserSignup userSignup) {

        disposables.add(findArttRepository.signUp(userSignup)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> responseLiveData.setValue(MVResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(MVResponse.success(result)),
                        throwable -> responseLiveData.setValue(MVResponse.error(throwable))
                ));

    }

    public void signUpGoogle(TokenInfo tokenInfo) {

        disposables.add(findArttRepository.signUpGoogle(tokenInfo)
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
