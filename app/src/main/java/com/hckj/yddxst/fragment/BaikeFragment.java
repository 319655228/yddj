package com.hckj.yddxst.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.faceunity.FUStaEngine;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.PoliticalPhotoActivity;
import com.hckj.yddxst.data.EffectFactory;
import com.hckj.yddxst.utils.StaUnityUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 百科页面 <br>
 * 日期: 2020-06-03 17:11 <br>
 * 作者: 林明健 <br>
 */
public class BaikeFragment extends BaseFragment {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.qr_code)
    ImageView qrCode;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baike;
    }

    public static BaikeFragment newInstance(String url, String title, String qrCodeUrl) {
        BaikeFragment fragment = new BaikeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        bundle.putString("qrCodeUrl", qrCodeUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        super.initView();

        String url = "";
        String title = "";
        String qrCodeUrl = "";
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getString("url") != null) {
            url = arguments.getString("url");
            title = arguments.getString("title") + "";
            qrCodeUrl = arguments.getString("qrCodeUrl") + "";
        }

        // 设置标题
        tvTitle.setText(title);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // 运行js脚本
        if (url != null && url.contains("rzkeji")) {
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        //加载二维码
        Glide.with(BaikeFragment.this)
                .load(qrCodeUrl)
                .into(qrCode);

        // 支持屏幕缩放
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setGeolocationEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadsImagesAutomatically(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                setStaAnimLeft();
                super.onPageFinished(webView, s);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 释放资源
        webView.destroy();
        webView = null;
        resetStaAnim();
    }

    @OnClick({R.id.btn_previous, R.id.btn_next, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                webView.scrollBy(0, -300);
                break;
            case R.id.btn_next:
                webView.scrollBy(0, 300);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
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
        FUStaEngine fuStaEngine = StaUnityUtils.getInstance().getFUStaEngine();
        fuStaEngine.setAnimTransX(-65f);  // 数值越小越偏做
        fuStaEngine.setAnimTransY(190f);  // 数值越大 越高
        fuStaEngine.setAnimTransZ(-1600f);
    }
}
