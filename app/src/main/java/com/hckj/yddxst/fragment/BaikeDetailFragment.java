package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.adapter.BaikeDetailAdapter;
import com.hckj.yddxst.bean.BaikeInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.utils.ImgChangeUtil;
import com.hckj.yddxst.widget.SpacesItemDecoration;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 百科列表 Fragment <br>
 * 日期: 2020-06-03 16:38 <br>
 * 作者: 林明健 <br>
 */
public class BaikeDetailFragment extends BaseFragment {
    @BindView(R.id.rv_data)
    RecyclerView rvData;

    @BindView(R.id.rl_container)
    View rlContainer;
    @BindView(R.id.btn_previous)
    ImageView btnPrevious;
    @BindView(R.id.btn_next)
    ImageView btnNext;

    private BaikeDetailAdapter mAdapter;
    private List<BaikeInfo> mList = new ArrayList<>();
    private final static int crollV = 300;

    public static BaikeDetailFragment newInstance(int classifyId) {
        BaikeDetailFragment fragment = new BaikeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("classifyId", classifyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baike_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getAppActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        rvData.setLayoutManager(layoutManager);
        mAdapter = new BaikeDetailAdapter(R.layout.item_baike_detail, mList);
        mAdapter.openLoadAnimation();
        rvData.setAdapter(mAdapter);
        rvData.addItemDecoration(new SpacesItemDecoration(35));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            BaikeInfo item = (BaikeInfo) adapter.getItem(position);
            if (item != null) {
                replaceFragment(BaikeFragment.newInstance(item.getRead_url(), item.getTitle(),item.getQrcode_url()));
            }
        });
        // 获取上个页面传参
        Bundle arguments = getArguments();
        if (arguments == null) {
            showAlertDialogWithClose("程序异常，请打开重试！");
            return;
        }
        int classifyId = arguments.getInt("classifyId");
        request().getBaikeInfo(classifyId, 1, 100)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<BaikeInfo>>(getAppActivity(), true) {
                    @Override
                    public void onSuccess(List<BaikeInfo> res) {
                        if (res == null || res.isEmpty()) {
                            showAlertDialog("当前暂无可查看的百科列表");
                            return;
                        }

                        mList.clear();
                        mList.addAll(res);
                        mAdapter.notifyDataSetChanged();
                        rlContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });

        ImgChangeUtil imgChangeUtil = new ImgChangeUtil();
        imgChangeUtil.imgChange(rvData,btnPrevious,btnNext,crollV);

    }

    @OnClick({R.id.btn_previous, R.id.btn_next, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                rvData.smoothScrollBy(0, -crollV);
                break;
            case R.id.btn_next:
                rvData.smoothScrollBy(0, crollV);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
}
