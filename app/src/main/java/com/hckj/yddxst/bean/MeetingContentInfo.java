package com.hckj.yddxst.bean;

import java.io.Serializable;
import java.util.LinkedList;

public class MeetingContentInfo implements Serializable {

    /**
     * id : 10
     * pid : 1
     * title : 首页内容
     * desc : null
     * type : null
     * deleted_at : null
     * created_at : null
     * updated_at : null
     * content_list : [{"id":1,"meeting_id":"10","title":"首页","content":"中共中央关于加强党的政治建设的意见","img_id":"2303","video_url":null,"type":null,"deleted_at":null,"created_at":"2019-09-15 07:53:22","updated_at":null,"img_url":"http://file.rzkeji.com/uploads/2019/9/17/81498ef25a0759ed9e25ebf45006c5f9.png"}]
     */

    private int id;
    private String pid;
    private String title;
    private String desc;
    private String type;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private LinkedList<SubContentInfo> content_list;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public LinkedList<SubContentInfo> getContent_list() {
        return content_list;
    }

    public void setContent_list(LinkedList<SubContentInfo> content_list) {
        this.content_list = content_list;
    }

    @Override
    public String toString() {
        return "MeetingContentInfo{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", content_list=" + content_list +
                '}';
    }

    public static class SubContentInfo {
        /**
         * id : 1
         * meeting_id : 10
         * title : 首页
         * content : 中共中央关于加强党的政治建设的意见
         * img_id : 2303
         * video_url : null
         * type : null
         * deleted_at : null
         * created_at : 2019-09-15 07:53:22
         * updated_at : null
         * img_url : http://file.rzkeji.com/uploads/2019/9/17/81498ef25a0759ed9e25ebf45006c5f9.png
         * deleted_at : null
         * sort : 0
         * previous_content :
         * next_content :
         */

        private int id;
        private String meeting_id;
        private String title;
        private String content;
        private String img_id;
        private String video_url;
        private String type;
        private String deleted_at;
        private String created_at;
        private String updated_at;
        private String img_url;
        private String sort;
        private String previous_content;
        private String next_content;
        private String chapterTitle;
        private String nextChapterTitle;
        private int is_read;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMeeting_id() {
            return meeting_id;
        }

        public void setMeeting_id(String meeting_id) {
            this.meeting_id = meeting_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg_id() {
            return img_id;
        }

        public void setImg_id(String img_id) {
            this.img_id = img_id;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
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

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getPrevious_content() {
            return previous_content;
        }

        public void setPrevious_content(String previous_content) {
            this.previous_content = previous_content;
        }

        public String getNext_content() {
            return next_content;
        }

        public void setNext_content(String next_content) {
            this.next_content = next_content;
        }


        public String getChapterTitle() {
            return chapterTitle;
        }

        public void setChapterTitle(String chapterTitle) {
            this.chapterTitle = chapterTitle;
        }

        public String getNextChapterTitle() {
            return nextChapterTitle;
        }

        public void setNextChapterTitle(String nextChapterTitle) {
            this.nextChapterTitle = nextChapterTitle;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        @Override
        public String toString() {
            return "SubContentInfo{" +
                    "id=" + id +
                    ", meeting_id='" + meeting_id + '\'' +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", img_id='" + img_id + '\'' +
                    ", video_url='" + video_url + '\'' +
                    ", type='" + type + '\'' +
                    ", deleted_at='" + deleted_at + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    ", img_url='" + img_url + '\'' +
                    ", sort='" + sort + '\'' +
                    ", previous_content='" + previous_content + '\'' +
                    ", next_content='" + next_content + '\'' +
                    '}';
        }
    }
}
