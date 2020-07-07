package com.hckj.yddxst.net;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver2<T> implements Observer<BaseResponse2<T>> {
    private static final String TAG = "BaseObserver";

    @Override
    public void onSubscribe(Disposable d) {
        Log.e(TAG, "onSubscribe: ");
    }

    @Override
    public void onNext(BaseResponse2<T> baseResp) {
        Log.e(TAG, "onNext: ");
        if ("success".equals(baseResp.getLevel())) {
            onSuccess(baseResp.getData());
            onSuccess(baseResp);
        } else if ("error".equals(baseResp.getLevel()) && baseResp.getData() != null) {
            String errMsg = baseResp.getMessage() == null ? "接口返回异常" : baseResp.getMessage();
            onFailureSpecial(baseResp.getData(), errMsg);
        } else {
            String errMsg = baseResp.getMessage() == null ? "接口返回异常" : baseResp.getMessage();
            onFailure(null, errMsg);
        }

    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: " + e.getMessage());
        onFailure(e, RxHelper.exceptionHandler(e));
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ");
    }

    public abstract void onSuccess(T res);

    public void onSuccess(BaseResponse2<T> baseResp){

    }

    public abstract void onFailure(Throwable e, String err);

    public void onFailureSpecial(T errResp, String err) {

    }
}
