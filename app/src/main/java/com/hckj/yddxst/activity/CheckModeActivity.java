package com.hckj.yddxst.activity;

import android.os.Bundle;
import android.view.View;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;

import butterknife.OnClick;

public class CheckModeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_mode;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_check_party, R.id.tv_check_mental})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_check_party:
                startActivity(OnlineCheckActivity.class);
                break;
//            case R.id.tv_check_mental:
//                startActivity(CheckMentalActivity.class);
//                break;
        }
    }
}
