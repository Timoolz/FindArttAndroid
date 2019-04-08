package com.olamide.findartt.di;

import android.arch.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.olamide.findartt.Constants;
import com.olamide.findartt.di.rx.SchedulersFactory;
import com.olamide.findartt.ViewModelFactory;
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

@Module
public class BuilderModule {
    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder =
//                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
                new GsonBuilder();
        return builder.setLenient().create();
    }


    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(Constants.FINDARTT_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }



    @Provides
    @Singleton
    FindArttAPI getFindArttAPI(Retrofit retrofit) {
        return retrofit.create(FindArttAPI.class);
    }

    @Provides
    @Singleton
    OkHttpClient getRequestHeader() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .build();
            return chain.proceed(request);
        })
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS);

        return httpClient.build();
    }

    @Provides
    @Singleton
    FindArttRepository getRepository(FindArttAPI findArttAPI) {
        return new FindArttRepository(findArttAPI);
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory getViewModelFactory(FindArttRepository myRepository, SchedulersFactory schedulersFactory) {
        return new ViewModelFactory(myRepository,schedulersFactory);
    }




}