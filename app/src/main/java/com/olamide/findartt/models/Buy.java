package com.olamide.findartt.models;


import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

public class Buy extends AbstractEntity implements Parcelable {

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


        dest.writeInt(getId());
        dest.writeString(getCreatedDate());
        dest.writeLong(getCreatedDateEpoch());
        dest.writeString(getUpdatedDate());
        dest.writeLong(getUpdatedDateEpoch());
        dest.writeInt(artworkId);
        dest.writeDouble(amount);
        dest.writeInt(madeBy);

    }


    public static final Creator<Buy> CREATOR = new Creator<Buy>() {
        @Override
        public Buy createFromParcel(Parcel in) {
            Buy buy = new Buy();

            buy.setId( in.readInt());
            buy.setCreatedDate (in.readString());
            buy.setCreatedDateEpoch( in.readLong());
            buy.setUpdatedDate(in.readString());
            buy.setUpdatedDateEpoch( in.readLong());

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

