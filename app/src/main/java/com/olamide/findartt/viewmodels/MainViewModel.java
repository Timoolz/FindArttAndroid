package com.olamide.findartt.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.olamide.findartt.database.FindArttDatabase;
import com.olamide.findartt.models.Artwork;

import java.util.List;

import timber.log.Timber;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Artwork>> artworks;

    public MainViewModel(Application application) {
        super(application);


        FindArttDatabase database = FindArttDatabase.getInstance(this.getApplication());
        Timber.d( "Actively retrieving the favourite Artworks from the DataBase");

        artworks = database.artworkDao().loadAllArtworks();

    }

    public LiveData<List<Artwork>> getArtworks() {
        return artworks;
    }



}
