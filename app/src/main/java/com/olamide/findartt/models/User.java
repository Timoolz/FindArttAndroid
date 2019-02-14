package com.olamide.findartt.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;
import com.olamide.findartt.enums.Gender;


public class User extends AbstractEntity implements Parcelable {

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

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


    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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


        dest.writeInt(getId());
        dest.writeString(getCreatedDate());
        dest.writeLong(getCreatedDateEpoch());
        dest.writeString(getUpdatedDate());
        dest.writeLong(getUpdatedDateEpoch());
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(imageUrl);
        dest.writeString((dateOfBirth));
        dest.writeLong(dateOfBirthEpoch);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(phone);
        dest.writeByte((byte) (socialSignUp ? 1 : 0));
        dest.writeString(address);
        dest.writeString(country);
        if(gender!=null){
            dest.writeString(gender.getName());
        }
        dest.writeString(socialId);
        dest.writeByte((byte) (verified ? 1 : 0));


    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public User createFromParcel(Parcel in) {
            User userdetails = new User();

            userdetails.setId( in.readInt());
            userdetails.setCreatedDate (in.readString());
            userdetails.setCreatedDateEpoch( in.readLong());
            userdetails.setUpdatedDate(in.readString());
            userdetails.setUpdatedDateEpoch( in.readLong());

            userdetails.email = in.readString();
            userdetails.firstName = in.readString();
            userdetails.lastName = in.readString();
            userdetails.imageUrl = in.readString();
            userdetails.email = in.readString();
            userdetails.dateOfBirth = (in.readString());
            userdetails.dateOfBirthEpoch = in.readLong();
            userdetails.active = in.readByte()!=0;
            userdetails.phone = in.readString();
            userdetails.socialSignUp = in.readByte()!=0;
            userdetails.address = in.readString();
            userdetails.country = in.readString();
            if(in.readString() != null && !in.readString().isEmpty()){
                userdetails.gender = Gender.valueOf(in.readString());
            }
            userdetails.socialId = in.readString();
            userdetails.verified = in.readByte()!=0;


            return userdetails;
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };



}
