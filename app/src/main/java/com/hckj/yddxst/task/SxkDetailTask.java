package com.hckj.yddxst.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hckj.yddxst.activity.SxkDetailActivity;
import com.hckj.yddxst.bean.SxkDetailInfo;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SxkDetailTask extends AsyncTask<String, Integer, SxkDetailInfo> {
    private final OkHttpClient client = new OkHttpClient();
    private SxkDetailActivity sxkDetailActivity = null;
    private JSONObject jsonObject = null;
    private Integer resultcode;

    public SxkDetailTask(SxkDetailActivity sxkDetailActivity) {
        this.sxkDetailActivity = sxkDetailActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected SxkDetailInfo doInBackground(String... values) {
        String id = values[0];
        Request request = new Request.Builder()
                .url( "http://zddxapi.rzkeji.com/api/dangv2/data/getContentDetail?id="+id)
                .get( )
                .build();

        Response response = null;
        try {
            response = client.newCall( request ).execute();//得到Response 对象
            if (!response.isSuccessful()) {
                return null;
            }

            if (response.isSuccessful()) {
                Log.d( "","response.code()==" + response.code() );
                Log.d("", "response.message()==" + response.message() );
                String responseText = response.body().string();
                Log.d("", "res==" + responseText );

                jsonObject = new JSONObject( responseText );
                JSONObject data = jsonObject.getJSONObject( "data" );
                SxkDetailInfo sxkDetailInfo =SxkDetailInfo.toBean( data );

                return sxkDetailInfo;
            }
        } catch (Exception e) {
            Log.e("",""+e);
            return null;
        }
        return  null;
    }



    protected void onPostExecute(SxkDetailInfo sxkDetailInfo) {
        if (sxkDetailInfo==null ){
            sxkDetailActivity.faildRefreshViews();
        }else {
            sxkDetailActivity.refreshViews(sxkDetailInfo);
        }
    }
}
