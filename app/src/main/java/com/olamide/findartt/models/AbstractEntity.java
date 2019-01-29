package com.olamide.findartt.models;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;
import com.olamide.findartt.utils.Converters;

import java.time.LocalDateTime;


public class AbstractEntity implements Parcelable {


    @SerializedName("id")
    private Integer id;

    @SerializedName("createdDate")
    private LocalDateTime createdDate;

    @SerializedName("createdDateEpoch")
    private Long createdDateEpoch;

    @SerializedName("updatedDate")
    private LocalDateTime updatedDate;

    @SerializedName("updatedDateEpoch")
    private Long updatedDateEpoch;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;

    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getCreatedDateEpoch() {
        return createdDateEpoch;
    }

    public void setCreatedDateEpoch(Long createdDateEpoch) {
        this.createdDateEpoch = createdDateEpoch;
    }

    public void setUpdatedDateEpoch(Long updatedDateEpoch) {
        this.updatedDateEpoch = updatedDateEpoch;
    }

    public Long getUpdatedDateEpoch() {
        return updatedDateEpoch;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeLong(Converters.toEpochLong(createdDate));
        dest.writeLong(createdDateEpoch);
        dest.writeLong(Converters.toEpochLong(updatedDate));
        dest.writeLong(updatedDateEpoch);

    }


    public static final Creator<AbstractEntity> CREATOR = new Creator<AbstractEntity>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public AbstractEntity createFromParcel(Parcel in) {
            AbstractEntity abstractEntity = new AbstractEntity();
            abstractEntity.id = in.readInt();
            abstractEntity.createdDate = Converters.toDateTime(in.readLong());
            abstractEntity.createdDateEpoch = in.readLong();
            abstractEntity.updatedDate = Converters.toDateTime(in.readLong());
            abstractEntity.updatedDateEpoch = in.readLong();

            return abstractEntity;
        }

        @Override
        public AbstractEntity[] newArray(int size) {
            return new AbstractEntity[size];
        }
    };


}