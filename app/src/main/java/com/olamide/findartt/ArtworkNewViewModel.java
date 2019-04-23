package com.olamide.findartt;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.network.FindArttRepository;

import io.reactivex.disposables.CompositeDisposable;

public class ArtworkNewViewModel extends AndroidViewModel {

    private FindArttRepository findArttRepository;
    private SchedulersFactory schedulersFactory;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final CompositeDisposable favouriteDisposables = new CompositeDisposable();
    private final CompositeDisposable videoDisposables = new CompositeDisposable();
    private final MutableLiveData<MVResponse> artworkSummaryLive = new MutableLiveData<>();
    private final MutableLiveData<MVResponse> artworkFavouriteLive = new MutableLiveData<>();



    public ArtworkNewViewModel(Application application, FindArttRepository findArttRepository, SchedulersFactory schedulersFactory) {
        this(application);
        this.findArttRepository = findArttRepository;
        this.schedulersFactory = schedulersFactory;
    }

    public ArtworkNewViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<MVResponse> getArtWorkResponse() {
        return artworkSummaryLive;
    }

    public MutableLiveData<MVResponse> getArtWorkFavourite() {
        return artworkFavouriteLive;
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

    public void findArtFavourite( int artId ) {

        favouriteDisposables.add(findArttRepository.loadArtworkById( artId)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkFavouriteLive.setValue(MVResponse.loading()))
                .subscribe(
                        result -> artworkFavouriteLive.setValue(MVResponse.success(result)),
                        throwable -> artworkFavouriteLive.setValue(MVResponse.error(throwable))
                ));

    }

    public void insertFavouriteArt(Artwork artwork) {


        favouriteDisposables.add(findArttRepository.insertFavouriteArt(artwork)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkFavouriteLive.setValue(MVResponse.loading()))
                .subscribe(
                        () -> artworkFavouriteLive.setValue(MVResponse.success(new Artwork())),
//                        () -> artwork.getCreatedBy(),
                        throwable -> artworkFavouriteLive.setValue(MVResponse.error(throwable))
                ));

    }

    public void deleteFavouriteArt( Artwork artwork ) {

        favouriteDisposables.add(findArttRepository.deleteFavouriteArt(artwork)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .doOnSubscribe((d) -> artworkFavouriteLive.setValue(MVResponse.loading()))
                .subscribe(
                        () -> artworkFavouriteLive.setValue(MVResponse.success(null)),
//                        () -> artwork.getCreatedBy(),
                        throwable -> artworkFavouriteLive.setValue(MVResponse.error(throwable))
                )
        );

    }


    @Override
    protected void onCleared() {
        disposables.clear();
        favouriteDisposables.clear();
        videoDisposables.clear();
    }
}
