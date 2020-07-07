package com.hckj.yddxst.bean;

import java.io.Serializable;

public class VideoInfo implements Answerable, Serializable {
    private String title;

    private String android_video_url;

    private String model;

    private String desc;

    private String video_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAndroid_video_url() {
        return android_video_url;
    }

    public void setAndroid_video_url(String android_video_url) {
        this.android_video_url = android_video_url;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public VideoInfo() {

    }

    public VideoInfo(CourseInfo courseInfo) {
        setTitle(courseInfo.getTitle());
        setAndroid_video_url(courseInfo.getAndroid_video_url());
        setDesc(courseInfo.getDesc());
        setModel(courseInfo.getModel());
    }

    public static VideoInfo from(CourseInfo courseInfo) {
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setTitle(courseInfo.getTitle());
        videoInfo.setAndroid_video_url(courseInfo.getAndroid_video_url());
        videoInfo.setDesc(courseInfo.getDesc());
        videoInfo.setModel(courseInfo.getModel());
        return videoInfo;
    }

    @Override
    public String getDisplayName() {
        return getTitle();
    }
}
