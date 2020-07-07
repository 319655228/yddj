package com.hckj.yddxst.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.rxbus.RxBus;
import com.dhh.websocket.Config;
import com.dhh.websocket.RxWebSocket;
import com.faceunity.FUStaEngine;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.ImgUploadInfo;
import com.hckj.yddxst.bean.MeetingClassifyInfo;
import com.hckj.yddxst.bean.MeetingContentInfo;
import com.hckj.yddxst.bean.MeetingInfo;
import com.hckj.yddxst.bean.MeetingStartInfo;
import com.hckj.yddxst.bean.SocketInfo;
import com.hckj.yddxst.data.EffectFactory;
import com.hckj.yddxst.net.BaseApi;
import com.hckj.yddxst.net.BaseResponse;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.SocketSubscriber;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.tts.BakerTtsHandler;
import com.hckj.yddxst.utils.CommonUtils;
import com.hckj.yddxst.utils.PhotoUtils;
import com.hckj.yddxst.utils.SpUtil;
import com.hckj.yddxst.utils.StaUnityUtils;
import com.hckj.yddxst.utils.VolumeObserver;
import com.hckj.yddxst.widget.MeetingContentLayout;
import com.hckj.yddxst.widget.MeetingEndLayout;
import com.hckj.yddxst.widget.MeetingInfSelectLayout;
import com.hckj.yddxst.widget.MeetingPhotoLayout;
import com.hckj.yddxst.widget.MeetingWelcomeLayout;
import com.rxjava.rxlife.RxLife;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.WebSocket;

/**
 * 描述: MeetingFragment 党小组会Fragment <br>
 * 日期: 2019-09-22 00:49 <br>
 * 作者: 林明健 <br>
 */
public class MeetingFragment extends BaseFragment implements VolumeObserver.VolumeChangeListener {
    @BindView(R.id.layout_meeting_select)
    MeetingInfSelectLayout layoutMeetingSelect;
    @BindView(R.id.layout_meeting_welcome)
    MeetingWelcomeLayout layoutMeetingWelcome;
    @BindView(R.id.layout_meeting_content)
    MeetingContentLayout layoutMeetingContent;
    @BindView(R.id.layout_meeting_photo)
    MeetingPhotoLayout layoutMeetingPhoto;
    @BindView(R.id.layout_meeting_end)
    MeetingEndLayout layoutMeetingEnd;

    // 当前会议对象
    private MeetingInfo curMeetingInfo;
    // websocket对象
    private WebSocket mWebSocket;
    private Disposable mDisposable;
    private Disposable mPingDisposable;
    // 是否已完成websocket初始化（应付websocket重复执行init消息）
    private boolean isInit;

    private String meetingKey = "";
    private String classify = "";// 会议分类
    // 音频监听
    private VolumeObserver volumeObserver;
    private boolean isFromQa;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meeting;
    }

    @Override
    protected void lazyLoad() {

    }

    static MeetingFragment newInstance(String classify) {
        MeetingFragment fragment = new MeetingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classify", classify);
        fragment.setArguments(bundle);
        return fragment;
    }

    static MeetingFragment newInstance(MeetingInfo info) {
        MeetingFragment fragment = new MeetingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classify", "问答");
        bundle.putSerializable("info", info);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        StaUnityUtils.setStaBigger();
        if (getArguments() != null) {
            classify = getArguments().getString("classify");
            curMeetingInfo = (MeetingInfo) getArguments().getSerializable("info");
            isFromQa = curMeetingInfo != null;
            if (TextUtils.isEmpty(classify)) {
                showAlertDialogWithClose("数据异常，请退出重试");
                return;
            }
        } else {
            showAlertDialogWithClose("数据异常，请退出重试");
            return;
        }

        // 议题选择页 返回按钮点击事件
        layoutMeetingSelect.setSelectListener(new MeetingInfSelectLayout.MeetingInfSelectListener() {
            @Override
            public void onMeetingSelect(MeetingClassifyInfo info) {
                curMeetingInfo = new MeetingInfo();
                curMeetingInfo.setId(info.getId());
                curMeetingInfo.setTitle(info.getName());
                startWebSocket();
            }

            @Override
            public void onClassifySelect(MeetingClassifyInfo info) {
                loadMeetingList(info.getName());
            }

            @Override
            public void onBackBtnClick() {
                onBackPressed();
            }
        });
        layoutMeetingEnd.setBackListener(v -> onBackPressed());
        layoutMeetingWelcome.setClickListener(v -> {
            if (isFromQa) {
                onBackPressed();
            } else {
                stopSpeech();
                swithLayout(layoutMeetingSelect);
            }

        }, view -> {
            loadMeetingContent();
        });
        layoutMeetingContent.setListener(new MeetingContentLayout.LayoutListener() {
            @Override
            public void onWarn(String msg) {
                showAlertDialogWithClose(msg);
            }

            @Override
            public void onError(String msg) {
                showAlertDialog(msg);
            }

            @Override
            public void onComplete() {
                swithLayout(layoutMeetingPhoto);
            }

            @Override
            public void onStop() {
                stopMediaPlayer();
            }

            @Override
            public void onSpeach(String word) {
                startSpeech(word);
            }

            @Override
            public void onSwith(String meetingId, String contentId) {
                request().sendContentToApp(meetingKey, meetingId, contentId)
                        .compose(RxHelper.io2m())
                        .as(RxLife.as(MeetingFragment.this))
                        .subscribe(s -> {
                        }, e -> {
                        });
            }

            @Override
            public void onVolume() {
                boolean isMute = volumeObserver.getCurrentMusicVolume() > 0;
                volumeObserver.setMute(isMute);
                layoutMeetingContent.updateVolume(isMute ? -1 : 1);
            }

            @Override
            public void onQuit() {
                showAlertDialogWithCancel("当前为第一页，是否返回上一环节？", "确认", "取消", v -> {
                    swithLayout(layoutMeetingWelcome);
                    StaUnityUtils.setStaBigger();
                    stopMediaPlayer();
                    switchAnim("双手打开");
                });
            }
        });

        // RxWebSocket初始化
        OkHttpClient client = new OkHttpClient.Builder()
                .pingInterval(10, TimeUnit.SECONDS)
                .build();
        Config config = new Config.Builder()
                .setShowLog(false, "WebSocket")
                .setClient(client)
                .setReconnectInterval(70, TimeUnit.SECONDS)
                .build();
        RxWebSocket.setConfig(config);

        // 监听声音变化
        volumeObserver = new VolumeObserver(getAppActivity());
        volumeObserver.setVolumeChangeListener(this);

        if (curMeetingInfo == null) {
            // 开始播放音频，并网络加载会议题目
            String strWelcome = getString(R.string.meeting_begin).replace("党小组会", classify.replace("模块", "").replace("流程", "步骤"));
            startSpeech(strWelcome);
            loadMeetingList(classify);
        } else {
            startWebSocket();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.interval(20, 8, TimeUnit.SECONDS)
                .filter(aLong -> isVisible() && BakerTtsHandler.isPlaying()
                        && getAppActivity() != null && getAppActivity().isIndexForeground()
                        && layoutMeetingContent.getVisibility() == View.VISIBLE)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    switchAnim(CommonUtils.getRandomAction(false));
                }, throwable -> {

                });
    }

    /**
     * 描述: websocket <br>
     * 日期: 2019-09-22 01:00 <br>
     * 作者: 林明健 <br>
     */
    private void startWebSocket() {
        stopMediaPlayer();
        if (mWebSocket != null) {
            mWebSocket.close(1000, "Yes");
        }
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        isInit = false;
        RxWebSocket.get(BaseApi.WEBSOCKET_URL)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new SocketSubscriber<SocketInfo>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    protected void onMessage(SocketInfo socketInfo) {
                        Log.e("YddxSocket", "onMessage: " + socketInfo);
                        if (socketInfo != null) {
                            String handle = socketInfo.getHandle();
                            if ("init".equals(handle) && !isInit) {                 // 初始化房间
                                loadMeetingQrcode(socketInfo.getClient_id() + "");
                                isInit = true;
                            } else if ("logout".equals(handle)) {                    // 用户退出
                                layoutMeetingWelcome.removePeople(socketInfo.getClient_id() + "");
                                layoutMeetingContent.removePeople(socketInfo.getClient_id() + "");
                            } else if ("user_online".equals(handle)) {               // 新用户加入
                                layoutMeetingWelcome.addPeople(socketInfo.getData());
                                layoutMeetingContent.addPeople(socketInfo.getData());
                            } else if ("chat_info".equals(handle)) {
                                layoutMeetingContent.setChatContent(socketInfo.getData());
                            } else if ("next_link".equals(handle)) {
                                layoutMeetingContent.nextSeg();
                            } else if ("pre_link".equals(handle)) {
                                layoutMeetingContent.preSeg();
                            } else if ("next_page".equals(handle)) {
                                layoutMeetingContent.next();
                            } else if ("pre_page".equals(handle)) {
                                layoutMeetingContent.previous();
                            }
                        }
                    }

                    @Override
                    protected void onOpen(WebSocket webSocket) {
                        super.onOpen(webSocket);
                        mWebSocket = webSocket;
                        mDisposable = disposable;
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showAlertDialog(e.getMessage());
                    }
                });

        if (mPingDisposable == null) {
            mPingDisposable = Observable.interval(5, TimeUnit.SECONDS)
                    .filter(aLong -> isInit && mWebSocket != null && (layoutMeetingContent.getVisibility() == View.VISIBLE || layoutMeetingWelcome.getVisibility() == View.VISIBLE))
                    .as(RxLife.as(this))
                    .subscribe(aLong -> mWebSocket.send("ping"), Throwable::printStackTrace);
        }
    }

    /**
     * 描述: 显示会议二维码 <br>
     * 日期: 2019-09-22 01:00 <br>
     * 作者: 林明健 <br>
     *
     * @param clientId 当前会议Id
     * @return void
     */
    private void loadMeetingQrcode(String clientId) {
        if (curMeetingInfo == null) {
            showAlertDialogWithClose("会议信息异常，请重新打开进入");
            return;
        }

        String deviceNum = SpUtil.get(getAppActivity(), SpUtil.DEVICE_NUM, "");
        request().startMeeting(curMeetingInfo.getId(), deviceNum, clientId)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<MeetingStartInfo>(getActivity(), true) {
                    @Override
                    public void onSuccess(MeetingStartInfo info) {
                        swithLayout(layoutMeetingWelcome);
                        meetingKey = info.getKey() + "";
                        layoutMeetingWelcome.animLoadImg(info.getQrcode_url(), info.getJoin_number());
                        switchAnim("双手打开");
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });
    }

    /**
     * 描述: 网络请求会议议题列表，并显示 <br>
     * 日期: 2019-09-22 00:58 <br>
     * 作者: 林明健 <br>
     */
    private void loadMeetingList(String classify) {
        swithLayout(layoutMeetingSelect);
        String deviceNum = SpUtil.get(getAppActivity(), SpUtil.DEVICE_NUM, "");
        request().getMeetingClassifyList(deviceNum, classify)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<MeetingClassifyInfo>>(getActivity(), true) {
                    @Override
                    public void onSuccess(BaseResponse<List<MeetingClassifyInfo>> bs) {
                        layoutMeetingSelect.setList(bs.getData(), bs.getType());
                    }

                    @Override
                    public void onSuccess(List<MeetingClassifyInfo> res) {

                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });
    }

    /**
     * 描述: 网络请求会议内容，并开始显示 <br>
     * 日期: 2019-09-22 00:57 <br>
     * 作者: 林明健 <br>
     */
    private void loadMeetingContent() {
        if (curMeetingInfo == null) {
            showAlertDialogWithClose("会议信息异常，请退出重试");
            return;
        }
        request().getMeetingContent(curMeetingInfo.getId())
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<MeetingContentInfo>>(getActivity(), true) {
                    @Override
                    public void onSuccess(List<MeetingContentInfo> res) {
                        if (res == null || res.isEmpty()) {
                            showAlertDialogWithClose("会议内容为空");
                            return;
                        }
                        swithLayout(layoutMeetingContent);
                        layoutMeetingContent.loadMeetingContent(res);
                        layoutMeetingContent.updateVolume(volumeObserver.getCurrentMusicVolume());
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });
    }

    /**
     * 描述: 停止所有媒体播放 <br>
     * 日期: 2019-09-22 01:00 <br>
     * 作者: 林明健 <br>
     */
    private void stopMediaPlayer() {
        stopSpeech();
        GSYVideoManager.releaseAllVideos();
    }

    private void swithLayout(View v) {
        View[] views = new View[]{layoutMeetingSelect, layoutMeetingEnd, layoutMeetingContent, layoutMeetingPhoto, layoutMeetingWelcome};
        for (View view : views) {
            view.setVisibility(view.getId() == v.getId() ? View.VISIBLE : View.INVISIBLE);
        }

        if (v.getId() == layoutMeetingEnd.getId()) {
            resetStaAnim();
            RxBus.getDefault().post(meetingKey, RxHelper.TAG_MEETING_CLOSE);
            request().getMeetingDocQrcode(meetingKey + "")
                    .compose(RxHelper.io2m())
                    .as(RxLife.as(this))
                    .subscribe(new YddxObserver<String>(getContext(), false) {
                        @Override
                        public void onSuccess(String res) {
                            layoutMeetingEnd.animLoadQrcode(res + "");
                            StaUnityUtils.getInstance().getFUStaEngine().updateAnimationOnce("anims/双手打开.bundle", null);
                        }

                        @Override
                        public void onFailure(Throwable e, String err) {
                            showAlertDialogWithClose(err + "");
                        }
                    });
            startSpeech(getString(R.string.meeting_finish).replace("党小组会", classify.replace("模块", "")));
        } else if (v.getId() == layoutMeetingPhoto.getId()) {
            layoutMeetingPhoto.startCamera(new MeetingPhotoLayout.Callback() {
                @Override
                public void onPictureSuccess(Bitmap bitmap, String filePath) {
                    Observable.just(filePath)
                            .flatMap((Function<String, ObservableSource<BaseResponse<ImgUploadInfo>>>) filePath1 -> {
                                PhotoUtils.saveBitmap(filePath, bitmap);
                                File file = new File(filePath);
                                RequestBody requestFile =
                                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                MultipartBody.Part imgBody =
                                        MultipartBody.Part.createFormData("img_file", file.getName(), requestFile);
                                RequestBody keyBody = RequestBody.create(MediaType.parse("multipart/form-data"), meetingKey + "");
                                return request().uploadMeetingImg(imgBody, keyBody);
                            })
                            .compose(RxHelper.io2m())
                            .as(RxLife.as(getAppActivity()))
                            .subscribe(new YddxObserver<ImgUploadInfo>(getAppActivity()) {
                                @Override
                                public void onSuccess(ImgUploadInfo res) {
                                    Log.e(TAG, "图片上传: " + res);
                                    showToast("上传成功");
                                }

                                @Override
                                public void onFailure(Throwable e, String err) {
                                    showAlertDialog(err);
                                }
                            });
                }

                @Override
                public void onPictureError(String errMsg) {
                    showAlertDialog(errMsg);
                }

                @Override
                public void onPreviousClick() {
                    swithLayout(layoutMeetingContent);
                    layoutMeetingContent.backToFinal();
                    layoutMeetingPhoto.closeCamera();
                }

                @Override
                public void onNextClick() {
                    layoutMeetingPhoto.closeCamera();
                    swithLayout(layoutMeetingEnd);
                }
            });
        } else if (v.getId() == layoutMeetingContent.getId()) {
            setStaAnimLeft();
        } else if (v.getId() == layoutMeetingSelect.getId() && !TextUtils.isEmpty(meetingKey)) {
            RxBus.getDefault().post(meetingKey + "", RxHelper.TAG_MEETING_CLOSE);
        }
    }

    /**
     * 描述: 音频大小变化监听 <br>
     * 日期: 2019-11-10 14:57 <br>
     * 作者: 林明健 <br>
     */
    @Override
    public void onVolumeChanged(int volume) {
        layoutMeetingContent.updateVolume(volume);
    }

    /**
     * 描述: 将模型恢复至默认位置大小 <br>
     * 日期: 2019-09-22 00:53 <br>
     * 作者: 林明健 <br>
     */
    private void resetStaAnim() {
        FUStaEngine fuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
        fuStaEngine.setAnimTransX(EffectFactory.getFixedX());
        fuStaEngine.setAnimTransY(EffectFactory.getFixedY());
        fuStaEngine.setAnimTransZ(EffectFactory.getFixedZ());
    }

    /**
     * 描述: 将模型设置在左上角位置 <br>
     * 日期: 2019-09-22 00:54 <br>
     * 作者: 林明健 <br>
     */
    private void setStaAnimLeft() {
        Log.e(TAG, "setStaAnimLeft: ");
        FUStaEngine fuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
        fuStaEngine.setAnimTransX(-80f);  // 数值越小越偏做
        fuStaEngine.setAnimTransY(190f);  // 数值越大 越高
        fuStaEngine.setAnimTransZ(-1600f);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (volumeObserver != null) {
            volumeObserver.registerReceiver();
        }
    }

    @Override
    public void onDestroy() {
        if (volumeObserver != null) {
            volumeObserver.unregisterReceiver();
        }
        RxBus.getDefault().post(meetingKey + "", RxHelper.TAG_MEETING_CLOSE);
        stopMediaPlayer();
        resetStaAnim();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        RxBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
