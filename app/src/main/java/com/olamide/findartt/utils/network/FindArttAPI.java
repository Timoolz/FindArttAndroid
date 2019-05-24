package com.olamide.findartt.utils.network;


import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.models.Bid;
import com.olamide.findartt.models.Buy;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserSignup;
import com.olamide.findartt.models.UserUpdate;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FindArttAPI {


    @POST("v1/auth/login")
    Observable<FindArttResponse<UserResult>> login(@Body UserLogin login);

    @POST("v1/auth/signup")
    Observable<FindArttResponse<UserResult>> signUp(@Body UserSignup signup);

    @POST("v1/auth/login/google")
    Observable<FindArttResponse<UserResult>> loginGoogle(@Body TokenInfo tokenInfo);

    @POST("v1/auth/signup/google")
    Observable<FindArttResponse<UserResult>> signUpGoogle(@Body TokenInfo tokenInfo);

    @GET("v1/users")
    Observable<FindArttResponse<User>> getUserFromToken(@Header("Authorization") String auth );



    @POST("v1/auth/logout")
    Observable<FindArttResponse> logout(@Header("Authorization") String auth);


    @POST("v1/users/update")
    Observable<FindArttResponse<User>> updateUser(@Header("Authorization") String auth, @Body UserUpdate userUpdate );


    @POST("v1/art/create")
    Observable<FindArttResponse<Artwork>> createArt(@Header("Authorization") String auth, @Body Artwork artwork );

    @POST("v1/art/update")
    Observable<FindArttResponse<Artwork>> updateArt(@Header("Authorization") String auth, @Body Artwork artwork );

    @DELETE("v1/art/delete/{id}")
    Observable<FindArttResponse> deleteArt(@Header("Authorization") String auth, @Path("id") int artworkId );

    @GET("v1/art/find")
    Observable<FindArttResponse<List<Artwork>>> findArt(@Header("Authorization") String auth );

    @GET("v1/art/owner/find")
    Observable<FindArttResponse<List<Artwork>>> findMyArt(@Header("Authorization") String auth );

    @GET("v1/art/find/{id}/summary")
    Observable<FindArttResponse<ArtworkSummary>> getArtSummary(@Header("Authorization") String auth, @Path("id") int artworkId );

    @POST("v1/art/buy")
    Observable<FindArttResponse<Buy>> buyArt(@Header("Authorization") String auth, @Body Buy buy );

    @POST("v1/art/bid")
    Observable<FindArttResponse<Bid>> bidForArt(@Header("Authorization") String auth, @Body Bid bid );

    @POST("v1/art/bid/accept")
    Observable<FindArttResponse<Bid>> acceptBid(@Header("Authorization") String auth, @Body Bid bid );





}
