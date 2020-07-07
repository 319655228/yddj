package com.hckj.yddxst.bean;

import java.io.Serializable;

public class MeetingStartInfo implements Serializable {

    /**
     * meeting_id : 1
     * device_id : 3
     * key : 6e3ebea9f28ff42d79e77d407b4933e7
     * s_client_id : 222
     * status : 1
     * updated_at : 2019-09-19 23:04:21
     * created_at : 2019-09-19 23:04:21
     * id : 24
     * qrcode_url : http://zddxapi.rzkeji.com/api/meeting/showQrcode?key=6e3ebea9f28ff42d79e77d407b4933e7
     */

    private String meeting_id;  // 会员ID
    private int device_id;  // 设备ID
    private String key; // 会议Key
    private String s_client_id; //会议的socket链接编号
    private int status; // 会议状态
    private String updated_at;
    private String created_at;
    private int id;
    private String qrcode_url; // 二维码
    private String join_number;// 口令号

    public String getJoin_number() {
        return join_number;
    }

    public void setJoin_number(String join_number) {
        this.join_number = join_number;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getS_client_id() {
        return s_client_id;
    }

    public void setS_client_id(String s_client_id) {
        this.s_client_id = s_client_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }
}
