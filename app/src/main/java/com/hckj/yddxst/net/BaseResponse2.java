package com.hckj.yddxst.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponse2<T> implements Serializable {
    private String level;
    private String message;
    private int page_count;
    @SerializedName("data_v2")
    private T data;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }
}
