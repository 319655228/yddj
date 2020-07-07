package com.hckj.yddxst.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.cameraview.CameraView;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.widget.EmptyControlVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 宣誓模式页 <br>
 * 日期: 2019-10-16 23:43 <br>
 * 作者: 林明健 <br>
 */
public class PartyOathActivity extends BaseActivity {
    @BindView(R.id.video_player)
    EmptyControlVideo videoPlayer;
    @BindView(R.id.camera)
    CameraView camera;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.layout_mask)
    LinearLayout layoutMask;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;

    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, PartyOathActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_party_oath;
    }

    @Override
    protected void initListener() {
        super.initListener();
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                showAlertDialog("视频播放异常");
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mediaPlayer = MediaPlayer.create(PartyOathActivity.this, R.raw.wlc);  //无需再调用setDataSource
        mediaPlayer.start();
        layoutMask.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(14 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!PartyOathActivity.this.isFinishing()) {
                    int remainTime = (int) (millisUntilFinished / 1000L);
                    btnStart.setText(remainTime + " S");
                    btnStart.setEnabled(false);
                }
            }

            @Override
            public void onFinish() {
                btnStart.setEnabled(true);
                btnStart.setText("开始宣誓");
            }
        };
        countDownTimer.start();
    }


    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        layoutMask.setVisibility(View.GONE);
        videoPlayer.postDelayed(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            String url = "android.resource://" + getPackageName() + "/" + R.raw.yddx_oath;
            videoPlayer.start(url, false, false);
            videoPlayer.setVisibility(View.VISIBLE);
        }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
        try {
            if (camera != null) {
                camera.start();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        GSYVideoManager.releaseAllVideos();
    }
}
