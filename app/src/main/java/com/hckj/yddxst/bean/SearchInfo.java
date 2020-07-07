package com.hckj.yddxst.bean;

import java.io.Serializable;

public class SearchInfo implements Serializable {
    private String login_key;
    private String url;
    private String qrcode_login_url;
    private int is_exist;
    private String is_check_in;
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getLogin_key() {
        return login_key;
    }

    public void setLogin_key(String login_key) {
        this.login_key = login_key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQrcode_login_url() {
        return qrcode_login_url;
    }

    public void setQrcode_login_url(String qrcode_login_url) {
        this.qrcode_login_url = qrcode_login_url;
    }

    public int getIs_exist() {
        return is_exist;
    }

    public void setIs_exist(int is_exist) {
        this.is_exist = is_exist;
    }

    public String getIs_check_in() {
        return is_check_in;
    }

    public void setIs_check_in(String is_check_in) {
        this.is_check_in = is_check_in;
    }
}
