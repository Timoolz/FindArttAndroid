package com.olamide.findartt.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.olamide.findartt.enums.PurchaseType;

import java.util.Date;

@Entity(tableName = "artwork")
public class Artwork extends AbstractEntity  implements Parcelable {

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "image_url")
    @SerializedName("imageUrl")
    private String imageUrl;

    @ColumnInfo(name = "video_url")
    @SerializedName("videoUrl")
    private String videoUrl;

    @ColumnInfo(name = "purchase_type")
    @SerializedName("purchaseType")
    private PurchaseType purchaseType;

    @ColumnInfo(name = "minimum_amount")
    @SerializedName("minimumAmount")
    private Double minimumAmount;

    @ColumnInfo(name = "created_by")
    @SerializedName("createdBy")
    private Integer createdBy;

    @ColumnInfo(name = "posted")
    @SerializedName("posted")
    private boolean posted;

    //For Room Db
    @ColumnInfo(name = "created_at")
    private Date createdAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(videoUrl);
        dest.writeString(purchaseType.getName());
        dest.writeDouble(minimumAmount);
        dest.writeInt(createdBy);
        dest.writeByte((byte) (posted ? 1 : 0));
//        if(createdAt!=null){
//            dest.writeLong(Converters.toTimestamp(createdAt));
//        }

    }


    public static final Creator<Artwork> CREATOR = new Creator<Artwork>() {
        @Override
        public Artwork createFromParcel(Parcel in) {
            Artwork artwork = new Artwork();
            artwork.setId( in.readInt());
            artwork.setCreatedDate (in.readString());
            artwork.setCreatedDateEpoch( in.readLong());
            artwork.setUpdatedDate(in.readString());
            artwork.setUpdatedDateEpoch( in.readLong());
            
            artwork.name = in.readString();
            artwork.description = in.readString();
            artwork.imageUrl = in.readString();
            artwork.videoUrl = in.readString();
            artwork.purchaseType = PurchaseType.valueOf(in.readString());
            artwork.minimumAmount = in.readDouble();
            artwork.createdBy = in.readInt();
            artwork.posted = in.readByte() !=0;
//            artwork.createdAt = Converters.toDate(in.readLong());

            return artwork;
        }

        @Override
        public Artwork[] newArray(int size) {
            return new Artwork[size];
        }
    };





}
