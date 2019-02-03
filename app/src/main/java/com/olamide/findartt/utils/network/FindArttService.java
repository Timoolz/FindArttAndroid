package com.olamide.findartt.utils.network;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ShareCompat;

import com.olamide.findartt.Constants;
import com.olamide.findartt.activity.SignInActivity;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserSignup;

import java.util.List;

import retrofit2.Call;

public class FindArttService {


    public static Call<FindArttResponse<UserResult>> login(UserLogin userLogin){
       return RetrofitBuilder.getFindArttApi().login(userLogin);
    }

    public static Call<FindArttResponse<UserResult>> signUp(UserSignup userSignup){
        return RetrofitBuilder.getFindArttApi().signUp(userSignup);
    }

    public static Call<FindArttResponse<UserResult>> loginGoogle(TokenInfo tokenInfo){
        return RetrofitBuilder.getFindArttApi().loginGoogle(tokenInfo);
    }

    public static Call<FindArttResponse<UserResult>> signUpGoogle(TokenInfo tokenInfo){
        return RetrofitBuilder.getFindArttApi().signUpGoogle(tokenInfo);
    }

    public static Call<FindArttResponse> logout(String ApiAccessToken){
        return RetrofitBuilder.getFindArttApi().logout(ApiAccessToken);
    }


}
