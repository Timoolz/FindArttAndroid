package com.olamide.findartt.utils.network;

import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;

import java.util.List;

import retrofit2.Call;

public class FindArttService {


    public static Call<FindArttResponse<UserResult>> login(UserLogin userLogin){
       return RetrofitBuilder.getFindArttApi().login(userLogin);
    }
}
