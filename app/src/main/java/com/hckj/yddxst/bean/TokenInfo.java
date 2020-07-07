package com.hckj.yddxst.bean;

import java.io.Serializable;

/**
 * 描述：Tts Token
 * 作者：林明健
 * 日期：2019-12-02 9:20
 */
public class TokenInfo implements Serializable {

    /**
     * access_token : 4088a67b-4891-4f54-9fd5-fd2ea1f54521
     * token_type : bearer
     * expires_in : 34514
     * scope : tts
     */
    private String access_token;
    private String token_type;
    private int expires_in;
    private String scope;
    /**
     * err_msg : authenticate fail, access_token is invalid
     * err_no : 30000
     * log_id : 3007986223
     * sn : 643c0e0a-313a-4cb6-b200-a8fbb316f3b7
     */

    private String err_msg;
    private int err_no;
    private long log_id;
    private String sn;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public int getErr_no() {
        return err_no;
    }

    public void setErr_no(int err_no) {
        this.err_no = err_no;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
