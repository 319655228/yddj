package com.hckj.yddxst.activity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CheckMentalActivity extends BaseActivity {
    @BindView(R.id.tv_evaluate_character)
    TextView tvEvaluateCharacter;
    @BindView(R.id.tv_evaluate_pressure)
    TextView tvEvaluatePressure;
    @BindView(R.id.tv_evaluate_relation)
    TextView tvEvaluateRelation;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.layout_mask)
    LinearLayout layoutMask;
    private AlertDialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_mental;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_evaluate_character, R.id.tv_evaluate_pressure, R.id.tv_evaluate_relation, R.id.layout_mask})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_evaluate_character:
                showQuestionDialog("http://wechat.psych.ac.cn/index.php/xlcp/start/3?uid=25618");
                break;
            case R.id.tv_evaluate_pressure:
                showQuestionDialog("http://wechat.psych.ac.cn/index.php/xlcp/start/2?uid=25618");
                break;
            case R.id.tv_evaluate_relation:
                showQuestionDialog("http://wechat.psych.ac.cn/index.php/xlcp/start/1?uid=25618");
                break;
            case R.id.layout_mask:
                layoutMask.setVisibility(View.GONE);
                break;
        }
    }

    private void showQuestionDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.color.transparent);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_alert_yddx, null, false);
        builder.setView(dialogView);
        mDialog = builder.create();

        TextView tvInfo = dialogView.findViewById(R.id.tv_msg);
        tvInfo.setText("请选择答题方式");

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        btnConfirm.setText("手机答题");
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(v -> {
            layoutMask.setVisibility(View.VISIBLE);
            Bitmap qrcodeBitmap = CodeUtils.createImage(url, 500, 500, null);
            ivQrcode.setImageBitmap(qrcodeBitmap);
            mDialog.dismiss();
        });

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setText("直接答题");
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setOnClickListener(v -> {
            OnlineCheckActivity.startActivity(CheckMentalActivity.this, url);
            mDialog.dismiss();
        });
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }
}
