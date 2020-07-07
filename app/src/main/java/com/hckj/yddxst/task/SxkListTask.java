package com.hckj.yddxst.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hckj.yddxst.Constant;
import com.hckj.yddxst.activity.SXKActivity;
import com.hckj.yddxst.bean.NewsInfo2;
import com.hckj.yddxst.bean.SocketInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SxkListTask extends AsyncTask<List, Integer,  List<NewsInfo2>> {
    private final OkHttpClient client = new OkHttpClient();
    private SXKActivity sxkActivity = null;
    private JSONObject jsonObject = null;
    private String total = null;
    private String page = null;
    private SxkCallback sxkCallback;
    private Context context;
    private boolean flag;

    public SxkListTask(SxkCallback sxkCallback,Context context, boolean flag) {
        this.sxkCallback = sxkCallback;
        this.context = context;
        this.flag = flag;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  List<NewsInfo2> doInBackground(List... lists) {
        List stringList = lists[0];
        stringList.get(0);
        Request request = new Request.Builder()
                .url("http://zddxapi.rzkeji.com/api/dangv2/data/getContentList?" +
                        "page="+stringList.get(0)+"&limit="+stringList.get(1)
                        +"&module="+stringList.get(2)+"&pid="+stringList.get(3)
                        +"&keyword="+stringList.get(4))
                .get()
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();//得到Response 对象
            if (response.isSuccessful()) {
                Log.i("NewsTask:", " response.code()==" + response.code());
                Log.i("NewsTask:", "NewsTask: response.message()==" + response.message());
                String responseText = response.body().string();
                Log.i("LogiTnTNewsTaskask:", "NewsTask: res==" + responseText);

                jsonObject = new JSONObject(responseText);
                String level = jsonObject.getString("level");
                JSONObject pageInfo = jsonObject.getJSONObject("page_info");
                total = pageInfo.getString("total");
                page = pageInfo.getString("page");

                if (level.equals("success")) {
                    List<NewsInfo2> list = null;
                    JSONArray newsList   = jsonObject.getJSONArray("data");
                    list = NewsInfo2.toList(newsList);

                    return list;
                } else {
                    Log.e("NewsTask:", "submit error,responseText:" + responseText);
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
    protected void onPostExecute( List<NewsInfo2> listBeans) {
        if(null == listBeans){
            sxkCallback.sxkFailure();
        }else {
            sxkCallback.sxkSuccess(listBeans,total,page,flag);
        }
    }
    public   interface  SxkCallback {
        public  void sxkFailure();
        public  void sxkSuccess(List<NewsInfo2> listBeans,String total,String page,Boolean flag);
    }
}
