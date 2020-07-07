package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.rxbus.RxBus;
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
import com.hckj.yddxst.bean.TipInfo;
import com.hckj.yddxst.bean.VideoInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.widget.QuestionAnswerLayout;
import com.hckj.yddxst.widget.RecordDialog;
import com.hckj.yddxst.widget.TipLayout;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.rxjava.rxlife.RxLife;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HelperFragment extends BaseFragment {
    @BindView(R.id.iv_record)
    ImageView ivRecord;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_guide)
    ImageView ivGuide;
    @BindView(R.id.layout_qa)
    QuestionAnswerLayout layoutQa;
    @BindView(R.id.layout_tips)
    TipLayout layoutTips;

    private Gson mGson;
    private SpeechRecognizer mIat;
    private StringBuffer mSpeachQuestion;
    private RecordDialog mRecordDialog;

    static HelperFragment newInstance() {
        return new HelperFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_helper;
    }

    @Override
    protected void initView() {
        super.initView();
        mRecordDialog = new RecordDialog(getAppActivity()).setFinishListener(v -> {
            if (mIat != null) {
                mIat.stopListening();
                ivRecord.setVisibility(View.VISIBLE);
                mRecordDialog.hide();
            }
        });
        layoutQa.setClickListener(new QuestionAnswerLayout.OnClickListener() {
            @Override
            public void onBackClick() {
                hideAnswerLayout();
                startSpeech(getString(R.string.other_quesiotn));
                switchAnim("双手张开", "高兴");
            }

            @Override
            public void onItemClick(Answerable info) {
                stopSpeech();
                if (info instanceof MeetingInfo) {
                    replaceFragment(MeetingFragment.newInstance((MeetingInfo) info));
                } else if (info instanceof VideoInfo) {
                    VideoActivity.startActivity(getAppActivity(), (VideoInfo) info);
                } else if (info instanceof DocInfo) {
                    DocDetailActivity.startActivity(getAppActivity(), (DocInfo) info);
                }
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化Iat
        initIatParam();

        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_COMPLETE, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String flag) {
                if ("HELPER_BYE".equals(flag)) {
                    onBackPressed();
                } else if ("HELPER_SEARCHING".equals(flag)) {
                    String question = tvQuestion.getText().toString();
                    searchByKeyword(question);
                }
            }
        });

        request().getTips().compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<TipInfo>>(getAppActivity(), false) {
                    @Override
                    public void onSuccess(List<TipInfo> res) {
                        if (res != null && !res.isEmpty()) {
                            layoutTips.setData(res, (adapter, view, position) -> {
                                TipInfo tipInfo = (TipInfo) adapter.getItem(position);
                                if (tipInfo != null && tipInfo.getTip() != null) {
                                    tvQuestion.setText(tipInfo.getTip());
                                    stopSpeech();
                                    startSpeech(getString(R.string.searching), "HELPER_SEARCHING");
                                    switchAnim("疑惑");
                                }
                            });
                            layoutTips.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {

                    }
                });

        // 鞠躬欢迎
        startSpeech(getString(R.string.sayhi));
        switchAnim("鞠躬");
    }


    private void startRecord() {
        stopSpeech();
        if (mIat == null) return;
        mSpeachQuestion = new StringBuffer();
        int ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showAlertDialog("语音识别失败");
        }
    }

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int volume, byte[] bytes) {
            Log.e(TAG, "onVolumeChanged: " + volume);
            mRecordDialog.updateVolume(volume);
        }

        @Override
        public void onBeginOfSpeech() {
            ivRecord.setVisibility(View.INVISIBLE);
            mRecordDialog.show();
        }

        @Override
        public void onEndOfSpeech() {
            ivRecord.setVisibility(View.VISIBLE);
            mRecordDialog.hide();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            try {
                String result = results.getResultString(); // 未解析的
                RecognizerInfo info = mGson.fromJson(result, RecognizerInfo.class);
                String w;
                for (int i = 0; i < info.getWs().size(); i++) {
                    w = info.getWs().get(i).getCw().get(0).getW();
                    mSpeachQuestion.append(w);
                }
                if (isLast) {
                    String question = mSpeachQuestion.toString();
                    if (!TextUtils.isEmpty(question) && question.matches(".*[88|没有|返回|再见|退出|拜拜|推出|结束].*")) {
                        startSpeech(getString(R.string.saybye), "HELPER_BYE");
                        switchAnim("挥手");
                        return;
                    }
                    if (TextUtils.isEmpty(question)) {
                        startSpeech(getString(R.string.no_question));
                    } else {
                        startSpeech(getString(R.string.searching), "HELPER_SEARCHING");
                        switchAnim("疑惑");
                    }
                    tvQuestion.setText(question);
                }
            } catch (Exception e) {
                showToast("语音识别异常");
                startSpeech(getString(R.string.other_quesiotn));
                switchAnim("双手张开", "高兴");
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            String errStr = speechError.getPlainDescription(false);
            Log.e(TAG, "onError: " + errStr);
            if (errStr != null && errStr.contains("没有说话")) {
                showToast("您好像没有说话");
            } else {
                showAlertDialogWithClose("语音识别失败:【" + speechError.getErrorDescription() + "】，请重试");
            }
        }

        @Override
        public void onEvent(int eventType, int i1, int i2, Bundle bundle) {

        }
    };

    private void searchByKeyword(String kw) {
        Log.e(TAG, "searchByKeyword: " + kw);
        request().getQaByWord(kw)
                .filter(b -> isVisible())
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
                            startSpeech(getString(R.string.no_asr));
                            switchAnim("惊讶", "悲伤");
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        startSpeech(getString(R.string.no_asr));
                        switchAnim("惊讶", "悲伤");
                    }
                });
    }

    private void showAnswerLayout() {
        ivGuide.setVisibility(View.GONE);
        tvQuestion.setVisibility(View.GONE);
        ivRecord.setVisibility(View.GONE);
        layoutTips.setVisibility(View.GONE);
        layoutQa.setVisibility(View.VISIBLE);
    }

    private void hideAnswerLayout() {
        ivGuide.setVisibility(View.VISIBLE);
        tvQuestion.setVisibility(View.VISIBLE);
        ivRecord.setVisibility(View.VISIBLE);
        layoutTips.setVisibility(View.VISIBLE);
        layoutQa.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        stopSpeech();
        if (mIat != null) {
            if (mIat.isListening()) {
                mIat.cancel();
            }
            mIat.destroy();
            mIat = null;
        }
        RxBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.iv_record, R.id.iv_guide, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_record:
                startRecord();
                break;
            case R.id.iv_guide:
                stopSpeech();
                startActivity(QaGuideActivity.class);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    /**
     * 描述: 初始化IAT参数 <br>
     * 日期: 2020-03-08 20:54 <br>
     * 作者: 林明健 <br>
     */
    private void initIatParam() {
        mGson = new Gson();
        // IAT语音听写对象
        mIat = SpeechRecognizer.createRecognizer(getAppActivity(), code -> {
            if (code != ErrorCode.SUCCESS) {
                showAlertDialogWithClose("语音模块初始化失败，请退出重试");
            }
        });
        if (mIat == null) {
            showAlertDialog("Iat is null");
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
        mIat.setParameter(SpeechConstant.VAD_BOS, "2000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
    }
}
