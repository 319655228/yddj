package com.hckj.yddxst.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hckj.yddxst.activity.SplashActivity;

/**
 * 描述：设备启动广播
 * 作者：林明健
 * 日期：2019-08-22 14:34
 */
public class AutoBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            Intent i = new Intent(context, SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
