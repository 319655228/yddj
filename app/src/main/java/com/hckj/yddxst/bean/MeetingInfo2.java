package com.hckj.yddxst.bean;

import java.io.Serializable;
import java.util.List;

public class MeetingInfo2 implements Serializable {

    private List<MeetingInfo> model;
    private List<MeetingInfo> currency;

    public List<MeetingInfo> getModel() {
        return model;
    }

    public void setModel(List<MeetingInfo> model) {
        this.model = model;
    }

    public List<MeetingInfo> getCurrency() {
        return currency;
    }

    public void setCurrency(List<MeetingInfo> currency) {
        this.currency = currency;
    }
}
