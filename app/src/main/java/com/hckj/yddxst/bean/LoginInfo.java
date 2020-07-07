package com.hckj.yddxst.bean;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private String login_key;
    private String user_key;
    private int user_id;
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

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
