package com.hckj.yddxst.bean;

import java.io.Serializable;
import java.util.List;

public class DocInfo implements Answerable, Serializable {

    /**
     * id : 1
     * classify_id : 74
     * name : 党支部的成立工作要点.docx
     * desc : null
     * file_key : af109322f098858e0e363360a8752ca4
     * file_id : 0
     * type : null
     * sort : 0
     * extension : .docx
     * deleted_at : null
     * created_at : 2019-09-26 20:53:59
     * updated_at : 2019-09-26 20:53:59
     * qrcode_url : http://zddxapi.rzkeji.com/api/document/showQrcode?content=http%3A%2F%2Fzddxapi.rzkeji.com%2FsendEmail%3Fid%3D1
     */

    private int id;
    private String classify_id;
    private String name;
    private String desc;
    private String file_key;
    private String file_id;
    private String type;
    private String sort;
    private String extension;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private String qrcode_url;
    private List<String> img_list;

    public List<String> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<String> img_list) {
        this.img_list = img_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(String classify_id) {
        this.classify_id = classify_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFile_key() {
        return file_key;
    }

    public void setFile_key(String file_key) {
        this.file_key = file_key;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
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

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }

    @Override
    public String getDisplayName() {
        return getName();
    }
}
