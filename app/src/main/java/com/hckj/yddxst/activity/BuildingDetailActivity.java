package com.hckj.yddxst.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.bean.BuildingDetailInfo;
import com.hckj.yddxst.bean.BuildingInfo;
import com.hckj.yddxst.net.BaseResponse;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class BuildingDetailActivity extends BaseActivity {
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_news_title)
    MaequeeText tvNewsTitle;
    @BindView(R.id.tv_news_content)
    TextView tvNewsContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.video_player)
    EmptyControlVideo videoPlayer;

    private int page = 0;
    private int totalPage = 1;
    private BuildingInfo info;
    private Disposable imgDisposable;

    public static void startActivity(BaseActivity activity, BuildingInfo info) {
        Intent intent = new Intent(activity, BuildingDetailActivity.class);
        intent.putExtra("info", info);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_building_detail;
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
                showAlertDialog("播放异常，请检查视频源");
            }


            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                videoPlayer.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        info = (BuildingInfo) intent.getSerializableExtra("info");
        if (info == null) {
            showAlertDialogWithClose("数据异常");
            return;
        }
        tvNewsTitle.setText("" + info.getButton_name());
        requestBuildDetail(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //定时滚动内容
        Observable.interval(3000, 100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    if (scrollView != null) {
                        scrollView.smoothScrollBy(0, 1);
                    }
                });
        GSYVideoManager.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        Log.e(TAG, "Question Destroy");
    }

    @OnClick({R.id.btn_previous, R.id.btn_next})
    public void onViewClicked(View view) {
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

    private void requestBuildDetail(boolean showDialog) {
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
        GSYVideoManager.releaseAllVideos();
        request().getBuildingInfo(info.getId(), page)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<BuildingDetailInfo>>(this, showDialog) {
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
                                       tvNewsContent.setText("" + buildingDetailInfo.getContent());
                                       if ("content".equals(buildingDetailInfo.getType())) {
                                           taskShowImage(imgList);
                                       } else if ("video".equals(buildingDetailInfo.getType())) {
                                           taskShowVideo(buildingDetailInfo.getVideo_url());
                                       }
                                   }
                               }
                           }
                );
    }


    private void taskShowVideo(String videoUrl) {
        videoPlayer.start(videoUrl);
    }

    private void taskShowImage(List<String> imgList) {
        if (imgList == null || imgList.isEmpty())
            return;
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
                        Glide.with(BuildingDetailActivity.this)
                                .load(url)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(ivInfo);
                        ivInfo.setVisibility(View.VISIBLE);
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

}
