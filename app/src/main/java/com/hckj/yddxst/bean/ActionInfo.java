package com.hckj.yddxst.bean;

import java.io.Serializable;

public class ActionInfo implements Serializable {
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ActionInfo(String action) {
        this.action = action;
    }
}
