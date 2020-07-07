package com.hckj.yddxst.bean;

import java.io.Serializable;

public class CourseInfo implements Serializable {

    /**
     * id : 1
     * num : 1
     * form : 动画
     * type : 时政
     * title : 0001-时政-党的建设面临的形式和新时代党的建设总要求
     * from : 共产党员网
     * question : 党的建设面临的形势是什么？
     如何正确认识全面从严治党？
     “四大考验”是什么？
     “四大危险”是什么？
     新时代党的建设总要求是什么？
     新时代党的建设总要求的作用是什么？
     * desc : 打铁必须自身硬，党要团结带领人民，推进伟大事业，实现伟大梦想，必须毫不动摇坚持和完善党的领导，毫不动摇把党建设得更加坚强有力，对此我们要认识和把握好两个方面的问题。第一，清醒认识党的建设面临的形式；第二，把握新时代党的建设总要求。
     * time_length : 3min3s
     * link_url : http://dygbjy.12371.cn/2018/10/12/VIDE1539326120717167.shtml
     * remark : null
     * is_get : 1
     * deleted_at : null
     * created_at : 2019-03-13 20:32:59
     * updated_at : 2019-03-13 20:52:46
     * video_page_url : http://zddxapi.rzkeji.com/video/showVideo?id=1
     */

    private int id;
    private String num;
    private String form;
    private String type;
    private String title;
    private String from;
    private String question;
    private String desc;
    private String time_length;
    private String link_url;
    private Object remark;
    private String is_get;
    private Object deleted_at;
    private String created_at;
    private String updated_at;
    private String video_page_url;
    private String android_video_url;
    private String model;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAndroid_video_url() {
        return android_video_url;
    }

    public void setAndroid_video_url(String android_video_url) {
        this.android_video_url = android_video_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime_length() {
        return time_length;
    }

    public void setTime_length(String time_length) {
        this.time_length = time_length;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getIs_get() {
        return is_get;
    }

    public void setIs_get(String is_get) {
        this.is_get = is_get;
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

    public String getVideo_page_url() {
        return video_page_url;
    }

    public void setVideo_page_url(String video_page_url) {
        this.video_page_url = video_page_url;
    }
}
