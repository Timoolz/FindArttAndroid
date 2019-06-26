package com.olamide.findartt.viewmodels;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.olamide.findartt.di.rx.SchedulersFactory;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

public class VideoViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
//    private PlayerUseCase playerUseCase;

    //    private MutableLiveData<PlayerResponse> mPlayerResponse = new MutableLiveData<>();
    private MutableLiveData<String> mPlayerResponse = new MutableLiveData<>();
    private SchedulersFactory schedulersFactory;

    public VideoViewModel(SchedulersFactory schedulersFactory) {
        this.schedulersFactory = schedulersFactory;
    }



    public void getPlayableContent(@NonNull String videoUrl) {
//        Disposable url = new DisposableSingleObserver<String>() {
//            @Override
//            public void onSuccess(String p) {
//                mPlayerResponse.setValue(videoUrl);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//        } ;
//
        Observable<String> ee =  Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                // Do some long running operation
                SystemClock.sleep(2000);
                return Observable.just(videoUrl);
            }
        });
//        compositeDisposable.add(ee);
//

        compositeDisposable.add(Observable.just(videoUrl)
                .subscribeOn(schedulersFactory.io())
                .observeOn(schedulersFactory.ui())
                .subscribe(
                        result -> mPlayerResponse.setValue(result)
                ));
    }

    public MutableLiveData<String> getContent() {
        return mPlayerResponse;
    }


    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

}
