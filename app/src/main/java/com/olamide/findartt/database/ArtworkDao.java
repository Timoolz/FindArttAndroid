package com.olamide.findartt.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.olamide.findartt.models.Artwork;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ArtworkDao {

    @Query("SELECT * FROM artwork ORDER BY created_at")
    Single<List<Artwork>> loadAllArtworks();

    @Query("SELECT * FROM artwork WHERE id = :id")
    Single<Artwork> loadArtworkById(int id);

    @Query("SELECT * FROM artwork WHERE id = :id")
    Artwork loadArtwork(int id);

    @Query("SELECT * FROM artwork ORDER BY created_at")
    List<Artwork> loadArtworks();

    @Insert
    void insertArtwork(Artwork artwork);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateArtwork(Artwork artwork);

    @Delete
    void deleteArtwork(Artwork artwork);


}
