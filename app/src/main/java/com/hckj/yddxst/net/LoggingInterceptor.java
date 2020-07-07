package com.hckj.yddxst.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：请求日志拦截器
 * 作者：林明健
 * 日期：2019-08-22 18:18
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "Network";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.e(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
//        Log.e(TAG, String.format("Received response for %s in %.1fms%n%s",
//                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        Log.e(TAG, String.format("Received response for %s in %.1fms%n",
                response.request().url(), (t2 - t1) / 1e6d));

        return response;
    }
}
