package com.hckj.yddxst.activity;

import butterknife.BindView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.widget.SlowlyProgressBar;

public class DJZXActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    private SlowlyProgressBar slowlyProgressBar;

    private final String DEF_URL = "http://zddxapi.rzkeji.com/dangv2/dataIndex";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_online_check;
    }
    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, DJZXActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void initData(Bundle savedInstanceState) {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // 运行js脚本
        settings.setBuiltInZoomControls(true);//support zoom
        settings.setUseWideViewPort(true);// 这个很关键
        settings.setLoadWithOverviewMode(true);
        if (!TextUtils.isEmpty(DEF_URL) && DEF_URL.contains("rzkeji")) {
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
        /** 第二种动画模式，another solution */
        findViewById(R.id.p).setVisibility(View.GONE);
        slowlyProgressBar =
                new SlowlyProgressBar
                        (
                                (ProgressBar) findViewById(R.id.ProgressBar)
                        );
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                slowlyProgressBar.onProgressStart();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                slowlyProgressBar.onProgressChange(newProgress);
            }
        });
        webView.loadUrl(DEF_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
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

//    public static void startActivity(BaseActivity activity, String url) {
//        Intent intent = new Intent(activity, DJZXActivity.class);
//        intent.putExtra("url", url);
//        activity.startActivity(intent);
//    }

    @Override
    protected void btnBackClick() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}