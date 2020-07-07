package com.hckj.yddxst.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PartyGroupActivity extends BaseActivity {
    @BindView(R.id.sv_process_member)
    ScrollView svProcessMember;
    @BindView(R.id.sv_process_group)
    ScrollView svProcessGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_party_group;
    }


    public static void startActivity(Context context, int flag) {
        Intent intent = new Intent(context, PartyGroupActivity.class);
        intent.putExtra("flag", flag); // 标志位 ： 0 党员大会流程， 1 党小组流程
        context.startActivity(intent);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag", 0);
        if (flag == 0) {
            svProcessMember.setVisibility(View.VISIBLE);
            svProcessGroup.setVisibility(View.GONE);
        } else if (flag == 1) {
            svProcessMember.setVisibility(View.GONE);
            svProcessGroup.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.sv_process_member, R.id.sv_process_group})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sv_process_member:
                break;
            case R.id.sv_process_group:
                break;
        }
    }
}
