package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.PlanQrcodeInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.widget.QrcodeImageView;
import com.rxjava.rxlife.RxLife;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import butterknife.BindView;
import butterknife.OnClick;

public class PlanFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_qrcode)
    QrcodeImageView ivQrcode;
    @BindView(R.id.btn_return)
    ImageView btnReturn;

    static PlanFragment newInstance() {
        return new PlanFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_plan;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request().getQrcode()
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<PlanQrcodeInfo>(getAppActivity()) {
                    @Override
                    public void onSuccess(PlanQrcodeInfo info) {
                        ivQrcode.show(info.getQrcode_url());
                        tvTitle.setText(info.getTitle() + "");
                        switchAnim("双手打开");
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialogWithClose(err);
                    }
                });
    }


    @Override
    public void onDestroy() {
        stopSpeech();
        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }

    @OnClick(R.id.btn_return)
    public void onViewClicked() {
        onBackPressed();
    }
}
