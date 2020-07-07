package com.hckj.yddxst.bean;

import java.io.Serializable;

public class StudyBtnInfo implements Serializable {

    /**
     * button_name : 学习强国
     * type : web
     * link_url : https://www.xuexi.cn/
     */

    private String button_name;
    private String type;
    private String link_url;

    public String getButton_name() {
        return button_name;
    }

    public void setButton_name(String button_name) {
        this.button_name = button_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }
}
