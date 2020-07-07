package com.hckj.yddxst;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.faceunity.utils.LogUtils;
import com.hckj.yddxst.activity.SplashActivity;
import com.hckj.yddxst.activity.UpgradeActivity;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import io.reactivex.plugins.RxJavaPlugins;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 描述:  Yddx Application<br>
 * 日期: 2019-09-22 00:48 <br>
 * 作者: 林明健 <br>
 */
public class YddxApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        // 初始化科大讯飞
        initIflytek();
        super.onCreate();
        mContext = this;
        // 初始化Bugly
        initBugly();
        // 屏蔽Sta日志
        LogUtils.setLogLevel(LogUtils.OFF);
        // Ijk关闭日志
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);

        RxJavaPlugins.setErrorHandler(Throwable::printStackTrace);
    }

    /**
     * 描述: 初始化科大讯飞SDK <br>
     * 日期: 2019-09-22 00:45 <br>
     * 作者: 林明健 <br>
     */
    private void initIflytek() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + Constant.XFYUN_KEY);
        Setting.setShowLog(false);
    }

    /**
     * 描述: 初始化腾讯Bugly <br>
     * 日期: 2019-09-22 00:46 <br>
     * 作者: 林明健 <br>
     */
    private void initBugly() {
        // Beta更新监听
        Beta.upgradeListener = (i, upgradeInfo, b, b1) -> {
            if (upgradeInfo != null) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), UpgradeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(YddxApplication.this, "当前版本为最新版本", Toast.LENGTH_SHORT).show();
            }
        };
        // true表示初始化时自动检查升级 false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
        Beta.autoCheckUpgrade = true;
        //只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;不设置会默认所有activity都可以显示弹窗;
//        Beta.canShowUpgradeActs.add(IndexActivity.class);
        Beta.canNotShowUpgradeActs.add(SplashActivity.class);
        // 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
        Beta.showInterruptedStrategy = true;
        Bugly.init(getApplicationContext(), Constant.Bugly.API_KEY, false);
    }
}
