package com.olamide.findartt.models;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ArtworkSummary extends Artwork implements Parcelable {

    @SerializedName("currentBuy")
    private Buy currentBuy;

    @SerializedName("currentBid")
    private Bid currentBid;

    @SerializedName("acceptedBid")
    private Bid acceptedBid;

    @SerializedName("bids")
    private List<Bid> bids;

    public Buy getCurrentBuy() {
        return currentBuy;
    }

    public void setCurrentBuy(Buy currentBuy) {
        this.currentBuy = currentBuy;
    }

    public Bid getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(Bid currentBid) {
        this.currentBid = currentBid;
    }

    public Bid getAcceptedBid() {
        return acceptedBid;
    }

    public void setAcceptedBid(Bid acceptedBid) {
        this.acceptedBid = acceptedBid;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(currentBuy,flags);
        dest.writeParcelable(currentBid,flags);
        dest.writeParcelable(acceptedBid,flags);
        dest.writeTypedList( bids);


    }


    public static final Creator<ArtworkSummary> CREATOR = new Creator<ArtworkSummary>() {
        @Override
        public ArtworkSummary createFromParcel(Parcel in) {
            ArtworkSummary artworkSummary = new ArtworkSummary();
            List<Bid> bidList = new ArrayList<>();
            artworkSummary.currentBuy = in.readParcelable(Buy.class.getClassLoader());
            artworkSummary.currentBid = in.readParcelable(Bid.class.getClassLoader());
            artworkSummary.acceptedBid = in.readParcelable(Bid.class.getClassLoader());
            in.readTypedList(bidList, Bid.CREATOR);
            artworkSummary.bids = bidList;

            return artworkSummary;
        }

        @Override
        public ArtworkSummary[] newArray(int size) {
            return new ArtworkSummary[size];
        }
    };





}
