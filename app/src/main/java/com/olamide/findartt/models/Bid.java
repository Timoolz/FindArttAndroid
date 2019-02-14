package com.olamide.findartt.models;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;
import com.olamide.findartt.enums.BidStatus;

public class Bid extends AbstractEntity implements Parcelable {

    @SerializedName("artworkId")
    private Integer artworkId;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("madeBy")
    private Integer madeBy;

    @SerializedName("bidStatus")
    private BidStatus bidStatus;


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

    public BidStatus getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
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
        dest.writeString(bidStatus.getName());


    }


    public static final Creator<Bid> CREATOR = new Creator<Bid>() {
        @Override
        public Bid createFromParcel(Parcel in) {
            Bid bid = new Bid();

            bid.setId( in.readInt());
            bid.setCreatedDate (in.readString());
            bid.setCreatedDateEpoch( in.readLong());
            bid.setUpdatedDate(in.readString());
            bid.setUpdatedDateEpoch( in.readLong());

            bid.artworkId = in.readInt();
            bid.amount = in.readDouble();
            bid.madeBy = in.readInt();
            bid.bidStatus = BidStatus.valueOf(in.readString());


            return bid;
        }

        @Override
        public Bid[] newArray(int size) {
            return new Bid[size];
        }
    };

}
