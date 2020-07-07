package com.hckj.yddxst.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hckj.yddxst.bean.NewsInfo2;
import com.hckj.yddxst.fragment.NewsFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NewsListTask extends AsyncTask<String, Integer,  List<NewsInfo2>> {
    private final OkHttpClient client = new OkHttpClient();
    private NewsFragment newsFragment = null;
    private JSONObject jsonObject = null;

    public NewsListTask(NewsFragment newsFragment) {
        this.newsFragment = newsFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  List<NewsInfo2> doInBackground(String... strings) {
        String string = strings[0];
        Request request = new Request.Builder()
                .url("http://zddxapi.rzkeji.com/api/dangv2/data/getContentList?page=1&limit=30&module="+string)
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
            Toast.makeText(newsFragment.getActivity(), "服务异常！", Toast.LENGTH_SHORT).show();
        }else {
            newsFragment.refreshListView(listBeans);
        }
    }
}
