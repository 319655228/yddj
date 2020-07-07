package com.hckj.yddxst.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FollowHeartActivity extends BaseActivity {
    @BindView(R.id.tv_guide)
    TextView tvGuide;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_quiz)
    TextView tvTest;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow_heart;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_guide, R.id.tv_content, R.id.tv_quiz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_guide:
                ClassifyActivity.startActivity(this, 0, "guide", "classify");
                break;
            case R.id.tv_content:
                startActivity(HotSpotActivity.class);
                break;
            case R.id.tv_quiz:
                startActivity(OnlineCheckActivity.class);
                break;
        }
    }
}
