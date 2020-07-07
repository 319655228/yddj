package com.hckj.yddxst.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.faceunity.FUStaEngine;
import com.faceunity.fuenum.FUTtsLanguage;
import com.faceunity.fuenum.FUTtsType;
import com.google.gson.Gson;
import com.hckj.yddxst.Authpack;
import com.hckj.yddxst.Constant;
import com.hckj.yddxst.data.Effect;
import com.hckj.yddxst.data.EffectFactory;

import java.io.IOException;

/**
 * FUStaEngine 工具类
 */
public final class StaUnityUtils {
    private FUStaEngine mFUStaEngine;
    private Context mContext;

    private StaUnityUtils() {
    }

    public static StaUnityUtils getInstance() {
        return StaUnityHolder.INSTANCE;
    }

    /**
     * 初始化 FUStaEngine
     *
     * @param context
     */
    public void init(@NonNull Context context) throws IOException {
        mContext = context.getApplicationContext();
        // 从配置文件中读取Effect信息
        String configPath = "config.json";
        byte[] bytesConfig = FileIOUtils.readBytesFromAssets(mContext, configPath);
        Effect effect = new Gson().fromJson(new String(bytesConfig), Effect.class);
        EffectFactory.setEffect(effect);

        byte[] offLineAuth = FileIOUtils.readBytesFromFile(Constant.BUNDLE_PATH);

        String decoderPath = "fusta/data_decoder.bin";
        byte[] bytesDecoder = FileIOUtils.readBytesFromAssets(mContext, decoderPath);

        String asrPath = "fusta/data_asr.bin";
        byte[] bytesAsr = FileIOUtils.readBytesFromAssets(mContext, asrPath);

        String alignPath = "fusta/data_ali.bin";
        byte[] bytesAlign = FileIOUtils.readBytesFromAssets(mContext, alignPath);

        /**
         * 1. type1不走离线鉴权。能走的通
         * 2. type2走离线鉴权。能走通
         * 3. type1走离线鉴权。走不通
         * */
        FUStaEngine.Builder builder = new FUStaEngine
                //传入上下文，必要
                .Builder(context)
                //验证证书，必要
                .setAuth(Authpack.A())
                //设置语音识别工具包（中文）
                .setAsrData(bytesAsr)
                //设置tts查询方式
                .setFUTtsType(FUTtsType.ALIGNMENT)
                //设置中文标识
                .setVtaTtsLanguage(FUTtsLanguage.CHINESE)
                //设置语音自动校准工具包（中文）
                .setAlignData(bytesAlign)
                //设置文字编码功能数据文件
                .setCharacterDecoder(bytesDecoder)
                //设置离线鉴权
                .setOffLineAuth(offLineAuth)
                .setIsLimitFps(true);

        mFUStaEngine = builder.build();

    }

    public Context getContext() {
        return mContext;
    }

    public FUStaEngine getFUStaEngine() {
        return mFUStaEngine;
    }

    private static class StaUnityHolder {
        private static final StaUnityUtils INSTANCE = new StaUnityUtils();
    }

    public static void resetSta(){
        FUStaEngine fuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
        fuStaEngine.setAnimTransX(EffectFactory.getFixedX());
        fuStaEngine.setAnimTransY(EffectFactory.getFixedY());
        fuStaEngine.setAnimTransZ(EffectFactory.getFixedZ());
    }

    public static void setStaBigger(){
        FUStaEngine fuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
        fuStaEngine.setAnimTransX(0);
        fuStaEngine.setAnimTransY(40);
        fuStaEngine.setAnimTransZ(-500);
    }
}
