package com.hckj.yddxst.bean;

import java.util.List;


/**
 * 百科分类信息
 */
public class BaikeClassifyInfo {

    /**
     * id : 259
     * pid : 0
     * name : 党章党规
     * sort : null
     * type : baike
     * deleted_at : null
     * created_at : 2020-06-01 14:34:13
     * updated_at : 2020-06-01 14:41:21
     * child_classify_list : [{"id":260,"pid":"259","name":"党章","sort":null,"type":"baike","deleted_at":null,"created_at":"2020-06-01 14:41:54","updated_at":"2020-06-01 14:41:54"},{"id":262,"pid":"259","name":"准则","sort":null,"type":"baike","deleted_at":null,"created_at":"2020-06-02 14:34:01","updated_at":"2020-06-02 14:34:01"},{"id":263,"pid":"259","name":"条例","sort":null,"type":"baike","deleted_at":null,"created_at":"2020-06-02 14:41:18","updated_at":"2020-06-02 14:41:18"},{"id":264,"pid":"259","name":"规定","sort":null,"type":"baike","deleted_at":null,"created_at":"2020-06-02 17:06:51","updated_at":"2020-06-02 17:06:51"},{"id":265,"pid":"259","name":"办法","sort":null,"type":"baike","deleted_at":null,"created_at":"2020-06-03 10:39:53","updated_at":"2020-06-03 10:39:53"},{"id":266,"pid":"259","name":"规则","sort":null,"type":"baike","deleted_at":null,"created_at":"2020-06-03 11:19:11","updated_at":"2020-06-03 11:19:11"},{"id":267,"pid":"259","name":"细则","sort":null,"type":"baike","deleted_at":null,"created_at":"2020-06-03 11:27:55","updated_at":"2020-06-03 11:27:55"}]
     */

    private int id;
    private String pid;
    private String name;
    private String type;
    private List<BaikeClassifyInfo> child_classify_list;

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


    public List<BaikeClassifyInfo> getChild_classify_list() {
        return child_classify_list;
    }

    public void setChild_classify_list(List<BaikeClassifyInfo> child_classify_list) {
        this.child_classify_list = child_classify_list;
    }
}
