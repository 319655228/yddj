package com.hckj.yddxst.bean;

import java.io.Serializable;
import java.util.List;

public class ClassifyInfo implements Serializable {
    private String type;
    private List<CourseInfo> list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CourseInfo> getList() {
        return list;
    }

    public void setList(List<CourseInfo> list) {
        this.list = list;
    }
}
