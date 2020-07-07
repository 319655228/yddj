package com.hckj.yddxst.bean;

import java.io.Serializable;
import java.util.List;

public class AnswerInfo implements Serializable {
    private String keyword;
    private List<VideoInfo> list;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<VideoInfo> getList() {
        return list;
    }

    public void setList(List<VideoInfo> list) {
        this.list = list;
    }
}
