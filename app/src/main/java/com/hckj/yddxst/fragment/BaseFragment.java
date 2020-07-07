package com.hckj.yddxst.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.rxbus.RxBus;
import com.faceunity.AnimationStateListener;
import com.faceunity.FUStaEngine;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.IndexActivity;
import com.hckj.yddxst.data.EffectFactory;
import com.hckj.yddxst.net.BaseApi;
import com.hckj.yddxst.net.RetrofitManager;
import com.hckj.yddxst.utils.StaUnityUtils;
import com.rxjava.rxlife.RxLife;

import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;

public abstract class BaseFragment extends Fragment {
    public final String TAG = this.getClass().getSimpleName();
    /**
     * 用户设置的ContentView
     */
    protected View mContentView;
    /**
     * View有没有加载过
     */
    protected boolean isViewInitiated;
    /**
     * 页面是否可见
     */
    protected boolean isVisibleToUser;
    /**
     * 是不是加载过
     */
    protected boolean isDataInitiated;

    private IndexActivity mActivity;

    private AlertDialog mAlertDialog;

    private Button btnBack;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewInitiated) {
            initView();
        }
        isViewInitiated = true;
        loadData();
    }

    protected abstract int getLayoutId();

    /**
     * 懒加载
     */
    private void loadData() {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated)) {
            isDataInitiated = true;
            lazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            loadData();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (IndexActivity) context;
        Log.e(TAG, "onAttach: ");
    }

    /**
     * 加载数据操作,在视图创建之前初始化
     */
    protected  void lazyLoad(){};


    @Override
    public void onDestroy() {
        mActivity = null;
        mContentView = null;
        RxBus.getDefault().unregister(this);

        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, mContentView);
            btnBack = mContentView.findViewById(R.id.btn_back);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> {
                    onBackPressed();
//                    getAppActivity().onBackPressed();
                });
            }
        }
        return mContentView;
    }


    protected void initView() {

    }

    /**
     * 打开Activity
     */
    public final void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 打开Activity
     */
    public final void startActivity(Class<?> clazz, @Nullable Bundle options) {
        Intent intent = new Intent(getAppActivity(), clazz);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivity(intent);
    }

    /**
     * 获取当前的Activity
     */
    protected final IndexActivity getAppActivity() {
        return mActivity;
    }

    /**
     * 显示Toast
     *
     * @param content 显示的内容
     */
    protected void showToast(String content) {
        if (TextUtils.isEmpty(content) && getAppActivity() == null)
            return;
        if (getAppActivity() == null) {
            return;
        }
        try {
            getAppActivity().runOnUiThread(() -> Toast.makeText(getAppActivity(), content, Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected BaseApi request() {
        return RetrofitManager.getInstance().create(BaseApi.class);
    }

    /**
     * 显示对话框
     *
     * @param message 显示的内容
     */
    protected void showAlertDialog(String message) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        IndexActivity appActivity = getAppActivity();
        if (appActivity == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(appActivity, android.R.color.transparent);
        final View dialogView = appActivity.getLayoutInflater().inflate(R.layout.dialog_alert_yddx, null, false);
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        TextView tvInfo = dialogView.findViewById(R.id.tv_msg);
        tvInfo.setText(message + "");

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        btnConfirm.setText("确认");
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(v -> mAlertDialog.dismiss());

        mAlertDialog.setCancelable(true);
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.show();
    }

    protected void showAlertDialogWithCancel(String message, String confirmText, String cancelText, View.OnClickListener listener){
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        IndexActivity appActivity = getAppActivity();
        if (appActivity == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(appActivity, android.R.color.transparent);
        final View dialogView = appActivity.getLayoutInflater().inflate(R.layout.dialog_alert_yddx, null, false);
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        TextView tvInfo = dialogView.findViewById(R.id.tv_msg);
        tvInfo.setText(message + "");

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        btnConfirm.setText(confirmText);
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(v->{
            listener.onClick(v);
            mAlertDialog.dismiss();
        });

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setText(cancelText);
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setOnClickListener(v->{
            mAlertDialog.dismiss();
        });

        mAlertDialog.setCancelable(true);
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.show();
    }

    protected void showAlertDialogWithClose(String message) {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        IndexActivity activity = getAppActivity();
        if (activity == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, android.R.color.transparent);
        final View dialogView = activity.getLayoutInflater().inflate(R.layout.dialog_alert_yddx, null, false);
        builder.setView(dialogView);
        mAlertDialog = builder.create();

        TextView tvInfo = dialogView.findViewById(R.id.tv_msg);
        tvInfo.setText("" + message + "");

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        btnConfirm.setText("确认");
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setOnClickListener(v -> {
            mAlertDialog.dismiss();
            onBackPressed();
        });

        mAlertDialog.setCancelable(false);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();
    }

    protected void onBackPressed() {
        Log.e(TAG, "onBackPressed: ");
        IndexActivity appActivity = getAppActivity();
        if (appActivity != null) {
            appActivity.onBackPressed();
        }
    }

    void replaceFragment(Fragment fragment) {
        IndexActivity activity = getAppActivity();
        if (activity != null) {
            activity.replaceFragment(fragment);
        }
    }

    protected void stopSpeech() {
        Log.e(TAG, "stopSpeech: ");
        IndexActivity appActivity = getAppActivity();
        if (appActivity != null) {
            appActivity.stopSpeech();
        }
    }

    protected void stopSpeechNoAnim() {
        IndexActivity appActivity = getAppActivity();
        if (appActivity != null) {
            appActivity.stopSpeechNoAnim();
        }
    }

    protected void startSpeech(String word) {
        if (TextUtils.isEmpty(word) || "\n".equals(word)) {
            return;
        }
        word = word.replace("·", "，");
        IndexActivity appActivity = getAppActivity();
        if (appActivity != null) {
            appActivity.startSpeech(word);
        }
    }

    protected void startSpeech(String word, String flag) {
        if (TextUtils.isEmpty(word) || "\n".equals(word)) {
            return;
        }
        word = word.replace("·", "，");
        IndexActivity appActivity = getAppActivity();
        if (appActivity != null) {
            appActivity.startSpeech(word, flag);
        }
    }

    protected void showBackBtn() {
        if (btnBack != null) {
            btnBack.setVisibility(View.VISIBLE);
        }
    }

    protected void hideBackBtn() {
        if (btnBack != null) {
            btnBack.setVisibility(View.GONE);
        }
    }

    protected void switchAnim(String animPath) {
        switchAnim(animPath, null);
    }

    private Subscription subscription;

    protected void switchAnim(String... animPath) {
        if (subscription != null) {
            subscription.cancel();
            subscription = null;
        }
        int length = animPath.length;
        String finalAnim = animPath[length - 1];

        Flowable.fromArray(animPath)
                .onBackpressureBuffer()
                .as(RxLife.as(this))
                .subscribe(new FlowableSubscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                        subscription = s;
                    }

                    @Override
                    public void onNext(String path) {
                        Log.e(TAG, "动作开始: " + path);
                        switchAnim(path, () -> {
                            if (isVisible()){
                                subscription.request(1);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private void switchAnim(String animPath, boolean isCycle, AnimationStateListener listener) {
        if (TextUtils.isEmpty(animPath)) {
            return;
        }
        // 判断动作是否合理
        String mapPath = "anims/" + animPath + ".bundle";
        boolean isValid = false;
        String[] validPath = EffectFactory.getAnimsPath();
        for (String path : validPath) {
            if (mapPath.equals(path)) {
                isValid = true;
                break;
            }
        }
        // 合理即可调用动作
        if (isValid) {
            FUStaEngine fuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
            if (fuStaEngine != null) {
                if (isCycle) {
                    fuStaEngine.updateAnimation(mapPath);
                } else {
                    fuStaEngine.updateAnimationOnce(mapPath, listener);
                }
            }
        }
    }

    private void switchAnim(String animPath, AnimationStateListener listener) {
        switchAnim(animPath, false, listener);
    }
}
