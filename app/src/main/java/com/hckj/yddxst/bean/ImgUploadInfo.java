package com.hckj.yddxst.bean;

import java.io.Serializable;

public class ImgUploadInfo implements Serializable {

    /**
     * file_id : 10522
     * img_name : 1573376772844168.jpeg
     * img_type : .jpeg
     * updated_at : 2019-11-10 17:09:05
     * created_at : 2019-11-10 17:09:05
     * id : 5460
     * img_url : http://file.rzkeji.com/uploads/2019/11/10/e57c77446775dfcb572f9826b96f4aa6.jpeg
     */

    private String file_id;
    private String img_name;
    private String img_type;
    private String updated_at;
    private String created_at;
    private String id;
    private String img_url;

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getImg_type() {
        return img_type;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "ImgUploadInfo{" +
                "file_id='" + file_id + '\'' +
                ", img_name='" + img_name + '\'' +
                ", img_type='" + img_type + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", id='" + id + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }
}
