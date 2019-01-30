package com.olamide.findartt.utils.network;


import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FindArttAPI {
//
//    @Headers({"accept: application/json",
//    "Content-Type: application/json"})
    @POST("v1/auth/login")
    Call<FindArttResponse<UserResult>> login(@Body UserLogin login);

}
