package com.hckj.yddxst.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

/***
 * 描述: 网络相关工具类 <br>
 * 日期: 2019-09-22 13:28 <br>
 * 作者: 林明健 <br>
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static long lastTotalRxBytes = 0;
    private static long lastTimeStamp = 0;

    /**
     * 描述: 是否处于联网状态 <br>
     * 日期: 2019-09-22 13:29 <br>
     * 作者: 林明健 <br>
     *
     * @param context context
     * @return boolean
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }

    /**
     * 描述: 获取指定UID网速 <br>
     * 日期: 2019-09-22 13:27 <br>
     * 作者: 林明健 <br>
     *
     * @param uid 指定uid
     * @return java.lang.String 网速 xx kb/s
     */
    public static String getNetSpeed(int uid) {
        long nowTotalRxBytes = getTotalRxBytes(uid);
        long nowTimeStamp = System.currentTimeMillis();
        if (nowTimeStamp - lastTimeStamp == 0) {
            return "0 kb/s";
        }
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed + " kb/s";
    }

    /**
     * 描述: 获取网速 <br>
     * 日期: 2019-09-22 13:28 <br>
     * 作者: 林明健 <br>
     *
     * @param uid 应用UID
     * @return long
     */
    private static long getTotalRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }
}
