package com.olamide.findartt.models;


import com.google.gson.annotations.SerializedName;
import com.olamide.findartt.enums.ErrorCode;


public class FindArttResponse<T> {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private T data;

    @SerializedName("errorCode")
    private ErrorCode errorCode;

    @SerializedName("lastItem")
    private int lastItem;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public int getLastItem() {
        return lastItem;
    }

    public void setLastItem(int lastItem) {
        this.lastItem = lastItem;
    }
}
