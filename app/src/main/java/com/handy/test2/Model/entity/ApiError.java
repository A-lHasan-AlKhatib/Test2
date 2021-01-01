package com.handy.test2.Model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiError {
    @SerializedName("error")
    private String error;

    @Expose(deserialize = false, serialize = false)
    private boolean isRecoverable;

    public ApiError(boolean isRecoverable, String error) {
        this.isRecoverable = isRecoverable;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isRecoverable() {
        return isRecoverable;
    }

    public void setRecoverable(boolean recoverable) {
        isRecoverable = recoverable;
    }
}
