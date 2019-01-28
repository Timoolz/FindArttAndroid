package com.olamide.findartt.utils.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.olamide.findartt.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static Retrofit retrofit = null;
    private static FindArttAPI findArttAPI = getFindArttClient().create(FindArttAPI.class);


    public static Retrofit getFindArttClient() {
        if (retrofit==null) {
            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.FINDARTT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


    public static FindArttAPI getFindArttApi(){
        return findArttAPI;
    }

}
