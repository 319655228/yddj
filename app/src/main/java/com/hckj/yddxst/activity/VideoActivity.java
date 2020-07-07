package com.hckj.yddxst.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.bean.VideoInfo;
import com.rxjava.rxlife.RxLife;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

public class VideoActivity extends BaseActivity {
    public static final int CODE_NEED_RESULT = 3;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.video_player)
    StandardGSYVideoPlayer videoPlayer;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.layout_desc)
    View layoutDesc;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }

    public static void startActivity(Context ctx, VideoInfo info) {
        Intent intent = new Intent(ctx, VideoActivity.class);
        intent.putExtra("info", info);
        ctx.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, VideoInfo info) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("info", info);
        activity.startActivityForResult(intent, CODE_NEED_RESULT);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        VideoInfo videoInfo = (VideoInfo) intent.getSerializableExtra("info");

        if (videoInfo == null || TextUtils.isEmpty(videoInfo.getAndroid_video_url())) {
            showAlertDialogWithClose("数据异常，请检查视频源是否存在");
            return;
        }

        String title = videoInfo.getTitle();
        String desc = videoInfo.getDesc();
        title = title != null ? title : "";
        desc = desc != null ? desc : "";
        tvTitle.setText(title);
        tvDesc.setText("\t\t" + desc);
        tvDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        boolean cacheWithPlay = true;
        if ("full".equals(videoInfo.getModel())) {
            tvTitle.setVisibility(View.GONE);
            layoutDesc.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) videoPlayer.getLayoutParams();
            lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            lp.topMargin = 0;
            videoPlayer.setLayoutParams(lp);
        } else if ("some".equals(videoInfo.getModel())) {
            cacheWithPlay = false;
        }
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                showAlertDialogWithClose("播放异常，请检查视频文件");
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                showToast("感谢观看。");
                finish();
            }
        });
        String videoUrl = videoInfo.getAndroid_video_url();
        if (videoUrl.contains(".m3u8")) {
            PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        } else {
            PlayerFactory.setPlayManager(IjkPlayerManager.class);
        }
        videoPlayer.setUp(videoInfo.getAndroid_video_url(), cacheWithPlay, videoInfo.getTitle() + "");
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        videoPlayer.getBackButton().setVisibility(View.GONE);
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getBackButton().setOnClickListener(view -> onBackPressed());
        videoPlayer.startPlayLogic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
        //定时滚动标题
        Observable.interval(5, 8, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    if (scrollView != null) {
                        scrollView.smoothScrollBy(0, 100);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayer.getCurrentPlayer().release();
    }

    @Override
    public void onBackPressed() {
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

    @OnClick(R.id.iv_back)
    public void onBackClick() {
        finish();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
