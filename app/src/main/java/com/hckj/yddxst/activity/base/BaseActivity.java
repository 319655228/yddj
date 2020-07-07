package com.hckj.yddxst.activity.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hckj.yddxst.R;
import com.hckj.yddxst.net.BaseApi;
import com.hckj.yddxst.net.RetrofitManager;
import com.hckj.yddxst.widget.YddxProgressDialog;

import butterknife.ButterKnife;

/**
 * 描述：BaseActivity
 * 作者：林明健
 * 日期：2019-08-22 11:10
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = this.getClass().getSimpleName();
    protected Dialog mAlertDialog;
    private YddxProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
        initListener();
        initData(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected void initListener(){};

    protected void initView() {
        View vBack = findViewById(R.id.btn_back);
        if (vBack != null) {
            vBack.setOnClickListener(v -> {
                btnBackClick();
            });
        }
    }

    protected  void btnBackClick(){
        finish();
    }

    protected abstract void initData(Bundle savedInstanceState);

    protected void startActivity(Class cls) {
        Intent intent = new Intent(BaseActivity.this, cls);
        startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected BaseApi request() {
        return RetrofitManager.getInstance().create(BaseApi.class);
    }

    /**
     * 显示对话框
     *
     * @param message 显示的内容
     */
    protected void showAlertDialog(String message) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.color.transparent);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_alert_yddx, null, false);
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        TextView tvInfo = dialogView.findViewById(R.id.tv_msg);
        tvInfo.setText(message + "");

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        btnConfirm.setText("确认");
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(v -> mAlertDialog.dismiss());

        mAlertDialog.setCancelable(true);
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.show();
    }

    protected void showAlertDialogWithClose(String message) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.color.transparent);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_alert_yddx, null, false);
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        TextView tvInfo = dialogView.findViewById(R.id.tv_msg);
        tvInfo.setText(message + "");

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        btnConfirm.setText("确认");
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(v -> {
            mAlertDialog.dismiss();
            finish();
        });

        mAlertDialog.setCancelable(false);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();
    }

    /**
     * 显示Toast
     *
     * @param content 显示的内容
     */
    protected void showToast(String content) {
        if (TextUtils.isEmpty(content))
            return;
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    protected void showDialog(String message) {
        if (mLoadingDialog == null ) {
            mLoadingDialog = new YddxProgressDialog(this);
            mLoadingDialog.setIndeterminate(true);
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setCanceledOnTouchOutside(false);
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();

    }

    protected void hideDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
        mLoadingDialog = null;
    }

    @Override
    protected void onDestroy() {
        hideDialog();
        super.onDestroy();
    }
}
