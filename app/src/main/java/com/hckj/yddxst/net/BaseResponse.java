package com.hckj.yddxst.net;

import java.io.Serializable;

/**
 * 描述：基础响应封装
 * 作者：林明健
 * 日期：2019-08-22 15:58
 */
public class BaseResponse<T> implements Serializable {
    private String level;
    private String message;
    private int page_count;
    private T data;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
