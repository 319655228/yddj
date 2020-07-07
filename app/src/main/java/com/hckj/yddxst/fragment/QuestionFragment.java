package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.rxbus.RxBus;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.DocDetailActivity;
import com.hckj.yddxst.activity.QaGuideActivity;
import com.hckj.yddxst.activity.VideoActivity;
import com.hckj.yddxst.bean.Answerable;
import com.hckj.yddxst.bean.DocInfo;
import com.hckj.yddxst.bean.MeetingInfo;
import com.hckj.yddxst.bean.QaInfo;
import com.hckj.yddxst.bean.RecognizerInfo;
import com.hckj.yddxst.bean.VideoInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.widget.QuestionAnswerLayout;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.rxjava.rxlife.RxLife;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 问答Fragment <br>
 * 日期: 2019-10-19 17:50 <br>
 * 作者: 林明健 <br>
 */
public class QuestionFragment extends BaseFragment implements QuestionAnswerLayout.OnClickListener {
    private static final int STATUS_SAYHI = 1;
    private static final int STATUS_SEARCHING = 2;
    private static final int STATUS_OTHER = 3;
    private static final int STATUS_NOASR = 4;
    private static final int STATUS_BYE = 5;
    private static final int STATUS_NOQUESTION = 6;

    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.layout_qa)
    QuestionAnswerLayout layoutQa;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    private int STATUS = 0;
    @BindView(R.id.skv_recoding)
    SpinKitView skvRecoding;
    @BindView(R.id.iv_guide)
    ImageView ivGuide;

    private SpeechRecognizer mIat;
    private Gson mGson;
    private String mQuestion;
    private StringBuffer mSpeechSb;
    private int repeatCount = 0;
    private final int MAX_REPEAT_COUNT = 3; // 最多询问次数

    static QuestionFragment newInstance() {
        QuestionFragment fragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initView() {
        super.initView();
        layoutQa.setClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIatParam();
        // RxBus注册相关事件
        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_RECOVER, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                switchStatus(STATUS_OTHER);
            }
        });
        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_COMPLETE, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                Log.e(TAG, "testcomplete: " + s);
                switch (STATUS) {
                    case STATUS_SAYHI:
                        startQuestion();
                        break;
                    case STATUS_SEARCHING:
                        searchByKeyword(mQuestion + "");
                        break;
                    case STATUS_OTHER:
                        startQuestion();
                        break;
                    case STATUS_BYE:
                        onBackPressed();
                        break;
                    case STATUS_NOASR:
                        switchStatus(STATUS_OTHER);
                        break;
                    case STATUS_NOQUESTION:
                        switchStatus(STATUS_OTHER);
                    default:
                        break;
                }
            }
        });
        switchStatus(STATUS_SAYHI);
    }

    /**
     * 修改当前状态变量
     *
     * @param status 状态 STATUS_XXX
     */
    private void switchStatus(int status) {
        stopSpeechNoAnim();
        this.STATUS = status;
        switch (status) {
            case STATUS_SAYHI:
                startSpeech(getString(R.string.sayhi));
                switchAnim("鞠躬");
                break;
            case STATUS_SEARCHING:
                startSpeech(getString(R.string.searching));
                switchAnim("疑惑");
                break;
            case STATUS_OTHER:
                startSpeech(getString(R.string.other_quesiotn));
                switchAnim("双手张开","高兴");
                break;
            case STATUS_BYE:
                startSpeech(getString(R.string.saybye));
                break;
            case STATUS_NOASR:
                startSpeech(getString(R.string.no_asr));
                switchAnim("惊讶","悲伤");
                break;
            case STATUS_NOQUESTION:
                startSpeech(getString(R.string.no_question));
                break;
            default:
                break;
        }
    }

    //初始化科大讯飞引擎
    private void initIatParam() {
        mGson = new Gson();
        // IAT语音听写对象
        mIat = SpeechRecognizer.createRecognizer(getAppActivity(), code -> {
            if (code != ErrorCode.SUCCESS) {
                showAlertDialogWithClose("语音模块初始化失败");
            }
        });
        if (mIat == null) {
            showAlertDialog("mIat is null");
            return;
        }
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "3000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
    }

    /**
     * 讯飞引擎准备开始录音
     */
    private void startQuestion() {
        if (mIat == null) return;
        mSpeechSb = new StringBuffer();
        int ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showToast("语音识别失败");
        } else {
            showToast("请开始提出您的问题");
        }
    }

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int volume, byte[] bytes) {
        }

        @Override
        public void onBeginOfSpeech() {
            tvQuestion.setVisibility(View.VISIBLE);
            skvRecoding.setVisibility(View.VISIBLE);
        }

        @Override
        public void onEndOfSpeech() {
            skvRecoding.setVisibility(View.GONE);
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            try {
                String result = results.getResultString(); // 未解析的
                Log.e(TAG, "onResult: " + result);
                RecognizerInfo info = mGson.fromJson(result, RecognizerInfo.class);
                String w;
                for (int i = 0; i < info.getWs().size(); i++) {
                    w = info.getWs().get(i).getCw().get(0).getW();
                    mSpeechSb.append(w);
                }
                if (isLast) {
                    mQuestion = mSpeechSb.toString();
                    if (!TextUtils.isEmpty(mQuestion) &&
                            (mQuestion.contains("88") || mQuestion.contains("没有了") ||
                                    mQuestion.contains("没有") || mQuestion.contains("拜拜") ||
                                    mQuestion.contains("再见") || mQuestion.contains("退出") || mQuestion.contains("推出"))) {
                        switchStatus(STATUS_BYE);
                        return;
                    }
                    Log.e(TAG, "onResult: " + mQuestion);
                    if ("".equals(mQuestion)) {
                        repeatCount++;
                        switchStatus(STATUS_NOQUESTION);
                    } else {
                        repeatCount = 0;
                        switchStatus(STATUS_SEARCHING);
                    }
                    tvQuestion.setText(mSpeechSb.toString());
                }
            } catch (Exception e) {
                showToast("语音识别异常");
                switchStatus(STATUS_OTHER);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            String errStr = speechError.getPlainDescription(false);
            Log.e(TAG, "onError: " + errStr);
            if (errStr != null && errStr.contains("没有说话")) {
                if (repeatCount >= MAX_REPEAT_COUNT) {
                    showToast("检测不到您提问，欢迎下次使用");
                    switchStatus(STATUS_BYE);
                    return;
                }
                showToast("您好像没有说话");
                repeatCount++;
                switchStatus(STATUS_OTHER);
            } else {
                showAlertDialogWithClose("语音识别失败:" + speechError.getErrorDescription() + "，请退出重试");
            }
        }

        @Override
        public void onEvent(int eventType, int i1, int i2, Bundle bundle) {

        }
    };

    private void searchByKeyword(String kw) {
        Log.e(TAG, "searchByKeyword: " + kw);
        request().getQaByWord(kw)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<QaInfo>(getActivity(), false) {
                    @Override
                    public void onSuccess(QaInfo res) {
                        if (res != null && (
                                !res.getDocument_list().isEmpty() ||
                                        !res.getMeeting_list().isEmpty() ||
                                        !res.getVideo_list().isEmpty())
                        ) {
                            layoutQa.setData(res);
                            showAnswerLayout();
                        } else {
                            switchStatus(STATUS_NOASR);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        switchStatus(STATUS_NOASR);
                    }
                });
    }

    @OnClick(R.id.iv_guide)
    public void onViewClicked() {
        stopSpeech();
        startActivity(QaGuideActivity.class);
    }

    private void showAnswerLayout() {
        ivGuide.setVisibility(View.GONE);
        tvTips.setVisibility(View.GONE);
        layoutQa.setVisibility(View.VISIBLE);
    }

    private void hideAnswerLayout() {
        ivGuide.setVisibility(View.VISIBLE);
        tvTips.setVisibility(View.VISIBLE);
        layoutQa.setVisibility(View.GONE);
        showBackBtn();
    }

    @Override
    public void onDestroy() {
        if (mIat != null) {
            if (mIat.isListening()) {
                mIat.cancel();
            }
            mIat.destroy();
        }
        stopSpeech();
        super.onDestroy();
    }


    @Override
    public void onBackClick() {
        hideAnswerLayout();
        switchStatus(STATUS_OTHER);
    }

    @Override
    public void onItemClick(Answerable info) {
        stopSpeech();
        if (info instanceof MeetingInfo) {
            hideBackBtn();
            replaceFragment(MeetingFragment.newInstance((MeetingInfo) info));
        } else if (info instanceof VideoInfo) {
            VideoActivity.startActivity(getAppActivity(), (VideoInfo) info);
        } else if (info instanceof DocInfo) {
            DocDetailActivity.startActivity(getAppActivity(), (DocInfo) info);
        }
    }
}
