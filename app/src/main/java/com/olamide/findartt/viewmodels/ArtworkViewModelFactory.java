package com.olamide.findartt.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.olamide.findartt.database.FindArttDatabase;

public class ArtworkViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final FindArttDatabase mDb;
    private final int mArtworkId;


    public ArtworkViewModelFactory(FindArttDatabase database, int artworkId) {
        mDb = database;
        mArtworkId = artworkId;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ArtworkViewModel(mDb, mArtworkId);
    }
}
