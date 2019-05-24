package com.olamide.findartt.utils.network;

import com.olamide.findartt.database.ArtworkDao;
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

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;


public class FindArttRepository {

    private FindArttAPI findArttAPI;
    private ArtworkDao artworkDao;

    public FindArttRepository(FindArttAPI findArttAPI, ArtworkDao artworkDao) {
        this.findArttAPI = findArttAPI;
        this.artworkDao = artworkDao;
    }


    //********
    // API METHODS(RETROFIT)
    //********
    public Observable<FindArttResponse<UserResult>> login(UserLogin userLogin) {
        return findArttAPI.login(userLogin);
    }

    public Observable<FindArttResponse<UserResult>> signUp(UserSignup userSignup) {
        return findArttAPI.signUp(userSignup);
    }

    public Observable<FindArttResponse<UserResult>> loginGoogle(TokenInfo tokenInfo) {
        return findArttAPI.loginGoogle(tokenInfo);
    }

    public Observable<FindArttResponse<UserResult>> signUpGoogle(TokenInfo tokenInfo) {
        return findArttAPI.signUpGoogle(tokenInfo);
    }

    public Observable<FindArttResponse<User>> getUserFromToken(String apiAccessToken) {
        return findArttAPI.getUserFromToken(apiAccessToken);
    }

    public Observable<FindArttResponse> logout(String ApiAccessToken) {
        return findArttAPI.logout(ApiAccessToken);
    }


    public Observable<FindArttResponse<User>> updateUser(String ApiAccessToken, UserUpdate userUpdate) {
        return findArttAPI.updateUser(ApiAccessToken, userUpdate);
    }


    public Observable<FindArttResponse<Artwork>> createArt(String ApiAccessToken, Artwork artwork) {
        return findArttAPI.createArt(ApiAccessToken, artwork);
    }

    public Observable<FindArttResponse<Artwork>> updateArt(String ApiAccessToken, Artwork artwork) {
        return findArttAPI.updateArt(ApiAccessToken, artwork);
    }

    public Observable<FindArttResponse> deleteArt(String ApiAccessToken, int artworkId) {
        return findArttAPI.deleteArt(ApiAccessToken, artworkId);
    }

    public Observable<FindArttResponse<List<Artwork>>> findArt(String ApiAccessToken) {
        return findArttAPI.findArt(ApiAccessToken);
    }

    public Observable<FindArttResponse<List<Artwork>>> findMyArt(String ApiAccessToken) {
        return findArttAPI.findMyArt(ApiAccessToken);
    }

    public Observable<FindArttResponse<ArtworkSummary>> getArtSummary(String ApiAccessToken, int artworkId) {
        return findArttAPI.getArtSummary(ApiAccessToken, artworkId);
    }

    public Observable<FindArttResponse<Buy>> buyArt(String ApiAccessToken, Buy buy) {
        return findArttAPI.buyArt(ApiAccessToken, buy);
    }

    public Observable<FindArttResponse<Bid>> bidForArt(String ApiAccessToken, Bid bid) {
        return findArttAPI.bidForArt(ApiAccessToken, bid);
    }

    public Observable<FindArttResponse<Bid>> acceptBid(String ApiAccessToken, Bid bid) {
        return findArttAPI.acceptBid(ApiAccessToken, bid);
    }
    //********
    // LOCAL METHODS(ROOM)
    //********

    public Single<List<Artwork>> findFavouriteArt() {
        return artworkDao.loadAllArtworks();
    }

    public Single<Artwork> loadArtworkById(int id) {
        return artworkDao.loadArtworkById(id);
    }

    public Completable insertFavouriteArt(Artwork artwork) {
          return artworkDao.insertArtwork(artwork);
    }

    public Completable deleteFavouriteArt(Artwork artwork) {
          return artworkDao.deleteArtwork(artwork);
    }

}
