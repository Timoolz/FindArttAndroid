package com.olamide.findartt.di.modules;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.olamide.findartt.Constants;
import com.olamide.findartt.ViewModelFactory;
import com.olamide.findartt.database.ArtworkDao;
import com.olamide.findartt.database.FindArttDatabase;
import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.utils.AppAuthUtil;
import com.olamide.findartt.utils.network.FindArttAPI;
import com.olamide.findartt.utils.network.FindArttRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class AppModule {


    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }


    @Provides
    @Singleton
    FindArttDatabase providesFindArttDatabase(Application application) {
        Timber.d( "Getting the database instance");
        return Room.databaseBuilder(application, FindArttDatabase.class, Constants.DATABASE_NAME).build();


    }


    @Provides
    @Singleton
    ArtworkDao providesArtworkDao(FindArttDatabase database) {
        return database.artworkDao();
    }


    @Provides
    @Singleton
    FindArttRepository providesRepository(FindArttAPI findArttAPI, ArtworkDao artworkDao) {
        return new FindArttRepository(findArttAPI, artworkDao);
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory providesViewModelFactory(FindArttRepository myRepository, SchedulersFactory schedulersFactory) {
        return new ViewModelFactory(myRepository, schedulersFactory);
    }


    @Provides
    @Singleton
    AppAuthUtil providesAuth() {
        return new AppAuthUtil(application);
    }


}
