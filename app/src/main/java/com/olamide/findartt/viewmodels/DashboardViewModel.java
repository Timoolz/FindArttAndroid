package com.olamide.findartt.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.network.FindArttRepository;

import io.reactivex.disposables.CompositeDisposable;

public class DashboardViewModel extends AndroidViewModel {

    private FindArttRepository findArttRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<MVResponse> artworkLiveData = new MutableLiveData<>();
    private SchedulersFactory schedulersFactory;


    public DashboardViewModel(Application application,FindArttRepository findArttRepository, SchedulersFactory schedulersFactory) {
        this(application);
        this.findArttRepository = findArttRepository;
        this.schedulersFactory = schedulersFactory;
    }

    public DashboardViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<MVResponse> getArtWorkResponse() {
        return artworkLiveData;
    }


    public void findArtworks(String accessToken) {

        disposables.add(findArttRepository.findArt(accessToken)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkLiveData.setValue(MVResponse.loading()))
                .subscribe(
                        result -> artworkLiveData.setValue(MVResponse.success(result)),
                        throwable -> artworkLiveData.setValue(MVResponse.error(throwable))
                ));

    }

    public void findFavouriteArtworks() {

        disposables.add(findArttRepository.findFavouriteArt()
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkLiveData.setValue(MVResponse.loading()))
                .subscribe(
                        result -> artworkLiveData.setValue(MVResponse.success(result)),
                        throwable -> artworkLiveData.setValue(MVResponse.error(throwable))
                ));

    }


    @Override
    protected void onCleared() {
        disposables.clear();
    }

}
