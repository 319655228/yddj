package com.hckj.yddxst.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:RlTimeInfo
 * Package:com.hckj.yddxst.bean
 * Description:
 *
 * @date:2020/7/6 16:20
 * @author:1477083563@qq.com
 */
public class RlTimeInfo {
    private String title;
    private List<RlInfo> rlInfoList;

    public RlTimeInfo(String title,List<RlInfo> rlInfoList){
        this.title=title;
        this.rlInfoList=rlInfoList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RlInfo> getRlInfoList() {
        return rlInfoList;
    }

    public void setRlInfoList(List<RlInfo> rlInfoList) {
        this.rlInfoList = rlInfoList;
    }

    public static RlTimeInfo toBean(JSONObject item) {
        try {
            String titleTotal = item.getString("title");
            String year = titleTotal.substring(0,4);
            String month = titleTotal.substring(4,6);
            String title = year + "."+month;
            List<RlInfo> list =null;
            JSONArray jsonArray = (JSONArray)item.get("list");
            list = RlInfo.toList(jsonArray);
            return new RlTimeInfo(title,list);
        } catch (JSONException e) {
            Log.e("e", "e" + e);
            return null;
        }
    }
    public static List<RlTimeInfo> toList(JSONArray jsonArray) {
        List<RlTimeInfo> list = new ArrayList<RlTimeInfo>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(toBean( jsonArray.getJSONObject( i ) ));
            }
        }
        catch (JSONException e) {
            Log.e("e", "e" + e);
            return  new ArrayList<RlTimeInfo>();
        }
        return list;
    }
}
