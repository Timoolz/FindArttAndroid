package com.olamide.findartt.models;


import android.os.Build;
import android.os.Parcel;
import android.support.annotation.RequiresApi;


import com.google.gson.annotations.SerializedName;

public class Buy extends AbstractEntity {

    @SerializedName("artworkId")
    private Integer artworkId;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("madeBy")
    private Integer madeBy;


    public Integer getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(Integer artworkId) {
        this.artworkId = artworkId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getMadeBy() {
        return madeBy;
    }

    public void setMadeBy(Integer madeBy) {
        this.madeBy = madeBy;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(artworkId);
        dest.writeDouble(amount);
        dest.writeInt(madeBy);

    }


    public static final Creator<Buy> CREATOR = new Creator<Buy>() {
        @Override
        public Buy createFromParcel(Parcel in) {
            Buy buy = new Buy();
            buy.artworkId = in.readInt();
            buy.amount = in.readDouble();
            buy.madeBy = in.readInt();

            return buy;
        }

        @Override
        public Buy[] newArray(int size) {
            return new Buy[size];
        }
    };


}

