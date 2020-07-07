package com.hckj.yddxst.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:RlContextInfo
 * Package:com.hckj.yddxst.bean
 * Description:
 *
 * @date:2020/7/6 16:29
 * @author:1477083563@qq.com
 */
public class RlContextInfo {
    private String id;
    private String title;
    private String desc;
    private String type;
    private String content_type;
    private String sort;
    private String day;
    private String web_url;

    public RlContextInfo(String id,String title,String desc,String type,
                         String content_type,String sort,String day,String web_url){
        this.id=id;
        this.title=title;
        this.desc=desc;
        this.type=type;
        this.content_type=content_type;
        this.sort=sort;
        this.day=day;
        this.web_url=web_url;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public static RlContextInfo toBean(JSONObject item) {
        try {

            String id = item.getString("id");
            String title = item.getString("title");
            String desc = item.getString("desc");
            String type = item.getString("type");
            String content_type = item.getString("content_type");
            String sort = item.getString("sort");
            String day = item.getString("day");
            String web_url = item.getString("web_url");

            return new RlContextInfo(id,title,desc,type,content_type,sort, day,web_url);
        } catch (JSONException e) {
            Log.e("e", "e" + e);
            return null;
        }
    }
    public static List<RlContextInfo> toList(JSONArray jsonArray) {
        List<RlContextInfo> list = new ArrayList<RlContextInfo>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(toBean( jsonArray.getJSONObject( i ) ));
            }
        }
        catch (JSONException e) {
            Log.e("e", "e" + e);
            return  new ArrayList<RlContextInfo>();
        }
        return list;
    }
}
