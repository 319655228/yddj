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
public class RlInfo {
    private String type_name;
    private String count;
    private List<RlContextInfo> rlContextInfoArrayList;

    public RlInfo(String type_name,String count,List<RlContextInfo> rlContextInfoArrayList){
        this.type_name=type_name;
        this.count=count;
        this.rlContextInfoArrayList=rlContextInfoArrayList;
    }



    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<RlContextInfo> getRlContextInfoArrayList() {
        return rlContextInfoArrayList;
    }

    public void setRlContextInfoArrayList(ArrayList<RlContextInfo> rlContextInfoArrayList) {
        this.rlContextInfoArrayList = rlContextInfoArrayList;
    }

    public static RlInfo toBean(JSONObject item) {
        try {
            String title = item.getString("type_name");
            String desc = item.getString("count");
            List<RlContextInfo> list =null;
            JSONArray jsonArray = (JSONArray)item.get("list");
            list = RlContextInfo.toList(jsonArray);
            return new RlInfo(title,desc,list);
        } catch (JSONException e) {
            Log.e("e", "e" + e);
            return null;
        }
    }
    public static List<RlInfo> toList(JSONArray jsonArray) {
        List<RlInfo> list = new ArrayList<RlInfo>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(toBean( jsonArray.getJSONObject( i ) ));
            }
        }
        catch (JSONException e) {
            Log.e("e", "e" + e);
            return  new ArrayList<RlInfo>();
        }
        return list;
    }
}
