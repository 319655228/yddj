package com.hckj.yddxst.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.hckj.yddxst.R;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

/**
 * 无任何控制ui的播放器
 */
public class EmptyControlVideo extends StandardGSYVideoPlayer {

    public EmptyControlVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EmptyControlVideo(Context context) {
        super(context);
    }

    public EmptyControlVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.widget_empty_control_video;
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp() {
        //super.touchDoubleUp();
        //不需要双击暂停
    }

    /**
     * 停止在最后一帧
     */
    public void onAutoCompletion() {
        setStateAndUi(CURRENT_STATE_AUTO_COMPLETE);

        mSaveChangeViewTIme = 0;
        mCurrentPosition = 0;

        if (!mIfCurrentIsFullscreen)
            getGSYVideoManager().setLastListener(null);
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getActivityContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        releaseNetWorkState();


        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            Debuger.printfLog("onAutoComplete");
            mVideoAllCallBack.onAutoComplete(mOriginUrl, mTitle, this);
        }
    }

    @Override
    public void startPlayLogic() {
        // 网络视频使用 Ijk内核 其他使用 系统内核
        if (!TextUtils.isEmpty(mOriginUrl) && mOriginUrl.startsWith("http")) {
            if (mOriginUrl.contains(".m3u8")) {
                PlayerFactory.setPlayManager(Exo2PlayerManager.class);
            } else {
                PlayerFactory.setPlayManager(IjkPlayerManager.class);
            }
        } else {
            PlayerFactory.setPlayManager(SystemPlayerManager.class);//系统模式
        }
        super.startPlayLogic();
    }

    public void start(String url) {
        setUp(url, true, "");
        setLooping(true);
        startPlayLogic();
    }

    public void start(String url, boolean repeat){
        setUp(url, true, "");
        setLooping(repeat);
        startPlayLogic();
    }

    public void start(String url, boolean repeat, boolean cacheWithPlay){
        setUp(url, cacheWithPlay, "");
        setLooping(repeat);
        startPlayLogic();
    }
}
