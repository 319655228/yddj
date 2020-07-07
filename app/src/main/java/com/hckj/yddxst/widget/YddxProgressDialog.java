package com.hckj.yddxst.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import com.hckj.yddxst.R;

/**
 * 描述: Yddx自定义加载框<br>
 * 日期: 2019-09-22 13:20 <br>
 * 作者: 林明健 <br>
 */
public class YddxProgressDialog extends ProgressDialog {
    private TextView tvMsg;

    public YddxProgressDialog(Context context) {
        super(context, R.style.Theme_AppCompat_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yddx_progress_dialog);
        tvMsg = findViewById(R.id.tv_msg);
    }

    @Override
    public void setMessage(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            message = "";
        }
        if (tvMsg != null) {
            tvMsg.setText(message);
        }
    }

    @Override
    public void show() {
        Window w = this.getWindow();
        if (w != null) {
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }
        super.show();
    }
}
