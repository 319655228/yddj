package com.hckj.yddxst.activity;

import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.rxbus.RxBus;
import com.faceunity.FUStaEngine;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.bean.MenuAuthInfo;
import com.hckj.yddxst.data.EffectFactory;
import com.hckj.yddxst.fragment.MenuFragment;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.tts.BakerTtsHandler;
import com.hckj.yddxst.utils.AudioObserver;
import com.hckj.yddxst.utils.SpUtil;
import com.hckj.yddxst.utils.StaUnityUtils;
import com.rxjava.rxlife.RxLife;

import java.util.List;

import butterknife.BindView;

/**
 * 描述: 首页Activity, 含有多个Fragment可以切换 <br>
 * 日期: 2019-09-22 13:18 <br>
 * 作者: 林明健 <br>
 */
public class IndexActivity extends BaseActivity {
    @BindView(R.id.gl_surface)
    GLSurfaceView glSurface;
    @BindView(R.id.layout_fragment)
    LinearLayout layoutFragment;
    private long lastBackpress = 0;

    // FUSta引擎
    protected FUStaEngine mFuStaEngine;
    // 标贝语音
    private BakerTtsHandler mTtsHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mFuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
        mFuStaEngine.onCreate(glSurface);
        mFuStaEngine.selectEffect(EffectFactory.convertEffect(), () -> {
            Log.e(TAG, "3D人物渲染完成 ");
        });

        //声音管理
        AudioObserver audioObserver = new AudioObserver(this);
        getLifecycle().addObserver(audioObserver);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mTtsHandler = new BakerTtsHandler(this, new BakerTtsHandler.TtsListener() {
            @Override
            public void onTtsProcess(int process) {
                Log.e(TAG, "onTtsProcess: " + process);
                runOnUiThread(() -> RxBus.getDefault().post(RxHelper.TAG_TTS_PROCESS, RxHelper.TAG_TTS_PROCESS));
            }

            @Override
            public void onTtsComplete() {
                Log.e(TAG, "onTtsComplete: ");
                runOnUiThread(() -> RxBus.getDefault().post(RxHelper.TAG_TTS_COMPLETE, RxHelper.TAG_TTS_COMPLETE));

            }

            @Override
            public void onError(String errMsg) {
                Log.e(TAG, "onError: " + errMsg);
                runOnUiThread(() -> {
                    RxBus.getDefault().post(RxHelper.TAG_TTS_ERROR, RxHelper.TAG_TTS_ERROR);
                    showAlertDialog(errMsg);
                });

            }

            @Override
            public void onPlayerStart(String flag) {
                Log.e(TAG, "onPlayerStart: ");
                runOnUiThread(() -> RxBus.getDefault().post(flag == null ? "" : flag, RxHelper.TAG_SPEECH_BEGIN));
            }

            @Override
            public void onPlayerComplete(String flag) {
                Log.e(TAG, "onPlayerComplete: ");
                runOnUiThread(() -> RxBus.getDefault().post(flag == null ? "" : flag, RxHelper.TAG_SPEECH_COMPLETE));
            }
        });

        glSurface.setOnClickListener(v -> RxBus.getDefault().post("", RxHelper.TAG_INDEX_TOUCH));

        RxBus.getDefault().subscribe(this, RxHelper.TAG_MEETING_CLOSE, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String meetingKey) {
                setMeetingFinish(meetingKey);
            }
        });
        // 默认切换至MenuFragment
        replaceFragment(MenuFragment.newInstance());

        // 获取当前设备号下的菜单权限
        String deviceNum = SpUtil.get(this, SpUtil.DEVICE_NUM, "");
        request().getMenuAuth(deviceNum)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<MenuAuthInfo>>(this, false) {
                    @Override
                    public void onSuccess(List<MenuAuthInfo> res) {
                        if (res != null && !res.isEmpty()) {
                            for (MenuAuthInfo info : res) {
                                SpUtil.save(IndexActivity.this, info.getTag(), info.getStatus());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {

                    }
                });
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.layout_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFuStaEngine != null) {
            mFuStaEngine.onResume();
        }
        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFuStaEngine != null) {
            mFuStaEngine.onPause();
        }
        isForeground = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFuStaEngine != null) {
            mFuStaEngine.onDestroy();
        }
        if (mTtsHandler != null) {
            mTtsHandler.release();
        }
        RxBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        Log.e(TAG, "onBackPressed: " + backStackEntryCount);
        if (backStackEntryCount == 1) {
            long now = System.currentTimeMillis();
            if (now - lastBackpress > 2000) {
                lastBackpress = now;
                showToast(R.string.finish_tips);
                RxBus.getDefault().post(RxHelper.TAG_INDEX_BACK, RxHelper.TAG_INDEX_BACK);
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        } else {
            Log.e(TAG, "super.onBakPressed");
            fragmentManager.popBackStack();
        }
    }

    public void startSpeech(String word) {
//        Log.e(TAG, "startSpeech: " + word);
        runOnUiThread(() -> {
            mTtsHandler.start(word);
        });
    }

    public void startSpeech(String word, String flag) {
//        Log.e(TAG, "startSpeech: " + word + " flag: " + flag);
        runOnUiThread(() -> {
            mTtsHandler.start(word, flag);
        });
    }

    public void stopSpeech() {
        runOnUiThread(() -> {
            mFuStaEngine.stopMediaPlayer();
            mTtsHandler.stop();
            mFuStaEngine.updateAnimation("anims/呼吸.bundle");
        });
    }

    public void stopSpeechNoAnim() {
        runOnUiThread(() -> {
            mFuStaEngine.stopMediaPlayer();
            mTtsHandler.stop();
        });
    }

    public void setMeetingFinish(String key) {
        Log.e(TAG, "setMeetingFinish: " + key);
        if (TextUtils.isEmpty(key))
            return;
        request().setMeetingFinish(key)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<String>(this, false) {
                    @Override
                    public void onSuccess(String res) {
                        Log.e(TAG, "onSuccess: " + res);
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
    }

    /**
     * 当前前景是否在前台
     */
    private boolean isForeground;

    public boolean isIndexForeground(){
        return isForeground;
    }
}
