package com.olamide.findartt.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;
import com.olamide.findartt.enums.Gender;
import com.olamide.findartt.utils.Converters;


import java.time.LocalDateTime;


public class User extends AbstractEntity implements Parcelable {

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("dateOfBirth")
    private LocalDateTime dateOfBirth;

    @SerializedName("dateOfBirthEpoch")
    private Long dateOfBirthEpoch;

    @SerializedName("active")
    private boolean active;

    @SerializedName("phone")
    private String phone;

    @SerializedName("socialSignUp")
    private boolean socialSignUp;

    @SerializedName("address")
    private String address;

    @SerializedName("country")
    private String country;

    @SerializedName("gender")
    private Gender gender;

    @SerializedName("socialId")
    private String socialId;

    @SerializedName("verified")
    private boolean verified;



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSocialSignUp() {
        return socialSignUp;
    }

    public void setSocialSignUp(boolean socialSignUp) {
        this.socialSignUp = socialSignUp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getDateOfBirthEpoch() {
        return dateOfBirthEpoch;
    }

    public void setDateOfBirthEpoch(Long dateOfBirthEpoch) {
        this.dateOfBirthEpoch = dateOfBirthEpoch;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }



    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(imageUrl);
        dest.writeLong(Converters.toEpochLong(dateOfBirth));
        dest.writeLong(dateOfBirthEpoch);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(phone);
        dest.writeByte((byte) (socialSignUp ? 1 : 0));
        dest.writeString(address);
        dest.writeString(country);
        dest.writeString(gender.getName());
        dest.writeString(socialId);
        dest.writeByte((byte) (verified ? 1 : 0));


    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public User createFromParcel(Parcel in) {
            User user = new User();
            user.email = in.readString();
            user.firstName = in.readString();
            user.lastName = in.readString();
            user.imageUrl = in.readString();
            user.email = in.readString();
            user.dateOfBirth = Converters.toDateTime(in.readLong());
            user.dateOfBirthEpoch = in.readLong();
            user.active = in.readByte()!=0;
            user.phone = in.readString();
            user.socialSignUp = in.readByte()!=0;
            user.address = in.readString();
            user.country = in.readString();
            user.gender = Gender.valueOf(in.readString());
            user.socialId = in.readString();
            user.verified = in.readByte()!=0;


            return user;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };



}
