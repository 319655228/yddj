package com.hckj.yddxst.bean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ClassName:SxkDetailInfo
 * Package:com.hckj.yddx.bean
 * Description:
 *
 * @date:2020/7/2 10:55
 * @author:1477083563@qq.com
 */
public class SxkDetailInfo {
    public String title;
    public String content;

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

    @Override
    public String toString() {
        return "SxkDetailInfo{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
    public static SxkDetailInfo toBean(JSONObject item) {
        try {
            String title = item.getString("title");
            String content = item.getString("content");

            SxkDetailInfo sxkDetailInfo = new SxkDetailInfo();
            sxkDetailInfo.setTitle(title);
            sxkDetailInfo.setContent(content);
            return sxkDetailInfo;
        } catch (JSONException e) {
            Log.e("",""+e);
            return null;
        }
    }
}
