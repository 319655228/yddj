package com.hckj.yddxst.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.ClassifyActivity;
import com.hckj.yddxst.activity.OnlineCheckActivity;
import com.hckj.yddxst.adapter.ClassifyPreAdater;
import com.hckj.yddxst.bean.StudyBtnInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.widget.SpacesItemDecoration;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PartyClassFragment extends BaseFragment {
    @BindView(R.id.rv_data)
    RecyclerView rvData;

    private ClassifyPreAdater mAdapter;
    private List<StudyBtnInfo> mList = new ArrayList<>();

    static PartyClassFragment newInstance() {
        return new PartyClassFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_party_class;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getAppActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvData.setLayoutManager(layoutManager);

        mAdapter = new ClassifyPreAdater(R.layout.item_party_class, mList);
        mAdapter.openLoadAnimation();
        rvData.setAdapter(mAdapter);
        rvData.addItemDecoration(new SpacesItemDecoration(35));

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Object item = adapter.getItem(position);
            if (item instanceof StudyBtnInfo) {
                StudyBtnInfo info = (StudyBtnInfo) item;
                if ("web".equals(info.getType())) {
                    OnlineCheckActivity.startActivity(getAppActivity(), info.getLink_url());
                } else if ("meeting".equals(info.getType())) {
                    replaceFragment(MeetingFragment.newInstance(info.getLink_url()));
                } else {
                    startActivity(ClassifyActivity.class);
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request().getStudyBtnList()
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<StudyBtnInfo>>(getAppActivity(), true) {
                    @Override
                    public void onSuccess(List<StudyBtnInfo> list) {
                        if (list == null || list.isEmpty()) {
                            showAlertDialog("列表为空");
                            return;
                        }
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialogWithClose(err + "");
                    }
                });
    }

    @OnClick({R.id.btn_previous, R.id.btn_next, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                rvData.smoothScrollBy(0, -300);
                break;
            case R.id.btn_next:
                rvData.smoothScrollBy(0, 300);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
}
