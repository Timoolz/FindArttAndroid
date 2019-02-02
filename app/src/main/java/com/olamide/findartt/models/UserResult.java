package com.olamide.findartt.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserResult implements Parcelable{

    @SerializedName("tokenInfo")
    private TokenInfo tokenInfo;
    
    @SerializedName("userDetails")
    private User user;


   

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(tokenInfo, flags);
        dest.writeParcelable(user,flags);

    }


    public static final Parcelable.Creator<UserResult> CREATOR = new Parcelable.Creator<UserResult>() {
        @Override
        public UserResult createFromParcel(Parcel in) {
            UserResult userResult = new UserResult();
            userResult.tokenInfo = in.readParcelable(TokenInfo.class.getClassLoader());
            userResult.user = in.readParcelable(User.class.getClassLoader());

            return userResult;
        }

        @Override
        public UserResult[] newArray(int size) {
            return new UserResult[size];
        }
    };



}
