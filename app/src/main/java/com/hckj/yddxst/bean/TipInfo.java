package com.hckj.yddxst.bean;

import java.io.Serializable;

public class TipInfo implements Serializable {

    /**
     * id : 2
     * tip : 测试文本
     * deleted_at : null
     * created_at : 2020-02-27 10:47:57
     * updated_at : 2020-02-27 10:47:57
     */

    private String tip;

    public String getTip() {
        return tip == null ? "":tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
