//package com.olamide.findartt.viewmodels;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.lifecycle.ViewModel;
//
//import com.olamide.findartt.database.FindArttDatabase;
//import com.olamide.findartt.models.Artwork;
//
//public class ArtworkViewModel extends ViewModel {
//
//    private LiveData<Artwork> artworkLiveData;
//
//
//    public ArtworkViewModel(FindArttDatabase database, int artworkId) {
//        artworkLiveData = database.artworkDao().loadArtworkById(artworkId);
//    }
//
//
//    public LiveData<Artwork> getArtworkLiveData() {
//        return artworkLiveData;
//    }
//}
