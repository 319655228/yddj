package com.hckj.yddxst.bean;

import java.io.Serializable;
import java.util.List;

public class DocClassifyInfo implements Serializable {

   
    /**
     * id : 70
     * pid : 0
     * name : 组织设置
     * sort : null
     * type : document
     * deleted_at : null
     * created_at : null
     * updated_at : null
     * child_classify_list : [{"id":74,"pid":"70","name":"党支部的设立","sort":null,"type":"document","deleted_at":null,"created_at":null,"updated_at":null},{"id":75,"pid":"70","name":"党支部的拆分","sort":null,"type":"document","deleted_at":null,"created_at":null,"updated_at":null},{"id":76,"pid":"70","name":"党支部的撤销","sort":null,"type":"document","deleted_at":null,"created_at":null,"updated_at":null},{"id":77,"pid":"70","name":"党小组的设立","sort":null,"type":"document","deleted_at":null,"created_at":null,"updated_at":null},{"id":78,"pid":"70","name":"党支委的产生、换届、增补","sort":null,"type":"document","deleted_at":null,"created_at":null,"updated_at":null},{"id":79,"pid":"70","name":"工作模板","sort":null,"type":"document","deleted_at":null,"created_at":null,"updated_at":null}]
     */

    private int id;
    private String pid;
    private String name;
    private String sort;
    private String type;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private List<DocClassifyInfo> child_classify_list;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public List<DocClassifyInfo> getChild_classify_list() {
        return child_classify_list;
    }

    public void setChild_classify_list(List<DocClassifyInfo> child_classify_list) {
        this.child_classify_list = child_classify_list;
    }


}
