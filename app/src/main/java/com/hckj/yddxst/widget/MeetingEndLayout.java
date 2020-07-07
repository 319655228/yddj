package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;

/**
 * 描述：
 * 作者：林明健
 * 日期：2019-11-13 10:39
 */
public class MeetingEndLayout extends LinearLayout {
    private ImageView ivQrcode;
    private ImageView btnReturn;

    public MeetingEndLayout(Context context) {
        super(context);
        init(context);
    }

    public MeetingEndLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeetingEndLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_end, this);
        ivQrcode = view.findViewById(R.id.iv_qrcode);
        btnReturn = view.findViewById(R.id.btn_return);
    }

    public void animLoadQrcode(String imgUrl) {
        Glide.with(getContext())
                .load(imgUrl + "")
                .into(ivQrcode);
        //Glide新版动画改版了，使用原生动画取代放大功能
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(0);
        ivQrcode.setAnimation(animation);
    }

    public void setBackListener(OnClickListener l) {
        if (btnReturn != null) {
            btnReturn.setOnClickListener(l);
        }
    }
}
