package com.hckj.yddxst.net;

import com.hckj.yddxst.Constant;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 描述：Retrofit 静态单例封装
 * 作者：林明健
 * 日期：2019-08-22 15:16
 */
public class RetrofitManager {
    private static final int READ_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;
    private static final int CONNECT_TIMEOUT = 10;

    private Retrofit mRetrofit;

    private RetrofitManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new TokenInterceptor())
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static class SingletonHolder {
        private static final RetrofitManager INSTANTCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANTCE;
    }

    public <T> T create(Class<T> cls) {
        return mRetrofit.create(cls);
    }
}
