package com.olamide.findartt.utils.network;


import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.models.Bid;
import com.olamide.findartt.models.Buy;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserSignup;
import com.olamide.findartt.models.UserUpdate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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


    @POST("v1/users/update")
    Call<FindArttResponse<User>> updateUser(@Header("Authorization") String auth, @Body UserUpdate userUpdate );


    @POST("v1/art/create")
    Call<FindArttResponse<Artwork>> createArt(@Header("Authorization") String auth, @Body Artwork artwork );

    @POST("v1/art/update")
    Call<FindArttResponse<Artwork>> updateArt(@Header("Authorization") String auth, @Body Artwork artwork );

    @POST("v1/art/delete/{id}")
    Call<FindArttResponse> deleteArt(@Header("Authorization") String auth, @Path("id") int artworkId );

    @POST("v1/art/find")
    Call<FindArttResponse<List<Artwork>>> findArt(@Header("Authorization") String auth );

    @POST("v1/art/owner/find")
    Call<FindArttResponse<List<Artwork>>> findMyArt(@Header("Authorization") String auth );

    @POST("v1/art//find/{id}/summary")
    Call<FindArttResponse<ArtworkSummary>> getArtSummary(@Header("Authorization") String auth, @Path("id") int artworkId );

    @POST("v1/art/buy")
    Call<FindArttResponse<Buy>> buyArt(@Header("Authorization") String auth, @Body Buy buy );

    @POST("v1/art/bid")
    Call<FindArttResponse<Bid>> bidForArt(@Header("Authorization") String auth, @Body Bid bid );

    @POST("v1/art/bid/accept")
    Call<FindArttResponse<Bid>> acceptBid(@Header("Authorization") String auth, @Body Bid bid );





}
