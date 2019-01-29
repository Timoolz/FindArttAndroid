package com.olamide.findartt.models;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;
import com.olamide.findartt.enums.PurchaseType;
import com.olamide.findartt.utils.Converters;

public class Artwork extends AbstractEntity  implements Parcelable {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("videoUrl")
    private String videoUrl;

    @SerializedName("purchaseType")
    private PurchaseType purchaseType;

    @SerializedName("minimumAmount")
    private Double minimumAmount;

    @SerializedName("createdBy")
    private Integer createdBy;

    @SerializedName("posted")
    private boolean posted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(PurchaseType purchaseType) {
        this.purchaseType = purchaseType;
    }

    public Double getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Double minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(videoUrl);
        dest.writeString(purchaseType.getName());
        dest.writeDouble(minimumAmount);
        dest.writeInt(createdBy);
        dest.writeByte((byte) (posted ? 1 : 0));

    }


    public static final Creator<Artwork> CREATOR = new Creator<Artwork>() {
        @Override
        public Artwork createFromParcel(Parcel in) {
            Artwork artwork = new Artwork();
            artwork.name = in.readString();
            artwork.description = in.readString();
            artwork.imageUrl = in.readString();
            artwork.videoUrl = in.readString();
            artwork.purchaseType = PurchaseType.valueOf(in.readString());
            artwork.minimumAmount = in.readDouble();
            artwork.createdBy = in.readInt();
            artwork.posted = in.readByte() !=0;

            return artwork;
        }

        @Override
        public Artwork[] newArray(int size) {
            return new Artwork[size];
        }
    };





}
