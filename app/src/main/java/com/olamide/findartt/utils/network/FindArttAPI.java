package com.olamide.findartt.utils.network;


import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserSignup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FindArttAPI {


    @POST("v1/auth/login")
    Call<FindArttResponse<UserResult>> login(@Body UserLogin login);

    @POST("v1/auth/signup")
    Call<FindArttResponse<UserResult>> signUp(@Body UserSignup signup);

    @POST("v1/auth/login/google")
    Call<FindArttResponse<UserResult>> loginGoogle(@Body TokenInfo tokenInfo);

    @POST("v1/auth/signup/google")
    Call<FindArttResponse<UserResult>> signUpGoogle(@Body TokenInfo tokenInfo);

    @POST("v1/auth/logout")
    Call<FindArttResponse> logout(@Header("Authorization") String auth);

}
