package com.olamide.findartt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import static com.olamide.findartt.Status.*;

/**
 * MVResponse holder provided to the UI
 */
public class MVResponse {

    public final Status status;

    @Nullable
    public final Object data;

    @Nullable
    public final Throwable error;

    private MVResponse(Status status, @Nullable Object data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static MVResponse loading() {
        return new MVResponse(LOADING, null, null);
    }

    public static MVResponse success(@NonNull Object data) {
        return new MVResponse(SUCCESS, data, null);
    }

    public static MVResponse error(@NonNull Throwable error) {
        return new MVResponse(ERROR, null, error);
    }
}
