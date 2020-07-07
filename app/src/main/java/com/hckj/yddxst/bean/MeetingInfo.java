package com.hckj.yddxst.bean;

import java.io.Serializable;

public class MeetingInfo implements Answerable, Serializable {

    /**
     * id : 1
     * pid : 0
     * title : 《中共中央关于加强党的政治建设的意见》
     * desc : 《中共中央关于加强党的政治建设的意见》
     * type : null
     * deleted_at : null
     * created_at : 2019-09-15 07:51:35
     * updated_at : null
     */

    private int id;  // 会议ID
    private String pid;
    private String title; // 会议名称
    private String desc;  // 会议描述
    private String type;  // 会议类型，备用字段
    private String deleted_at;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "MeetingInfo{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    @Override
    public String getDisplayName() {
        return getTitle();
    }
}
