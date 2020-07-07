package com.hckj.yddxst.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsInfo2 implements Serializable {

    private String id;
    private String title;
    private String desc;
    private String module;
    private String img_id;
    private String type;//catalog:这个列表对应的是目录;content:这个列表对应的是内容
    private String content;
    private String video_url;
    private String sort;
    private String content_day;
    private String updated_at;
    private String imgs_url;
    private List<String> img_list;
 
    public NewsInfo2(String id,String title,String desc,String module,String img_id, String type,
                     String content,String video_url,String sort,String content_day,String updated_at,String imgs_url,List<String> img_list){
        this.id=id;
        this.title=title;
        this.desc=desc;
        this.module=module;
        this.img_id=img_id;
        this.type=type;
        this.content=content;
        this.video_url=video_url;
        this.sort=sort;
        this.content_day=content_day;
        this.updated_at=updated_at;
        this.imgs_url=imgs_url;
        this.img_list=img_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getContent_day() {
        return content_day;
    }

    public void setContent_day(String content_day) {
        this.content_day = content_day;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getImgs_url() {
        return imgs_url;
    }

    public void setImgs_url(String imgs_url) {
        this.imgs_url = imgs_url;
    }

    public List<String> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<String> img_list) {
        this.img_list = img_list;
    }

    public static NewsInfo2 toBean(JSONObject item) {
        try {
            String id = item.getString("id");
            String title = item.getString("title");
            String desc = item.getString("desc");
            String module = item.getString("module");
            String img_id = item.getString("img_id");
            String type = item.getString("type");
            String content = item.getString("content");
            String video_url = item.getString("video_url");
            String sort = item.getString("sort");
            String content_day = item.getString("content_day");
            String updated_at = item.getString("updated_at");
            String imgs_url = item.getString("img_url");
            JSONArray jsonArray = (JSONArray)item.get("img_list");
            List<String> img_list =new ArrayList<String>();
            for(int i=0; i< jsonArray.length();i++) {
                String s = (String) jsonArray.get(i);
                img_list.add(s);
            }


            return new NewsInfo2(id,title,desc,module,img_id,type,content,
                    video_url,sort,content_day,updated_at,imgs_url,img_list);
        } catch (JSONException e) {
            Log.e("e", "e" + e);
            return null;
        }
    }
    public static List<NewsInfo2> toList(JSONArray jsonArray) {
        List<NewsInfo2> list = new ArrayList<NewsInfo2>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(toBean( jsonArray.getJSONObject( i ) ));
            }
        }
        catch (JSONException e) {
            Log.e("e", "e" + e);
            return  new ArrayList<NewsInfo2>();
        }
        return list;
    }
}
