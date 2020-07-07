package com.hckj.yddxst.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;

public class RecordDialog extends Dialog {
    private ImageView ivStatus;
    private ImageView ivRecording;

    private View.OnClickListener mFinishListener;

    public RecordDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_record);
        // 点击空白处不能取消动画
        setCanceledOnTouchOutside(false);
        // 初始化控件
        initView();
    }

    private void initView() {
        ivRecording = findViewById(R.id.iv_recording);
        ivStatus = findViewById(R.id.iv_status);

        ivRecording.setOnClickListener(v -> {
            if (mFinishListener != null) {
                mFinishListener.onClick(v);
            }
        });
    }

    public void updateVolume(int volume) {
        int resId = 0;
        if (volume > 0 && volume < 3) {
            resId = R.drawable.bg_wav1;
        } else if (volume > 3 && volume <= 5) {
            resId = R.drawable.bg_wav2;
        } else if (volume > 5 && volume < 8) {
            resId = R.drawable.bg_wav3;
        } else if (volume >= 8) {
            resId = R.drawable.bg_wav4;
        }
        if (resId == 0)
        {

        }
        Glide.with(getContext())
                .load(resId == 0 ? "" : resId)
                .into(ivStatus);
    }

    @Override
    public void show() {
        super.show();
        updateVolume(0);
    }

    public RecordDialog setFinishListener(View.OnClickListener l) {
        this.mFinishListener = l;
        return this;
    }
}
