package com.hckj.yddxst.tts;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.faceunity.FUParams;
import com.faceunity.fuenum.FUAudioType;
import com.faceunity.fuenum.FUTimestampType;
import com.faceunity.sta.OnMediaPlayListener;
import com.hckj.yddxst.Constant;
import com.hckj.yddxst.bean.BakerInfo;
import com.hckj.yddxst.net.BaseApi;
import com.hckj.yddxst.net.RetrofitManager;
import com.hckj.yddxst.net.RxException;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.utils.CommonUtils;
import com.hckj.yddxst.utils.FileIOUtils;
import com.hckj.yddxst.utils.SpUtil;
import com.hckj.yddxst.utils.StaUnityUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 描述: 标贝Tts <br>
 * 日期: 2020-03-02 23:56 <br>
 * 作者: 林明健 <br>
 */
public class BakerTtsHandler {
    private static final String TAG = "BakerTts";
    private Context mContext;
    private TtsListener mTtsLisener;
    private CompositeDisposable mDisposables;
    private String mFlag;
    private LinkedList<BakerInfo> mBakerList = new LinkedList<>();
    private static boolean isPlaying; //STA是否正在播放
    private static boolean isEnabled; //是否可以将数据写入SDK

    public BakerTtsHandler(Context context, TtsListener listener) {
        this.mContext = context;
        this.mTtsLisener = listener;
        this.mDisposables = new CompositeDisposable();
        StaUnityUtils.getInstance().getFUStaEngine().setOnMediaPlayListener(new OnMediaPlayListener() {
            @Override
            public void onPrepared() {
                isPlaying = true;
                mTtsLisener.onPlayerStart(mFlag);
            }

            @Override
            public void onCompleted() {
                if (!isPlaying) {
                    return;
                }
                isPlaying = false;
                onPlayerReady();
            }

            @Override
            public void onError(String errMsg) {
                isPlaying = false;
                mTtsLisener.onError(errMsg);
            }
        });
    }

    public void start(String text, String flag) {
        if (TextUtils.isEmpty(text))
            return;
        isEnabled = true;
        mFlag = flag;
        mDisposables.clear();
        mBakerList.clear();
        LinkedList<String> splitText = CommonUtils.splitText(text);
        getVoice(splitText, true);
    }

    public void start(String text) {
        start(text, null);
    }

    public void stop() {
        mFlag = null;
        isPlaying = false;
        isEnabled = false;
        mBakerList.clear();
        if (!mDisposables.isDisposed()) {
            mDisposables.clear();
        }
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    public void release() {
        // Log.e(TAG, "release: ");
    }

    private void getVoice(LinkedList<String> res, boolean isFirst) {
        if (!isEnabled || res == null || res.isEmpty())
            return;
        String text = res.pop();

        Observable.just(text)
                .flatMap((Function<String, ObservableSource<ResponseBody>>) s ->
                        request().getVoice(SpUtil.get(mContext, "TTS_TOKEN", "TOKEN"),
                                "1", "6",
                                "10", "7", "4",
                                "zh", "标准合成_成熟女声_小玉", s))
                .delay(1, TimeUnit.SECONDS)
                .map(body -> {
                    String contentType = body.contentType().toString();
                    if (contentType.contains("application/json")) {
                        String errStr = body.string();
                        throw new RxException(errStr.contains("\"err_no\":30001") ? "网络拥堵" : "未知错误");
                    } else if ("audio/wav".equals(contentType)) {
                        return body.bytes();
                    } else {
                        throw new RxException("音频解析异常");
                    }
                })
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    boolean isRetry;

                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> observable) throws Exception {

                        return observable.flatMap(throwable -> {
                            Log.e(TAG, "重试: " + isRetry);
                            if (throwable instanceof RxException && !isRetry) {
                                isRetry = true;
                                return Observable.timer(1, TimeUnit.SECONDS)
                                        .flatMap((Function<Long, ObservableSource<?>>) aLong -> request().getVoice(SpUtil.get(mContext, "TTS_TOKEN", "TOKEN"),
                                                "1", "6",
                                                "10", "7", "4",
                                                "zh", "标准合成_成熟女声_小玉", text));

                            } else {
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .compose(RxHelper.io2m())
                .subscribe(new Observer<byte[]>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                        if (!isPlaying && mBakerList.isEmpty()) {
                            mTtsLisener.onTtsProcess(0);
                        }
                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        if (isEnabled) {
                            File audioFile = new File(Constant.FILE_DIR, new Date().getTime() + FileIOUtils.WAV_EXTENSION);
                            FileIOUtils.writeFileFromBytesByStream(audioFile, bytes);
                            mBakerList.add(new BakerInfo(audioFile.getAbsolutePath(), text));
                            mTtsLisener.onTtsComplete();
                            onPlayerReady();
                            getVoice(res, false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTtsLisener.onError(RxHelper.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void onPlayerReady() {
        Log.e(TAG, "onPlayerReady: " + isPlaying + " BakerList : " + mBakerList.size());
        if (isPlaying || !isEnabled)
            return;
        if (mBakerList.size() == 0) {
            mTtsLisener.onPlayerComplete(mFlag);
            return;
        }

        BakerInfo bakerInfo = mBakerList.pop();
        try {
            byte[] audio = FileIOUtils.readBytesFromFile(new File(bakerInfo.getFilePath()));
            FUParams params = new FUParams()
                    .setStreamMode(0)
                    .setAudioType(FUAudioType.WAV)
                    .setAudioData(audio)
                    .setAlignText(bakerInfo.getFileText())
                    .setTimestampType(FUTimestampType.PHONE);
            if (isPlaying || !isEnabled)
                return;
            StaUnityUtils.getInstance().getFUStaEngine().startStaDrivingProcess(params);
            Log.e(TAG, "播放: " + bakerInfo.getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
            mTtsLisener.onError("文件处理异常");
        }
    }

    public interface TtsListener {
        // Tts解析
        void onTtsProcess(int process);

        void onTtsComplete();

        void onError(String errMsg);

        void onPlayerStart(String flag);

        void onPlayerComplete(String flag);
    }

    private BaseApi request() {
        return RetrofitManager.getInstance().create(BaseApi.class);
    }
}
