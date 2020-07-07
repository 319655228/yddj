package com.hckj.yddxst.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.blankj.utilcode.util.SPUtils;
import com.hckj.yddxst.R;


public class YddxSettingDialog extends Dialog {
    private onItemClickLisenter onItemClickLisenter;

    public YddxSettingDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_yddx);
        boolean menuSilent = SPUtils.getInstance().getBoolean("MENU_SILENT", false);
        Button btnSilent = findViewById(R.id.btn_slient);
        btnSilent.setText("首页静音：" + (menuSilent ? "开" : "关"));
        btnSilent.setOnClickListener(v->{
            if (onItemClickLisenter != null) {
                onItemClickLisenter.onSilenceClick(this, !menuSilent);
            }
        });
        findViewById(R.id.btn_auth)
                .setOnClickListener(v -> {
                    if (onItemClickLisenter != null) {
                        onItemClickLisenter.onAuthClick(this);
                    }
                });
        findViewById(R.id.btn_update)
                .setOnClickListener(v -> {
                    if (onItemClickLisenter != null) {
                        onItemClickLisenter.onUpdateClick(this);
                    }
                });
        findViewById(R.id.btn_quit)
                .setOnClickListener(v -> {
                    if (onItemClickLisenter != null) {
                        onItemClickLisenter.onQuitClick(this);
                    }
                });
    }


    public void setOnItemClickLisenter(YddxSettingDialog.onItemClickLisenter onItemClickLisenter) {
        this.onItemClickLisenter = onItemClickLisenter;
    }


    public interface onItemClickLisenter {
        void onAuthClick(Dialog dialog);

        void onUpdateClick(Dialog dialog);

        void onQuitClick(Dialog dialog);

        void onSilenceClick(Dialog dialog, boolean isSilent);
    }
}
