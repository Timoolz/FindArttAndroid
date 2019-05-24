package com.olamide.findartt.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.olamide.findartt.models.Artwork;

import java.util.List;

import io.reactivex.Completable;
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
    Completable insertArtwork(Artwork artwork);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateArtwork(Artwork artwork);

    @Delete
    Completable deleteArtwork(Artwork artwork);


}
