package com.olamide.findartt.enums;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ConnectionStatus {

    public static final String NONE = "NONE";
    public static final String WIFI = "WIFI";
    public static final String MOBILE = "MOBILE";

    @StringDef({NONE, WIFI, MOBILE})
    @Retention(RetentionPolicy.SOURCE)

    public @interface ConnStatus {}

    public final String connectionStatus;

    public ConnectionStatus(@ConnStatus String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }


}
