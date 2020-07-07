package com.hckj.yddxst.net;

import android.content.Context;
import android.widget.Toast;

import com.hckj.yddxst.utils.NetworkUtils;
import com.hckj.yddxst.widget.YddxProgressDialog;

import io.reactivex.disposables.Disposable;

/**
 * 描述：带对话框Observer
 * 作者：林明健
 * 日期：2019-08-23 8:51
 */
public abstract class YddxObserver<T> extends BaseObserver<T> {
    private boolean mShowDialog;
    private YddxProgressDialog mDialog;
    private Context mContext;
    private Disposable d;

    protected YddxObserver(Context mContext, boolean mShowDialog) {
        this.mShowDialog = mShowDialog;
        this.mContext = mContext;
    }

    protected YddxObserver(Context ctx) {
        this(ctx, true);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        this.d = d;
        if (!NetworkUtils.isConnected(mContext)) {
            Toast.makeText(mContext, "请检查网络状态", Toast.LENGTH_SHORT).show();
            if (d.isDisposed()) {
                d.dispose();
            }
        } else {
            showDialog();
        }
    }

    private void showDialog(String message) {
        if (mDialog == null && mShowDialog) {
            mDialog = new YddxProgressDialog(mContext);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(message);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }
    }

    private void showDialog(){
        showDialog("正在加载中");
    }

    private void hideDialog() {
        if (mDialog != null && mShowDialog)
            mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        hideDialog();
        super.onComplete();
    }

    @Override
    public void onError(Throwable e) {
        if (d.isDisposed()) {
            d.dispose();
        }
        hideDialog();
        super.onError(e);
    }
}
