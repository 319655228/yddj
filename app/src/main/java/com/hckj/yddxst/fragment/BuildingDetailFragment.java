package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.rxbus.RxBus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.hckj.yddxst.R;
import com.hckj.yddxst.YddxApplication;
import com.hckj.yddxst.activity.OnlineCheckActivity;
import com.hckj.yddxst.bean.BuildingDetailInfo;
import com.hckj.yddxst.bean.BuildingInfo;
import com.hckj.yddxst.net.BaseResponse;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.tts.BakerTtsHandler;
import com.hckj.yddxst.utils.CommonUtils;
import com.hckj.yddxst.utils.NetworkUtils;
import com.hckj.yddxst.utils.StaUnityUtils;
import com.hckj.yddxst.widget.EmptyControlVideo;
import com.hckj.yddxst.widget.MaequeeText;
import com.rxjava.rxlife.RxLife;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 描述: 虚拟公务员Fragment <br>
 * 日期: 2019-10-31 00:06 <br>
 * 作者: 林明健 <br>
 */
public class BuildingDetailFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_news_title)
    MaequeeText tvNewsTitle;
    @BindView(R.id.view_divide)
    View viewDivide;
    @BindView(R.id.tv_news_content)
    TextView tvNewsContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.layout_content)
    LinearLayout layoutContent;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.video_player)
    EmptyControlVideo videoPlayer;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;

    private boolean isFromUser = false;
    private int page = 0;
    private int totalPage = 1;
    private BuildingInfo info;
    private Disposable imgDisposable;
    private Disposable srlDisposable;
    private Disposable animDisposable;

    public static BuildingDetailFragment newInstance(BuildingInfo info) {
        BuildingDetailFragment fragment = new BuildingDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_building_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        btnPrevious.setText("上一条");
        btnNext.setText("下一条");
        btnPrevious.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                videoPlayer.setVisibility(View.GONE);
                stopTaskScrollContent();
                showAlertDialog("播放异常，请检查视频源");
            }


            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                videoPlayer.setVisibility(View.VISIBLE);
                taskScrollContent();
            }
        });

        if (getArguments() != null && getArguments().getSerializable("info") != null) {
            info = (BuildingInfo) getArguments().getSerializable("info");
            if (info != null) {
                tvNewsTitle.setText(info.getButton_name() + "");
                requestBuildDetail(true);
            } else {
                showAlertDialogWithClose("数据异常，请退出重试");
            }
        }
        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_BEGIN, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                Log.e(TAG, "onEvent: 开始讲话");
                isFromUser = false;
                taskScrollContent();
            }
        });
        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_COMPLETE, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                Log.e(TAG, "onEvent: 下一条启动");
                getAppActivity().runOnUiThread(() -> {
                    if (btnNext.getVisibility() == View.VISIBLE && !isFromUser) {
                        showToast("自动为您播放下一条");
                        onViewClicked(btnNext);
                    }
                });
            }
        });
        RxBus.getDefault().subscribe(this, RxHelper.TAG_TTS_PROCESS, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                getAppActivity().runOnUiThread(() -> {
                    Log.e(TAG, "onEvent: " + RxHelper.TAG_SPEECH_PROCESS);
                    if (ivLoading.getVisibility() != View.VISIBLE) {
                        Glide.with(getAppActivity())
                                .load(R.drawable.img_speech_loading)
                                .into(ivLoading);
                        ivLoading.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        RxBus.getDefault().subscribe(this, RxHelper.TAG_TTS_ERROR, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                getAppActivity().runOnUiThread(() -> {
                    ivLoading.setVisibility(View.GONE);
                });
            }
        });
        RxBus.getDefault().subscribe(this, RxHelper.TAG_TTS_COMPLETE, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                getAppActivity().runOnUiThread(() -> {
                    Log.e(TAG, "onEvent: " + RxHelper.TAG_TTS_COMPLETE);
                    if (ivLoading.getVisibility() == View.VISIBLE) {
                        ivLoading.setVisibility(View.GONE);
                    }
                });
            }
        });
        taskShowNetSpeed();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        StaUnityUtils.setStaBigger();
        GSYVideoManager.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTaskScrollContent();
        stopSpeech();
        GSYVideoManager.releaseAllVideos();
    }

    private void requestBuildDetail(boolean showDialog) {
        if (animDisposable != null && !animDisposable.isDisposed()) {
            animDisposable.dispose();
        }
        if (page < 0) {
            page++;
            showToast("当前已为第一页");
            return;
        }
        if (page > totalPage - 1) {
            page--;
            showToast("已无更多内容");
            return;
        }
        if (imgDisposable != null && imgDisposable.isDisposed()) {
            imgDisposable.dispose();
            Log.e(TAG, "requestBuildDetail: imageDispose");
        }
        if (ivInfo.getVisibility() == View.VISIBLE) {
            ivInfo.setVisibility(View.GONE);
        }
        if (videoPlayer.getVisibility() == View.VISIBLE) {
            videoPlayer.setVisibility(View.GONE);
        }
        stopSpeech();
        stopTaskScrollContent();
        GSYVideoManager.releaseAllVideos();
        Observable<Long> delayObservable = Observable.timer(1, TimeUnit.SECONDS);
        Observable<BaseResponse<List<BuildingDetailInfo>>> responseObservable = request().getBuildingInfo(info.getId(), page);
        Observable.zip(delayObservable, responseObservable, (aLong, baseResp) -> baseResp)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<BuildingDetailInfo>>(getContext(), showDialog) {
                               @Override
                               public void onSuccess(List<BuildingDetailInfo> res) {

                               }

                               @Override
                               public void onFailure(Throwable e, String err) {
                                   showAlertDialog(err);
                               }

                               @Override
                               public void onSuccess(BaseResponse<List<BuildingDetailInfo>> baseResp) {
                                   totalPage = baseResp.getPage_count();
                                   if (page + 1 < totalPage) {
                                       btnNext.setVisibility(View.VISIBLE);
                                   }

                                   if (page - 1 >= 0) {
                                       btnPrevious.setVisibility(View.VISIBLE);
                                   }

                                   List<BuildingDetailInfo> data = baseResp.getData();
                                   if (data != null && !data.isEmpty()) {
                                       scrollView.scrollTo(0, 0);
                                       BuildingDetailInfo buildingDetailInfo = data.get(0);
                                       List<String> imgList = buildingDetailInfo.getImg_list();
                                       String content = buildingDetailInfo.getTitle() + "\n" + buildingDetailInfo.getContent();
                                       tvNewsContent.setText(content);
                                       if ("content".equals(buildingDetailInfo.getType())) {
                                           startSpeech(content);
                                           taskShowImage(imgList);
                                       } else if ("video".equals(buildingDetailInfo.getType())) {
                                           taskShowVideo(buildingDetailInfo.getVideo_url());
                                       } else if ("web".equals(buildingDetailInfo.getType()))
                                       {
                                           OnlineCheckActivity.startActivity(getAppActivity(), buildingDetailInfo.getLink_url()+"" );
                                       }
                                   }
                               }
                           }
                );
    }


    private void taskShowVideo(String videoUrl) {
        videoPlayer.start(videoUrl, false);
    }

    private void taskShowImage(List<String> imgList) {
        if (imgList == null || imgList.isEmpty()) {
            if (animDisposable != null && !animDisposable.isDisposed()) {
                animDisposable.dispose();
            }

            Observable.interval(7, 9, TimeUnit.SECONDS)
                    .takeWhile(aLong -> BakerTtsHandler.isPlaying())
                    .compose(RxHelper.io2m())
                    .as(RxLife.as(this))
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            animDisposable = d;
                        }

                        @Override
                        public void onNext(Long aLong) {
                            switchAnim(CommonUtils.getRandomAction(false));
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            return;
        }

        Observable<String> imgObservable = Observable.fromIterable(imgList);
        Observable<Long> timeObservable = Observable.interval(3, 5, TimeUnit.SECONDS);
        Observable.zip(imgObservable, timeObservable, (url, aLong) -> url).compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        imgDisposable = d;
                    }

                    @Override
                    public void onNext(String url) {
                        Glide.with(getAppActivity())
                                .load(url)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(ivInfo);
                        if (ivInfo.getVisibility() == View.GONE) {
                            ivInfo.setVisibility(View.VISIBLE);
                            //Glide新版动画改版了，使用原生动画取代放大功能
                            ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
                            animation.setDuration(1000);
                            animation.setRepeatCount(0);
                            ivInfo.setAnimation(animation);
                            Log.e(TAG, "onNext: " + "show Animation");
                            StaUnityUtils.getInstance().getFUStaEngine().updateAnimationOnce("anims/双手打开.bundle", null);
//                            RxBus.getDefault().post(, RxHelper.TAG_ANIMATION_UPDATE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ivInfo.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @OnClick({R.id.btn_next, R.id.btn_previous})
    public void onViewClicked(View view) {
        isFromUser = true;
        switch (view.getId()) {
            case R.id.btn_previous:
                page--;
                requestBuildDetail(false);
                break;
            case R.id.btn_next:
                page++;
                requestBuildDetail(false);
                break;
        }
    }

    private void taskScrollContent() {
        //定时滚动内容
        if (srlDisposable != null && !srlDisposable.isDisposed()) {
            Log.e(TAG, "taskScrollContent: isDisposed " + srlDisposable.isDisposed());
            srlDisposable.dispose();
        }
        srlDisposable = Observable.interval(1000, 100, TimeUnit.MILLISECONDS)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    if (scrollView != null) {
                        scrollView.smoothScrollBy(0, 1);
                    }
                });
    }

    private void taskShowNetSpeed() {
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .map(aLong -> NetworkUtils.getNetSpeed(YddxApplication.getContext().getApplicationInfo().uid))
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(s -> {
                    tvSpeed.setText(s + "");
                });
    }

    private void stopTaskScrollContent() {
        if (srlDisposable != null) {
            if (!srlDisposable.isDisposed()) {
                srlDisposable.dispose();
            }
            srlDisposable = null;
        }
    }
}
