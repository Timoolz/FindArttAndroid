package com.olamide.findartt.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.utils.Converters;

import timber.log.Timber;

@Database(entities = {Artwork.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class FindArttDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "FindArtt";
    private static FindArttDatabase sInstance;

    public static FindArttDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d( "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FindArttDatabase.class, FindArttDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Timber.d( "Getting the database instance");
        return sInstance;
    }
    public abstract ArtworkDao artworkDao();


}
