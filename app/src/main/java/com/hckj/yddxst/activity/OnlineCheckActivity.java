package com.hckj.yddxst.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;

import butterknife.BindView;

public class OnlineCheckActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;

    private final String DEF_URL = "http://zddx.rzkeji.com/#/question";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_online_check;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("url");
        if (url == null) {
            url = DEF_URL;
        }
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // 运行js脚本
        if (!TextUtils.isEmpty(url) && url.contains("rzkeji")) {
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }

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
                super.onPageFinished(webView, s);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放资源
        webView.destroy();
        webView = null;
    }

    public static void startActivity(BaseActivity activity, String url) {
        Intent intent = new Intent(activity, OnlineCheckActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void btnBackClick() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

}

