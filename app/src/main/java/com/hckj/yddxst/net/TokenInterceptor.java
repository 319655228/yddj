package com.hckj.yddxst.net;

import com.google.gson.Gson;
import com.hckj.yddxst.YddxApplication;
import com.hckj.yddxst.bean.TokenInfo;
import com.hckj.yddxst.utils.SpUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 描述：
 * 作者：林明健
 * 日期：2019-12-02 9:28
 */
public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String url = request.url().toString();

        // token认证不通过
        if (url.contains("openapi.data-baker.com/tts")) {
            MediaType mediaType = response.body().contentType();
            if ("application/json".equalsIgnoreCase(mediaType.toString())) {
                String resStr = response.body().string();
                TokenInfo tokenInfo = new Gson().fromJson(resStr, TokenInfo.class);
                if (tokenInfo.getErr_no() == 30000) {
                    Call<TokenInfo> call = RetrofitManager.getInstance().create(BaseApi.class).getToken();
                    TokenInfo body = call.execute().body();
                    // 同步获取Token 后进行重试
                    String token = body.getAccess_token();
                    SpUtil.save(YddxApplication.getContext(), "TTS_TOKEN", token);

                    // 表单参数重建
                    if (request.body() instanceof FormBody) {
                        FormBody.Builder builder = new FormBody.Builder();
                        FormBody formBody = (FormBody) request.body();
                        // 将以前的参数添加
                        for (int i = 0; i < formBody.size(); i++) {
                            builder.add(formBody.name(i), formBody.value(i));
                        }
                        builder.add("access_token", token);
                        request = request.newBuilder().post(builder.build()).build();
                    }
                    return chain.proceed(request);
                }
                return response.newBuilder().body(ResponseBody.create(mediaType, resStr)).build();
            }
        }
        return response;
    }
}
