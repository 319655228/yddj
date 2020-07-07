package com.hckj.yddxst.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.BuildConfig;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.bean.RedTourInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.widget.EmptyControlVideo;
import com.rxjava.rxlife.RxLife;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 红色之旅Activity <br>
 * 日期: 2019-09-26 00:32 <br>
 * 作者: 林明健 <br>
 */
public class RedTourActivity extends BaseActivity {
    @BindView(R.id.video_player)
    EmptyControlVideo videoPlayer;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    private List<RedTourInfo> redTourInfos;
    private int curIndex = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_redtour;
    }

    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, RedTourActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        ivLeft.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                showAlertDialogWithClose("页面启动异常，请重新进行尝试");
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        request().getRedTourList()
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<RedTourInfo>>(this, true) {
                    @Override
                    public void onSuccess(List<RedTourInfo> res) {
                        showVideoAndTitle(res);
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialogWithClose(err + "");
                    }
                });
    }

    private void showVideoAndTitle(List<RedTourInfo> res) {
        redTourInfos = res;
        if (res != null && !res.isEmpty()) {
            RedTourInfo redTourInfo = res.get(0);
            tvTitle.setText(redTourInfo.getTitle() + "");
            tvTitle.setTag(redTourInfo.getLink_url()+"");
            Glide.with(this)
                    .load(redTourInfo.getThumb_url())
                    .into(ivInfo);
            if (res.size() > 1) {
                ivRight.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.start("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.red_tour);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }

    @OnClick(R.id.iv_info)
    public void onViewClicked() {
        Object tag = tvTitle.getTag();
        if (tag instanceof String) {
            OnlineCheckActivity.startActivity(this, tag.toString());
        }
    }


    @OnClick({R.id.iv_left, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                showImageAndTitle(--curIndex);
                break;
            case R.id.iv_right:
                showImageAndTitle(++curIndex);
                break;
        }
    }

    private void showImageAndTitle(int index) {
        if (redTourInfos == null || redTourInfos.isEmpty()) {
            return;
        }
        int size = redTourInfos.size();
        if (index >= size) {
            curIndex--;
            return;
        }
        if (index < 0) {
            curIndex++;
            return;
        }

        RedTourInfo redTourInfo = redTourInfos.get(index);
        String thumb_url = redTourInfo.getThumb_url() + "";
        String link_url = redTourInfo.getLink_url() + "";
        tvTitle.setText(redTourInfo.getTitle() + "");
        tvTitle.setTag(link_url);
        Glide.with(this)
                .load(thumb_url)
                .into(ivInfo);
        if (index < size - 1) {
            ivRight.setVisibility(View.VISIBLE);
        } else {
            ivRight.setVisibility(View.GONE);
        }
        if (index > 0) {
            ivLeft.setVisibility(View.VISIBLE);
        } else {
            ivLeft.setVisibility(View.GONE);
        }
    }
}
