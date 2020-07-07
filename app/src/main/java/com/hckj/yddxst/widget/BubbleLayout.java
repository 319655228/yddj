package com.hckj.yddxst.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.net.RxHelper;
import com.rxjava.rxlife.RxLife;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 描述: 气泡布局 <br>
 * 日期: 2020-02-29 14:53 <br>
 * 作者: 林明健 <br>
 */
public class BubbleLayout extends RelativeLayout {
    private TextView tvContent;
    private ScrollView scrollView;
    private Disposable disposable;
    private LinearLayout llContainer;

    public BubbleLayout(Context context) {
        super(context);
        init(context);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bubble, this);

        tvContent = view.findViewById(R.id.tv_content);
        scrollView = view.findViewById(R.id.scrollView);
        llContainer = view.findViewById(R.id.ll_container);
    }

    public void setText(String content) {
        tvContent.setText(content);
        tvContent.setVisibility(INVISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setOnClickListener(OnClickListener l) {
        if (scrollView != null) {
            scrollView.setOnTouchListener((v, event) -> {
                l.onClick(v);
                return false;
            });
        }
        if (llContainer != null) {
            llContainer.setOnClickListener(l);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == VISIBLE) {
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(1000);
            animation.setRepeatCount(0);
            setAnimation(animation);
            if (disposable != null && disposable.isDisposed()) {
                disposable.dispose();
                disposable = null;
            }
            tvContent.setVisibility(VISIBLE);
            scrollView.scrollTo(0, 0);
            disposable = Observable.interval(500, 100, TimeUnit.MILLISECONDS)
                    .filter(aLong -> getVisibility() == VISIBLE)
                    .compose(RxHelper.io2m())
                    .as(RxLife.as(this))
                    .subscribe(aLong -> {
                        scrollView.smoothScrollBy(0, 1);
                    }, throwable -> {

                    });
        } else {
            AlphaAnimation animation = new AlphaAnimation(1, 0);
            animation.setDuration(1000);
            animation.setRepeatCount(0);
            setAnimation(animation);
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                disposable = null;
            }

        }
        super.setVisibility(visibility);
    }
}
