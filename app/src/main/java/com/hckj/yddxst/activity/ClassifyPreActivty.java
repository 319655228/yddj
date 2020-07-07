package com.hckj.yddxst.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.ClassifyPreAdater;
import com.hckj.yddxst.bean.StudyBtnInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 描述:  党课前一页（通过接口获取不同的按钮)<br>
 * 日期: 2019-09-26 23:52 <br>
 * 作者: 林明健 <br>
 */
public class ClassifyPreActivty extends BaseActivity {
    @BindView(R.id.rv_data)
    RecyclerView rvData;

    private ClassifyPreAdater mAdapter;
    private List<StudyBtnInfo> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classify_pre;
    }

    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, ClassifyPreActivty.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvData.setLayoutManager(layoutManager);

        mAdapter = new ClassifyPreAdater(R.layout.item_course_list, mList);
        mAdapter.openLoadAnimation();
        rvData.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Object item = adapter.getItem(position);
            if (item instanceof StudyBtnInfo) {
                StudyBtnInfo info = (StudyBtnInfo) item;
                if ("web".equals(info.getType())) {
                    OnlineCheckActivity.startActivity(this, info.getLink_url());
                } else {
                    startActivity(ClassifyActivity.class);
                }
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        request().getStudyBtnList()
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<StudyBtnInfo>>(this, true) {
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

}
