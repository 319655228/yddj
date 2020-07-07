package com.hckj.yddxst.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hckj.yddxst.activity.CalendarActivity;
import com.hckj.yddxst.activity.SXKActivity;
import com.hckj.yddxst.bean.NewsInfo2;
import com.hckj.yddxst.bean.RlInfo;
import com.hckj.yddxst.bean.RlTimeInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CalendarListTask extends AsyncTask<List, Integer,  List<RlTimeInfo>> {
    private final OkHttpClient client = new OkHttpClient();
    private SXKActivity sxkActivity = null;
    private JSONObject jsonObject = null;
    private String total = null;
    private String page = null;
    private SxkCallback sxkCallback;
    private CalendarActivity calendarActivity;
    private boolean flag;

    public CalendarListTask(CalendarActivity calendarActivity, boolean flag) {
        this.calendarActivity = calendarActivity;
        this.flag = flag;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  List<RlTimeInfo> doInBackground(List... lists) {
        List stringList = lists[0];
        Request request = new Request.Builder()
                .url("http://zddxapi.rzkeji.com/api/dangv2/data/getContentRiliList?"+"page="+stringList.get(0)+"&limit="+stringList.get(1))
                .get()
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();//得到Response 对象
            if (response.isSuccessful()) {
                Log.i("CalendarListTask:", " response.code()==" + response.code());
                Log.i("CalendarListTask:", "CalendarListTask: response.message()==" + response.message());
                String responseText = response.body().string();
                Log.i("CalendarListTask:", "CalendarListTask: res==" + responseText);

                jsonObject = new JSONObject(responseText);
                String level = jsonObject.getString("level");
                JSONObject pageInfo = jsonObject.getJSONObject("page_info");
                total = pageInfo.getString("total");
                page = pageInfo.getString("page");

                if (level.equals("success")) {
                    List<RlTimeInfo> list = null;
                    JSONArray newsList   = jsonObject.getJSONArray("data");
                    list = RlTimeInfo.toList(newsList);

                    return list;
                } else {
                    Log.e("CalendarListTask:", "submit error,responseText:" + responseText);
                    JSONObject jsonObject = new JSONObject(responseText);
                    return null;
                }

            }

        } catch (Exception e) {
            Log.e("e", "e:" + e);
            return null;
        }

        /**
         * 后台500错误，返回null,
         */

        return null;
    }


    @Override
    protected void onPostExecute( List<RlTimeInfo> listBeans) {
        if(null == listBeans){
            calendarActivity.sxkFailure();
        }else {
            calendarActivity.sxkSuccess(listBeans,total,page,flag);
        }
    }
    public   interface  SxkCallback {
        public  void sxkFailure();
        public  void sxkSuccess(List<RlTimeInfo> listBeans, String total, String page, Boolean flag);
    }
}
