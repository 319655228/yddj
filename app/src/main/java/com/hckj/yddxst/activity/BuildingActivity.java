package com.hckj.yddxst.activity;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.BuildingAdapter;
import com.hckj.yddxst.bean.BuildingInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.utils.SpUtil;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cdflynn.android.library.turn.TurnLayoutManager;

public class BuildingActivity extends BaseActivity {
    @BindView(R.id.rv_btn)
    RecyclerView rvBtn;
    private List<BuildingInfo> mList = new ArrayList<>();
    private BuildingAdapter mAdapter;
    private TurnLayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_building;
    }

    @Override
    protected void initView() {
        super.initView();
        layoutManager = new TurnLayoutManager(this,
                TurnLayoutManager.Gravity.END,
                TurnLayoutManager.Orientation.VERTICAL,
                250,
                250,
                false);
        rvBtn.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new BuildingAdapter(R.layout.item_building_btn, mList);
        mAdapter.openLoadAnimation();
        rvBtn.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            BuildingInfo buildingInfo = mList.get(position);
            if ("unity_content_list".equals(buildingInfo.getType())) {
                BuildingDetailActivity.startActivity(this, buildingInfo);
            } else if ("unity_web_view".equals(buildingInfo.getType())) {
                OnlineCheckActivity.startActivity(this, buildingInfo.getLink_url());
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String deviceNum = SpUtil.get(this, SpUtil.DEVICE_NUM, "");
        request().getBuildingInit(deviceNum)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<BuildingInfo>>(this) {
                    @Override
                    public void onSuccess(List<BuildingInfo> buildList) {
                        if (buildList == null || buildList.isEmpty()){
                            showAlertDialogWithClose("当前设备号暂无可查看组织活动");
                            return;
                        }
                        mList.clear();
                        for (BuildingInfo buildingInfo : buildList) {
                            if (buildingInfo != null && !TextUtils.isEmpty(buildingInfo.getType())) {
                                mList.add(buildingInfo);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialogWithClose(err);
                    }
                });
    }

}
