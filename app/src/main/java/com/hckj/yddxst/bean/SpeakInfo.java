package com.hckj.yddxst.bean;

import java.io.Serializable;

public class SpeakInfo implements Serializable {

    /**
     * id : 1
     * speak : 同志，祝您新的一天工作顺利哦！
     * action : {"id":29,"action":"点赞","deleted_at":null,"created_at":null,"updated_at":null}
     */

    private int id;
    private String speak;
    private ActionInfo action;

    public SpeakInfo(String speak, ActionInfo action) {
        this.id = (int) (1000 * Math.random());
        this.speak = speak;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpeak() {
        return speak;
    }

    public void setSpeak(String speak) {
        this.speak = speak;
    }

    public ActionInfo getAction() {
        return action;
    }

    public void setAction(ActionInfo action) {
        this.action = action;
    }
}
