package com.hckj.yddxst.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 描述: SharedPreferences工具类 <br>
 * 日期: 2019-09-22 13:24 <br>
 * 作者: 林明健 <br>
 */
public class SpUtil {
    public static final String DEVICE_NUM = "DEVICE_NUM";

    public static void save(Context ctx, String key, String value) {
        if (TextUtils.isEmpty(key))
            return;
        SharedPreferences sp = ctx.getSharedPreferences("yddx", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String get(Context ctx, String key, String def) {
        if (TextUtils.isEmpty(key))
            return "";
        SharedPreferences sp = ctx.getSharedPreferences("yddx", Context.MODE_PRIVATE);
        return sp.getString(key, def);
    }
}
