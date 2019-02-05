package com.olamide.findartt.utils.network;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ShareCompat;

import com.olamide.findartt.Constants;
import com.olamide.findartt.activity.SignInActivity;
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
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class FindArttService {


    public static Call<FindArttResponse<UserResult>> login(UserLogin userLogin) {
        return RetrofitBuilder.getFindArttApi().login(userLogin);
    }

    public static Call<FindArttResponse<UserResult>> signUp(UserSignup userSignup) {
        return RetrofitBuilder.getFindArttApi().signUp(userSignup);
    }

    public static Call<FindArttResponse<UserResult>> loginGoogle(TokenInfo tokenInfo) {
        return RetrofitBuilder.getFindArttApi().loginGoogle(tokenInfo);
    }

    public static Call<FindArttResponse<UserResult>> signUpGoogle(TokenInfo tokenInfo) {
        return RetrofitBuilder.getFindArttApi().signUpGoogle(tokenInfo);
    }

    public static Call<FindArttResponse> logout(String ApiAccessToken) {
        return RetrofitBuilder.getFindArttApi().logout(ApiAccessToken);
    }


    public static Call<FindArttResponse<User>> updateUser(String ApiAccessToken, UserUpdate userUpdate) {
        return RetrofitBuilder.getFindArttApi().updateUser(ApiAccessToken, userUpdate);
    }


    public static Call<FindArttResponse<Artwork>> createArt(String ApiAccessToken, Artwork artwork) {
        return RetrofitBuilder.getFindArttApi().createArt(ApiAccessToken, artwork);
    }

    public static Call<FindArttResponse<Artwork>> updateArt(String ApiAccessToken, Artwork artwork) {
        return RetrofitBuilder.getFindArttApi().updateArt(ApiAccessToken, artwork);
    }

    public static Call<FindArttResponse> deleteArt(String ApiAccessToken, int artworkId) {
        return RetrofitBuilder.getFindArttApi().deleteArt(ApiAccessToken, artworkId);
    }

    public static Call<FindArttResponse<List<Artwork>>> findArt(String ApiAccessToken) {
        return RetrofitBuilder.getFindArttApi().findArt(ApiAccessToken);
    }

    public static Call<FindArttResponse<List<Artwork>>> findMyArt( String ApiAccessToken){
        return  RetrofitBuilder.getFindArttApi().findMyArt(ApiAccessToken);
    }

    public static     Call<FindArttResponse<ArtworkSummary>> getArtSummary( String ApiAccessToken,  int artworkId){
        return RetrofitBuilder.getFindArttApi().getArtSummary(ApiAccessToken, artworkId);
    }

    public static     Call<FindArttResponse<Buy>> buyArt( String ApiAccessToken,  Buy buy){
        return  RetrofitBuilder.getFindArttApi().buyArt(ApiAccessToken, buy);
    }

    public static     Call<FindArttResponse<Bid>> bidForArt( String ApiAccessToken,  Bid bid){
        return  RetrofitBuilder.getFindArttApi().bidForArt(ApiAccessToken, bid);
    }

    public static     Call<FindArttResponse<Bid>> acceptBid(String ApiAccessToken,  Bid bid){
        return  RetrofitBuilder.getFindArttApi().acceptBid(ApiAccessToken, bid);
    }


}
