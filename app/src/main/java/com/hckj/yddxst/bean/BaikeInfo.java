package com.hckj.yddxst.bean;

public class BaikeInfo {

    /**
     * id : 192
     * user_id : 0
     * img_id : 0
     * title : 支部委员会向支部大会报告对发展对象的审查情况，一般应包括哪些内容？
     * desc :
     * link_url :
     * content : <p>支部委员会向支部大会报告对发展对象的审查情况，一般应包括以下内容：</p><p><br/></p><p>（1）发展对象的基本情况和现实表现。</p><p><br/></p><p>（2）对发展对象的政治历史和在重大政治斗争中的表现、遵纪守法和遵守社会公德、直系亲属和主要社会关系政治情况的审查情况。</p><p><br/></p><p>（3）征求党员和群众意见的情况。</p><p><br/></p><p>（4）基层党委对发展对象的预审情况。</p><p><br/></p><p>（5）其他需要向支部大会说明的情况。</p><p><br/></p>
     * read_num : 0
     * classify_id : 269
     * status : 0
     * sort : 0
     * deleted_at : null
     * created_at : 2020-06-03 12:06:28
     * updated_at : 2020-06-03 12:06:28
     * read_url : http://zddxapi.rzkeji.com/readBaike?id=192
     */

    private int id;
    private String title;
    private String read_url;

    public String getQrcode_url() {
        return qrcode_url;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }

    private String qrcode_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRead_url() {
        return read_url;
    }

    public void setRead_url(String read_url) {
        this.read_url = read_url;
    }
}
