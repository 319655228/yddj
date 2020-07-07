package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.net.RxHelper;
import com.rxjava.rxlife.RxLife;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class TextLayout extends RelativeLayout {
    private TextView tvContent;

    public TextLayout(Context context) {
        super(context);
        init(context);
    }

    public TextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bubble, this);
        tvContent = view.findViewById(R.id.tv_content);
    }

    public void setText(String content) {
        tvContent.setText(content);

        AlphaAnimation animation = new AlphaAnimation(0, 1);
//        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(0);
        setAnimation(animation);
        this.setVisibility(VISIBLE);
        Observable.timer(15, TimeUnit.SECONDS)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    this.setVisibility(GONE);
                }, throwable -> {

                });
    }
}
