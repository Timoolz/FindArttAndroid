package com.olamide.findartt.models;


import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


public class AbstractEntity /*implements Parcelable*/ {

@PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    protected Integer id;

    @ColumnInfo(name = "created_date")
    @SerializedName("createdDate")
    protected String createdDate;

    @ColumnInfo(name = "created_date_epoch")
    @SerializedName("createdDateEpoch")
    protected Long createdDateEpoch;

    @ColumnInfo(name = "updated_date")
    @SerializedName("updatedDate")
    protected String updatedDate;

    @ColumnInfo(name = "updated_date_epoch")
    @SerializedName("updatedDateEpoch")
    protected Long updatedDateEpoch;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;

    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
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


//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//        dest.writeInt(getId());
//        dest.writeString(getCreatedDate());
//        dest.writeLong(getCreatedDateEpoch());
//        dest.writeString(getUpdatedDate());
//        dest.writeLong(getUpdatedDateEpoch());
//
//    }
//
//
//    public static final Creator<AbstractEntity> CREATOR = new Creator<AbstractEntity>() {
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        @Override
//        public AbstractEntity createFromParcel(Parcel in) {
//            AbstractEntity abstractEntity = new AbstractEntity();
//            abstractEntity.id = in.readInt();
//            abstractEntity.createdDate = (in.readString());
//            abstractEntity.createdDateEpoch = in.readLong();
//            abstractEntity.updatedDate = (in.readString());
//            abstractEntity.updatedDateEpoch = in.readLong();
//
//            return abstractEntity;
//        }
//
//        @Override
//        public AbstractEntity[] newArray(int size) {
//            return new AbstractEntity[size];
//        }
//    };


}