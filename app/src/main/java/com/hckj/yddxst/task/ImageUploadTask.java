package com.hckj.yddxst.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ImageUploadTask extends AsyncTask<File, Integer, String> {
    private JSONObject jsonObject = null;
    private Context context = null;
    private UploadCallback uploadCallback ;
    private String msg ;

    //1.创建对应的MediaType
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final OkHttpClient client = new OkHttpClient();
    public ImageUploadTask(UploadCallback uploadCallback){this.uploadCallback = uploadCallback;}
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    /**
     * return image id
     */
    protected String doInBackground(File... files) {
        File file  = files[0];
//        String qwq = file.getName();
//        Log.d("",qwq);

        //2.创建RequestBody
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        //3.构建MultipartBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", "political_photo.jpg", fileBody)
                .build();
        //4.构建请求
        Request request = new Request.Builder()
                .url("http://zddxapi.rzkeji.com/api/dangv2/data/uploadBirthdayImg")
                .post(requestBody)
                .build();

        //5.发送请求
        Response response = null;
        try {
            response = client.newCall( request ).execute();//得到Response 对象
            if (response.isSuccessful()) {
                Log.d("", "response.code()==" + response.code() );
                Log.d("", "response.message()==" + response.message() );
                String responseText = response.body().string();
                Log.d( "","res==" + responseText );
                jsonObject = new JSONObject( responseText );
                String rdCode = jsonObject.getString( "data" );

                return rdCode;
            } else{
                String responseText = response.body().string();
                Log.e( "", "submit error,responseText:" + responseText );
                throw new IOException("Unexpected code " + response);
            }

        } catch (IOException e) {
            Log.e("",""+e);
        } catch (JSONException e) {
            Log.e("",""+e);
        }catch (Exception e) {
            Log.e("",""+e);
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String rdCode) {
        if(rdCode==null){
            uploadCallback.uploadFailure();
        }else if (rdCode!=null && !rdCode.equals("")){
            uploadCallback.uploadSucess(rdCode);
        }

    }
    public   interface  UploadCallback {
        public  void uploadFailure();
        public  void uploadSucess(String rdCode);
    }
}

