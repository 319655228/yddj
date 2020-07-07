package com.hckj.yddxst;

import android.os.Environment;

/**
 * 描述：常量
 * 作者：林明健
 * 日期：2019-08-22 15:18
 */
public class Constant {

    // 科大讯飞id
    static final String XFYUN_KEY = "=5d6a1dda";

    // Yddx Base Url
    public static final String BASE_URL = "http://zddxapi.rzkeji.com/api/";

    // Yddx File Path
    public static final String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yddx_temp/";
    //photo path
    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yddx_photo/";

    // Yddx Bundle File
    public static final String BUNDLE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yddx_offline.bundle";

    public static final int SPLIT_NUM = 150;

    static class Bugly {
        static final String API_KEY = BuildConfig.BUGLY_KEY;
    }

    // 全屏模式
    public static final int SHOW_MODE_FULL = 1;
    // 半屏 4/3模式
    public static final int SHOW_MODE_HALF = 0;
    // SurfaceView 显示模式

}
