package com.hckj.yddxst.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SocketInfo implements Serializable {

    /**
     * handle : int
     * client_id : client_id
     */
    

    private String handle;
    private String client_id;
    /**
     * data : {"id":1552,"user_id":"1552","photo_id":"3187","face_img_id":"0","picture":null,"nickname":"神经蛙","realname":null,"sex":"1","constellation":null,"province":"广东","city":"广州","area":null,"tag":null,"qq":null,"wechat":null,"description":null,"integral":"10","party_branch":null,"join_party_date":null,"birthday":null,"created_at":"2019-10-18 20:10:34","updated_at":"2019-10-18 20:10:34","deleted_at":null,"photo_url":"https://api.rzkeji.com/api/file/show-image/3187","client_id":"7f0000010b57000005c9"}
     */

    private Content data;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString() {
        return "SocketInfo{" +
                "handle='" + handle + '\'' +
                ", client_id='" + client_id + '\'' +
                '}';
    }

    public Content getData() {
        return data;
    }

    public void setData(Content data) {
        this.data = data;
    }

    public static class Content {
        /**
         * id : 1552
         * user_id : 1552
         * photo_id : 3187
         * face_img_id : 0
         * picture : null
         * nickname : 神经蛙
         * realname : null
         * sex : 1
         * constellation : null
         * province : 广东
         * city : 广州
         * area : null
         * tag : null
         * qq : null
         * wechat : null
         * description : null
         * integral : 10
         * party_branch : null
         * join_party_date : null
         * birthday : null
         * created_at : 2019-10-18 20:10:34
         * updated_at : 2019-10-18 20:10:34
         * deleted_at : null
         * photo_url : https://api.rzkeji.com/api/file/show-image/3187
         * client_id : 7f0000010b57000005c9
         */

        private int id;
        private String user_id;
        private String photo_id;
        private String face_img_id;
        private String picture;
        private String nickname;
        private String realname;
        private String sex;
        private String constellation;
        private String province;
        private String city;
        private String area;
        private String tag;
        private String qq;
        private String wechat;
        private String description;
        private String integral;
        private String party_branch;
        private String join_party_date;
        private String birthday;
        private String created_at;
        private String updated_at;
        private String deleted_at;
        private String photo_url;
        @SerializedName("client_id")
        private String client_idX;
        private int join_mode;

        /**
         * chat_content : 得到
         * chat_time : 2019-10-19 14:17:55
         */

        private String chat_content;
        private String chat_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPhoto_id() {
            return photo_id;
        }

        public void setPhoto_id(String photo_id) {
            this.photo_id = photo_id;
        }

        public String getFace_img_id() {
            return face_img_id;
        }

        public void setFace_img_id(String face_img_id) {
            this.face_img_id = face_img_id;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getParty_branch() {
            return party_branch;
        }

        public void setParty_branch(String party_branch) {
            this.party_branch = party_branch;
        }

        public String getJoin_party_date() {
            return join_party_date;
        }

        public void setJoin_party_date(String join_party_date) {
            this.join_party_date = join_party_date;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getPhoto_url() {
            return photo_url;
        }

        public void setPhoto_url(String photo_url) {
            this.photo_url = photo_url;
        }

        public String getClient_idX() {
            return client_idX;
        }

        public void setClient_idX(String client_idX) {
            this.client_idX = client_idX;
        }

        public String getChat_content() {
            return chat_content;
        }

        public void setChat_content(String chat_content) {
            this.chat_content = chat_content;
        }

        public String getChat_time() {
            return chat_time;
        }

        public void setChat_time(String chat_time) {
            this.chat_time = chat_time;
        }

        public int getJoin_mode() {
            return join_mode;
        }

        public void setJoin_mode(int join_mode) {
            this.join_mode = join_mode;
        }
    }
}
