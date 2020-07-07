package com.hckj.yddxst.bean;

import java.io.Serializable;

public class BuildingInfo implements Serializable {

    /**
     * id : 12
     * page_ident : null
     * page_url : null
     * device_num : null
     * device_id : 11
     * building_id : 0
     * button_name : 来穗服务
     * icon_img_id : 347
     * icon_img_url : http://zddxapi.rzkeji.com/api/file/show-image/347
     * link_url : http://lsj.gz.gov.cn/lsnew/bszn/list.shtml
     * desc :
     * content :
     * conten_type : null
     * type : unity_web_view
     * sort : 3
     * deleted_at : null
     * created_at : 2019-05-07 18:29:56
     * updated_at : 2019-05-08 12:43:56
     */

    private int id;
    private Object page_ident;
    private Object page_url;
    private Object device_num;
    private String device_id;
    private String building_id;
    private String button_name;
    private String icon_img_id;
    private String icon_img_url;
    private String link_url;
    private String desc;
    private String content;
    private Object conten_type;
    private String type;
    private String sort;
    private Object deleted_at;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getPage_ident() {
        return page_ident;
    }

    public void setPage_ident(Object page_ident) {
        this.page_ident = page_ident;
    }

    public Object getPage_url() {
        return page_url;
    }

    public void setPage_url(Object page_url) {
        this.page_url = page_url;
    }

    public Object getDevice_num() {
        return device_num;
    }

    public void setDevice_num(Object device_num) {
        this.device_num = device_num;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getButton_name() {
        return button_name;
    }

    public void setButton_name(String button_name) {
        this.button_name = button_name;
    }

    public String getIcon_img_id() {
        return icon_img_id;
    }

    public void setIcon_img_id(String icon_img_id) {
        this.icon_img_id = icon_img_id;
    }

    public String getIcon_img_url() {
        return icon_img_url;
    }

    public void setIcon_img_url(String icon_img_url) {
        this.icon_img_url = icon_img_url;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getConten_type() {
        return conten_type;
    }

    public void setConten_type(Object conten_type) {
        this.conten_type = conten_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
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
}
