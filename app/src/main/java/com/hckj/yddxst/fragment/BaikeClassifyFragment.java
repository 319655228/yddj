package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.adapter.BaikeClassifyAdapter;
import com.hckj.yddxst.bean.BaikeClassifyInfo;
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
 * 描述: 百科分类Fragment <br>
 * 日期: 2020-06-03 16:33 <br>
 * 作者: 林明健 <br>
 */
public class BaikeClassifyFragment extends BaseFragment {
    @BindView(R.id.rv_data)
    RecyclerView rvData;
    @BindView(R.id.btn_previous)
    ImageView btnPrevious;
    @BindView(R.id.btn_next)
    ImageView btnNext;

    private BaikeClassifyAdapter mAdapter;
    private ArrayList<BaikeClassifyInfo> mList = new ArrayList<>();
    private final static int crollV = 300;

    public static BaikeClassifyFragment newInstance(ArrayList<BaikeClassifyInfo> list) {
        BaikeClassifyFragment fragment = new BaikeClassifyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baike_classify;
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getAppActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvData.setLayoutManager(layoutManager);

        // 获取上个页面传参
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getSerializable("list") != null) {
            ArrayList<BaikeClassifyInfo> newList = (ArrayList<BaikeClassifyInfo>) arguments.getSerializable("list");
            mList.clear();
            mList.addAll(newList);
        }

        mAdapter = new BaikeClassifyAdapter(R.layout.item_party_class, mList);
        mAdapter.openLoadAnimation();
        rvData.setAdapter(mAdapter);
        rvData.addItemDecoration(new SpacesItemDecoration(35));

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            BaikeClassifyInfo item = (BaikeClassifyInfo) adapter.getItem(position);
            if (item != null) {
                List<BaikeClassifyInfo> childList = item.getChild_classify_list();
                if (childList != null) {
                    replaceFragment(BaikeClassifyFragment.newInstance(new ArrayList<>(childList)));
                } else {
                    replaceFragment(BaikeDetailFragment.newInstance(item.getId()));
                }
            }
        });

        if (mList != null && !mList.isEmpty())
            return;
        request().getBaikeClassify()
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<BaikeClassifyInfo>>(getAppActivity(), true) {
                    @Override
                    public void onSuccess(List<BaikeClassifyInfo> res) {
                        if (res == null || res.isEmpty()) {
                            showAlertDialog("当前暂无可查看的百科列表");
                            return;
                        }

                        mList.clear();
                        mList.addAll(res);
                        mAdapter.notifyDataSetChanged();
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
