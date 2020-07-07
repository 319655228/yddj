package com.hckj.yddxst.bean;

import java.io.Serializable;

public class MenuAuthInfo implements Serializable {

    /**
     * id : 17
     * device_id : 3
     * system_info_id : 1
     * status : 1
     * deleted_at : null
     * created_at : 2020-01-14 17:54:56
     * updated_at : 2020-01-14 17:54:56
     * name : 三会一课主持人
     * tag : meeting
     * desc : null
     */

    private int id;
    private String device_id;
    private String system_info_id;
    private String status;
    private String name;
    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getSystem_info_id() {
        return system_info_id;
    }

    public void setSystem_info_id(String system_info_id) {
        this.system_info_id = system_info_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
