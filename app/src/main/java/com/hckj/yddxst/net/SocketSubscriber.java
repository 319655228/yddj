package com.hckj.yddxst.net;

import android.util.Log;

import com.dhh.websocket.WebSocketSubscriber;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public abstract class SocketSubscriber<T> extends WebSocketSubscriber {
    private static final Gson GSON = new Gson();
    protected Type type;


    public SocketSubscriber() {
        analysisType();
    }

    private void analysisType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("No generics found!");
        }
        ParameterizedType type = (ParameterizedType) superclass;
        this.type = type.getActualTypeArguments()[0];
    }

    @Override
    protected void onMessage(String text) {
        Log.e("Socket", "onMessage: " + text);
        Disposable subscribe = Observable.just(text)
                .map((Function<String, T>) s -> {
                    if (!validate(text)) {
                        throw new JsonSyntaxException("Error Json");
                    }
                    try {
                        return GSON.fromJson(s, type);
                    } catch (JsonSyntaxException e) {
                        return GSON.fromJson(GSON.fromJson(s, String.class), type);
                    }
                })
                .compose(RxHelper.io2m())
                .subscribe(this::onMessage, throwable -> {
                    if (throwable instanceof JsonSyntaxException) {
                        onSrcString(text + "");
                    } else {
                        onError(throwable);
                    }
                });
    }

    protected abstract void onMessage(T t);

    protected  void onSrcString(String text){

    }

    /**
     * 描述: 校验是否Json <br>
     * 日期: 2019-10-19 00:25 <br>
     * 作者: 林明健 <br>
     */
    private boolean validate(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        return jsonElement.isJsonObject();
    }
}
