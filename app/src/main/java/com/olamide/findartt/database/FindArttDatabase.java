package com.olamide.findartt.database;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.utils.Converters;

import timber.log.Timber;

@Database(entities = {Artwork.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class FindArttDatabase extends RoomDatabase {

    public abstract ArtworkDao artworkDao();
    //ADD ANY FURTHER DAO THAT MIGHT BE NEEDED


}
