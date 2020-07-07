package com.hckj.yddxst.bean;

import java.io.Serializable;

/**
 * 描述: 会议分类 <br>
 * 日期: 2019-12-19 00:58 <br>
 * 作者: 林明健 <br>
 */
public class MeetingClassifyInfo implements Serializable {

    /**
     * id : 107
     * pid : 0
     * name : 党小组会模块
     * sort : null
     * type : meeting
     * deleted_at : null
     * created_at : null
     * updated_at : null
     * child_classify_list : [{"id":141,"pid":"107","name":"习近平总书记重要讲话","sort":null,"type":"meeting","deleted_at":null,"created_at":"2019-11-22 10:36:21","updated_at":"2019-11-22 10:36:21"},{"id":143,"pid":"107","name":"十八大以来中央全会精神","sort":null,"type":"meeting","deleted_at":null,"created_at":"2019-11-22 10:37:34","updated_at":"2019-11-22 10:37:34"},{"id":144,"pid":"107","name":"十八大以来总书记视察足迹","sort":null,"type":"meeting","deleted_at":null,"created_at":"2019-11-22 10:37:59","updated_at":"2019-11-22 10:37:59"},{"id":145,"pid":"107","name":"重要党内法规制度","sort":null,"type":"meeting","deleted_at":null,"created_at":"2019-11-22 10:38:24","updated_at":"2019-11-22 10:38:24"},{"id":146,"pid":"107","name":"全国两会精神","sort":null,"type":"meeting","deleted_at":null,"created_at":"2019-11-22 10:38:43","updated_at":"2019-11-22 10:38:43"},{"id":147,"pid":"107","name":"大国关系外交","sort":null,"type":"meeting","deleted_at":null,"created_at":"2019-11-22 10:40:17","updated_at":"2019-11-22 10:40:17"},{"id":149,"pid":"107","name":"中央重要会议","sort":null,"type":"meeting","deleted_at":null,"created_at":"2019-11-25 03:01:26","updated_at":"2019-11-25 03:01:26"},{"id":150,"pid":"107","name":"国家区域发展战略","sort":null,"type":"meeting","deleted_at":null,"created_at":"2019-11-25 03:15:41","updated_at":"2019-11-25 03:15:41"}]
     */

    private int id;
    private String pid;
    private String name;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
