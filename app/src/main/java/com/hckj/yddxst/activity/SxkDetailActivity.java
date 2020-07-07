package com.hckj.yddxst.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.bean.SxkDetailInfo;
import com.hckj.yddxst.task.SxkDetailTask;

import butterknife.BindView;
import butterknife.OnClick;

public class SxkDetailActivity extends BaseActivity {

    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.sxk_title)
    TextView sxkTitle;
    @BindView(R.id.tv_sxk_content)
    TextView tvSxkContent;

    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sxk_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        this.id = this.getIntent().getStringExtra("id");
        new SxkDetailTask(SxkDetailActivity.this).execute(id);
        showDialog("加载中");
    }
    public void faildRefreshViews() {
        hideDialog();
        Toast.makeText(this, "服务异常！", Toast.LENGTH_SHORT).show();
    }
    public void refreshViews(SxkDetailInfo sxkDetailInfo) {
        hideDialog();
        sxkTitle.setText(sxkDetailInfo.getTitle());
        tvSxkContent.setText(Html.fromHtml(sxkDetailInfo.getContent()));
    }
    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }


}