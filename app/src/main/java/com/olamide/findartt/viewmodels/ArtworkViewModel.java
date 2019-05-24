package com.olamide.findartt.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.Bid;
import com.olamide.findartt.models.Buy;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.network.FindArttRepository;

import io.reactivex.disposables.CompositeDisposable;

public class ArtworkViewModel extends AndroidViewModel {

    private FindArttRepository findArttRepository;
    private SchedulersFactory schedulersFactory;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<MVResponse> artworkSummaryLive = new MutableLiveData<>();
    private final MutableLiveData<MVResponse> artworkFavouriteLive = new MutableLiveData<>();
    private final MutableLiveData<MVResponse> buyLive = new MutableLiveData<>();
    private final MutableLiveData<MVResponse> bidLive = new MutableLiveData<>();



    public ArtworkViewModel(Application application, FindArttRepository findArttRepository, SchedulersFactory schedulersFactory) {
        this(application);
        this.findArttRepository = findArttRepository;
        this.schedulersFactory = schedulersFactory;
    }

    public ArtworkViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<MVResponse> getArtWorkResponse() {
        return artworkSummaryLive;
    }

    public MutableLiveData<MVResponse> getArtWorkFavourite() {
        return artworkFavouriteLive;
    }

    public MutableLiveData<MVResponse> getBuyResponse() {
        return buyLive;
    }

    public MutableLiveData<MVResponse> getBidResponse() {
        return bidLive;
    }



    public void findArtSummary(String accessToken, int artId ) {

        disposables.add(findArttRepository.getArtSummary(accessToken, artId)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkSummaryLive.setValue(MVResponse.loading()))
                .subscribe(
                        result -> artworkSummaryLive.setValue(MVResponse.success(result)),
                        throwable -> artworkSummaryLive.setValue(MVResponse.error(throwable))
                ));

    }

    public void buyArt(String accessToken, Buy buy ) {

        disposables.add(findArttRepository.buyArt(accessToken, buy)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> buyLive.setValue(MVResponse.loading()))
                .subscribe(
                        result -> buyLive.setValue(MVResponse.success(result)),
                        throwable -> buyLive.setValue(MVResponse.error(throwable))
                ));

    }

    public void bidForArt(String accessToken, Bid bid ) {

        disposables.add(findArttRepository.bidForArt(accessToken, bid)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> bidLive.setValue(MVResponse.loading()))
                .subscribe(
                        result -> bidLive.setValue(MVResponse.success(result)),
                        throwable -> bidLive.setValue(MVResponse.error(throwable))
                ));

    }

    public void findArtFavourite( int artId ) {

        disposables.add(findArttRepository.loadArtworkById( artId)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkFavouriteLive.setValue(MVResponse.loading()))
                .subscribe(
                        result -> artworkFavouriteLive.setValue(MVResponse.success(result)),
                        throwable -> artworkFavouriteLive.setValue(MVResponse.error(throwable))
                ));

    }

    public void insertFavouriteArt(Artwork artwork) {


        disposables.add(findArttRepository.insertFavouriteArt(artwork)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkFavouriteLive.setValue(MVResponse.loading()))
                .subscribe(
                        () -> artworkFavouriteLive.setValue(MVResponse.success(new Artwork())),
                        throwable -> artworkFavouriteLive.setValue(MVResponse.error(throwable))
                ));

    }

    public void deleteFavouriteArt( Artwork artwork ) {

        disposables.add(findArttRepository.deleteFavouriteArt(artwork)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkFavouriteLive.setValue(MVResponse.loading()))
                .subscribe(
                        () -> artworkFavouriteLive.setValue(MVResponse.success(null)),
                        throwable -> artworkFavouriteLive.setValue(MVResponse.error(throwable))
                )
        );

    }


    @Override
    protected void onCleared() {
        disposables.clear();

    }
}
