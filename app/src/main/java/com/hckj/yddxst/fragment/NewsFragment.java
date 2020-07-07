package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.rxbus.RxBus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.faceunity.FUStaEngine;
import com.hckj.yddxst.R;
import com.hckj.yddxst.YddxApplication;
import com.hckj.yddxst.adapter.NewClassifyAdapter;
import com.hckj.yddxst.adapter.NewsListAdapter;
import com.hckj.yddxst.bean.NewClassifyInfo;
import com.hckj.yddxst.bean.NewsInfo;
import com.hckj.yddxst.bean.NewsInfo2;
import com.hckj.yddxst.net.BaseResponse;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.task.NewsListTask;
import com.hckj.yddxst.utils.CommonUtils;
import com.hckj.yddxst.utils.NetworkUtils;
import com.hckj.yddxst.utils.StaUnityUtils;
import com.hckj.yddxst.widget.EmptyControlVideo;
import com.hckj.yddxst.widget.MaequeeText;
import com.rxjava.rxlife.RxLife;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class NewsFragment extends BaseFragment {
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.iv_type)
    ImageView ivType;
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
    @BindView(R.id.tv_news_time)
    TextView tvNewsTime;
    @BindView(R.id.layout_content)
    LinearLayout layoutContent;
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.rv_classify)
    RecyclerView rvClassify;
    @BindView(R.id.news_list)
    ListView newsList;

    private boolean isFirst = true;
    private boolean isFromUser = false;
    private boolean isShowVideo = false;
    private boolean canAnim = false;

    private Disposable imgDisposable;
    private Disposable srlDisposable;
    private Disposable animDisposable;
    //当前页数
    private int mPage = 0;
    //新闻类型
    private String mNewType;
    //视频新闻地址
    private String mVideoUrl;
    //新闻标签（分类）
    private String newsClassify = "";
    //新闻标签适配器
    private NewClassifyAdapter newClassifyAdapter;
    //新闻标签列表
    private List<NewClassifyInfo> classifyInfoList = new ArrayList<>();

    private List<NewsInfo2> newsInfoList = new ArrayList<>(10);
    private NewsListAdapter adapter;

    @Override
    protected void lazyLoad() {

    }

    /**
     * 描述: 将模型设置在左上角位置 <br>
     * 日期: 2019-09-22 00:54 <br>
     * 作者: 林明健 <br>
     */
    private void setStaAnimLeft() {
        Log.e(TAG, "setStaAnimLeft: ");
        FUStaEngine fuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
        fuStaEngine.setAnimTransX(40f);  // 数值越小越偏左
        fuStaEngine.setAnimTransY(30f);  // 数值越大 越高
        fuStaEngine.setAnimTransZ(-800f);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        super.initView();
        setStaAnimLeft();
        btnPrevious.setText("上一条新闻");
        btnNext.setText("下一条新闻");
        initData();
        new NewsListTask(NewsFragment.this).execute("zuji");

//        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
//                @Override
//                public void onPlayError(String url, Object... objects) {
//                    super.onPlayError(url, objects);
//                    videoPlayer.setVisibility(View.GONE);
//                    ivLoading.setVisibility(View.GONE);
//                    stopTaskScrollContent();
//                    showAlertDialog("播放异常，请检查视频源");
//                }
//
//            @Override
//            public void onPrepared(String url, Object... objects) {
//                super.onPrepared(url, objects);
//                FUStaEngine fuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
//                fuStaEngine.pauseMediaPlayer();
//                fuStaEngine.stopMediaPlayer();
//                ivLoading.setVisibility(View.GONE);
//                ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
//                animation.setDuration(1200);
//                animation.setRepeatCount(0);
//                videoPlayer.setAnimation(animation);
//                videoPlayer.setVisibility(View.VISIBLE);
//                switchAnim("双手打开");
//            }
//
//            @Override
//            public void onAutoComplete(String url, Object... objects) {
//                super.onAutoComplete(url, objects);
//            }
//        });


        newClassifyAdapter = new NewClassifyAdapter(R.layout.item_new_classify, classifyInfoList);
        newClassifyAdapter.openLoadAnimation(); // 加载动画

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        rvClassify.setLayoutManager(layoutManager);
        rvClassify.setAdapter(newClassifyAdapter);

        newClassifyAdapter.setOnItemClickListener((adapter, view, position) -> {
            NewClassifyInfo item = (NewClassifyInfo) adapter.getItem(position);
            if (item == null || TextUtils.isEmpty(item.getId()) || item.getId().equals(newClassifyAdapter.getSelectId()))
                return;
            newsClassify = item.getId();
            newClassifyAdapter.notifyDataSetChanged(newsClassify);
            getNews(mNewType, true);
        });
    }

    private void initData() {

        adapter = new NewsListAdapter(this.getActivity(), newsInfoList);
        newsList.setAdapter(adapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_BEGIN, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                Log.e(TAG, "onEvent: " + s);
                isFromUser = false;
                taskScrollContent();
            }
        });
        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_COMPLETE, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                Log.e(TAG, "onEvent: " + "下一条启动");
                if (btnNext.getVisibility() == View.VISIBLE && !isFromUser && !isShowVideo) {
                    onViewClicked(btnNext);
                }
                canAnim = false;
//                if (isShowVideo && !TextUtils.isEmpty(mVideoUrl)) {
                    videoPlayer.start("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png", false);
                    taskScrollContent();
//                }
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

        request().getNewsClassify()
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<NewClassifyInfo>>(getAppActivity(), false) {
                    @Override
                    public void onSuccess(List<NewClassifyInfo> res) {
                        if (res != null && !res.isEmpty()) {
                            NewClassifyInfo newClassifyInfo = res.get(0);
                            newsClassify = newClassifyInfo.getId();
                            classifyInfoList.clear();
                            classifyInfoList.addAll(res);
                            newClassifyAdapter.notifyDataSetChanged(newsClassify);
                        }
                        getNews("content", true);
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
//        StaUnityUtils.setStaBigger();
        setStaAnimLeft();
    }

    private void taskScrollContent() {
        //定时滚动内容
        if (srlDisposable != null && !srlDisposable.isDisposed()) {
            Log.e(TAG, "taskScrollContent: isDisposed " + srlDisposable.isDisposed());
            srlDisposable.dispose();
        }
        srlDisposable = Observable.interval(5000, 100, TimeUnit.MILLISECONDS)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    if (scrollView != null) {
                        scrollView.smoothScrollBy(0, 1);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
        stopTaskScrollContent();
        stopSpeech();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "Question Destroy");
        GSYVideoManager.releaseAllVideos();
        RxBus.getDefault().unregister(this);
        StaUnityUtils.resetSta();
        super.onDestroy();
    }

    private void stopTaskScrollContent() {
        if (srlDisposable != null) {
            if (!srlDisposable.isDisposed()) {
                srlDisposable.dispose();
            }
            srlDisposable = null;
        }
    }

    private void getNews(String type, boolean showDialog) {
        if (mPage < 0) {
            mPage = 0;
            showToast("当前已是第一条新闻");
            return;
        }
        if (imgDisposable != null && !imgDisposable.isDisposed()) {
            imgDisposable.dispose();
        }

        if (animDisposable != null && !animDisposable.isDisposed()) {
            animDisposable.dispose();
        }

        if (ivInfo.getVisibility() == View.VISIBLE) {
            ivInfo.setVisibility(View.GONE);
        }
        if (videoPlayer.getVisibility() == View.VISIBLE) {
            videoPlayer.setVisibility(View.GONE);
        }
        ivLoading.setVisibility(View.GONE);
        stopSpeech();
        GSYVideoManager.releaseAllVideos();
        mNewType = type;
        if ("content".equals(mNewType)) {
            ivType.setImageResource(R.drawable.img_news_video);
        } else {
            ivType.setImageResource(R.drawable.img_news_content);
        }
        stopTaskScrollContent();
        // 延迟1秒
        Observable<Long> delayObservable = Observable.timer(1, TimeUnit.SECONDS);
        Observable<BaseResponse<List<NewsInfo>>> newsObservable =
                request().getNews(mNewType, newsClassify, mPage);
        Observable.zip(delayObservable, newsObservable, (aLong, listBaseResponse) -> listBaseResponse)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<NewsInfo>>(getContext(), showDialog) {
                    @Override
                    public void onSuccess(List<NewsInfo> newsInfoList) {
                        if (newsInfoList == null || newsInfoList.isEmpty()) {
                            showToast("已无更多新闻内容");
                            return;
                        }
                        scrollView.scrollTo(0, 0);
                        NewsInfo newsInfo = newsInfoList.get(0);
                        tvNewsTime.setText(newsInfo.getNews_time() + "");
                        tvNewsContent.setText(newsInfo.getContent() + "");
                        tvNewsTitle.setText(newsInfo.getTitle() + "");
//                        List<String> imgList = new List<String>;
//                        taskShowImage(newsInfo.getImgs_url());
                        if ("content".equals(newsInfo.getType())) {
                            startSpeech(newsInfo.getTitle() + "\n" + newsInfo.getContent());
                            taskShowImage(newsInfo.getImgs_url());
                        } else if ("video".equals(newsInfo.getType())) {
                            taskShowVideo(newsInfo.getTitle(), newsInfo.getVideo_url());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });
    }

    @OnClick({R.id.btn_previous, R.id.btn_next, R.id.iv_type})
    public void onViewClicked(View view) {
        isFromUser = true;
        switch (view.getId()) {
            case R.id.btn_previous:
                mPage--;
                getNews(mNewType, false);
                break;
            case R.id.btn_next:
                mPage++;
                getNews(mNewType, false);
                break;
            case R.id.iv_type:
                if ("content".equals(mNewType)) {
                    getNews("video", true);
                } else {
                    getNews("content", true);
                }
                isFirst = true;
                break;
            default:
                break;
        }
    }

    private void taskShowVideo(String title, String videoUrl) {
        Log.e(TAG, "taskShowVideo: " + videoUrl);
        isShowVideo = true;
        mVideoUrl = videoUrl;
        startSpeech(title);
    }

    @Override
    protected void startSpeech(String word) {
        if (isFirst) {
            word = ("content".equals(mNewType) ? "以下是最新的图文新闻快讯。" : "以下是最新的视频新闻快讯。")
                    + word;
            isFirst = false;
            switchAnim("鞠躬");
        }
        super.startSpeech(word);
        canAnim = true;
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

    private void taskShowImage(List<String> imgList) {
        isShowVideo = false;
        if (imgList == null || imgList.isEmpty()) {
            Log.e(TAG, "taskAnim: isDisposed " + (animDisposable == null ? " NULL" : animDisposable.isDisposed()));
            if (animDisposable != null && !animDisposable.isDisposed()) {
                animDisposable.dispose();
            }

            Observable.interval(7, 9, TimeUnit.SECONDS)
                    .takeWhile(aLong -> canAnim)
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
        Observable.zip(imgObservable, timeObservable, (url, aLong) -> url)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        imgDisposable = d;
                    }

                    @Override
                    public void onNext(String url) {
                        Toast.makeText(NewsFragment.this.getActivity(), "666------" , Toast.LENGTH_SHORT).show();
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
                            switchAnim("双手打开");
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

    @Override
    protected void stopSpeech() {
        super.stopSpeech();
        canAnim = false;
    }

    public void refreshListView(List<NewsInfo2> listBeans) {
        NewsInfo2 newsInfo2 = listBeans.get(0);
        List<String> list = newsInfo2.getImg_list();
        taskShowImage(list);
        adapter.setNewsList(listBeans);
        adapter.notifyDataSetChanged();
    }
}
