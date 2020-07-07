package com.hckj.yddxst.net;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * 描述：
 * 作者：林明健
 * 日期：2019-08-22 16:03
 */
public class RxHelper {
    public static final String TAG_TTS_BEGIN = "TAG_TTS_BEGIN";
    public static final String TAG_TTS_PROCESS = "TAG_TTS_PROCESS";
    public static final String TAG_TTS_COMPLETE = "TAG_TTS_COMPLETE";
    public static final String TAG_TTS_ERROR = "TAG_TTS_ERROR";

    public static final String TAG_SPEECH_BEGIN = "TAG_SPEECH_BEGIN";
    public static final String TAG_SPEECH_COMPLETE = "TAG_SPEECH_COMPLETE";
    public static final String TAG_SPEECH_ERROR = "TAG_SPEECH_ERROR";
    public static final String TAG_SPEECH_RECOVER = "TAG_SPEECH_RECOVER";
    public static final String TAG_SPEECH_PROCESS = "TAG_SPEECH_PROCESS";

    public static final String TAG_INDEX_BACK = "TAG_INDEX_BACK";
    public static final String TAG_MEETING_CLOSE = "TAG_MEETING_CLOSE";
    public static final String TAG_INDEX_TOUCH = "TAG_INDEX_TOUCH";

    /**
     * 线程调度器转换，订阅事件发生在io线程，回调发生在ui主线程
     *
     * @param <T> 泛型
     * @return 转换器
     */
    public static <T> ObservableTransformer<T, T> io2m() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static String exceptionHandler(Throwable e) {
        e.printStackTrace();
        String errorMsg = "未知错误";
        if (e instanceof UnknownHostException) {
            errorMsg = "请检查网络状态";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "请求网络超时";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorMsg = convertStatusCode(httpException);
        } else if (e instanceof ParseException || e instanceof JSONException || e instanceof JsonSyntaxException) {
            errorMsg = "数据解析错误";
        } else if (e instanceof ConnectException) {
            errorMsg = "连接服务器异常";
        } else if (e instanceof RxException) {
            RxException re = (RxException) e;
            errorMsg = re.getMessage() + "";
        }
        Log.e("RxHelper", "是否rxexception: " +(e instanceof RxException));
        return errorMsg;
    }

    /**
     * 请求错误转换信息
     *
     * @param httpException httpException类
     * @return 信息
     */
    private static String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 404) {
            msg = "请求资源不存在";
        } else if (httpException.code() >= 500 && httpException.code() < 600) {
            msg = "服务器处理请求出错";
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            msg = "服务器无法处理请求";
        } else if (httpException.code() >= 300 && httpException.code() < 400) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}
