package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.OnlineCheckActivity;
import com.hckj.yddxst.adapter.BuildingAdapter;
import com.hckj.yddxst.bean.BuildingInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.utils.SpUtil;
import com.hckj.yddxst.utils.StaUnityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cdflynn.android.library.turn.TurnLayoutManager;

public class BuildingFragment extends BaseFragment {
    @BindView(R.id.rv_btn)
    RecyclerView rvBtn;
    @BindView(R.id.btn_back)
    Button btnBack;
    private List<BuildingInfo> mList = new ArrayList<>();
    private BuildingAdapter mAdapter;
    private TurnLayoutManager layoutManager;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_building;
    }

    @Override
    protected void initView() {
        super.initView();

        layoutManager = new TurnLayoutManager(getActivity(),
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
            stopSpeech();
            BuildingInfo buildingInfo = mList.get(position);
            if ("unity_content_list".equals(buildingInfo.getType())) {
                replaceFragment(BuildingDetailFragment.newInstance(buildingInfo));
            } else if ("unity_web_view".equals(buildingInfo.getType())) {
                OnlineCheckActivity.startActivity(getAppActivity(), buildingInfo.getLink_url());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String deviceNum = SpUtil.get(getActivity(), SpUtil.DEVICE_NUM, "");
        request().getBuildingInit(deviceNum)
                .compose(RxHelper.io2m())
                .subscribe(new YddxObserver<List<BuildingInfo>>(getActivity()) {
                    @Override
                    public void onSuccess(List<BuildingInfo> buildList) {
                        if (buildList == null || buildList.isEmpty()) {
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
                        switchAnim("鞠躬", "信任");
                        startSpeech("您好，我是您的虚拟公务员。请点击右下方的模块，我会为您进行相应的引导和服务。");
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
        StaUnityUtils.resetSta();
        super.onDestroy();
    }
}
