package com.hckj.yddxst.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.rxbus.RxBus;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.CourseTypeAdapter;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class QaGuideActivity extends BaseActivity {
    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;
    private List<String> mList = new ArrayList<>();
    private CourseTypeAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qa_guide;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvType.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new CourseTypeAdapter(R.layout.item_course_type, mList);
        mAdapter.openLoadAnimation();
        rvType.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            CourseListActivity.startActivity(this, mList.get(position));
        });

        request().getCourseTypeList()
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<String>>(this) {
                    @Override
                    public void onSuccess(List<String> res) {
                        mList.clear();
                        mList.addAll(res);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });
    }

    @OnClick({R.id.btn_previous, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                rvType.smoothScrollBy(0, -1100);
                break;
            case R.id.btn_next:
                rvType.smoothScrollBy(0, 1100);
                break;
        }
    }

    public static void startActivity(BaseActivity activity, int reqCode) {
        Intent intent = new Intent(activity, QaGuideActivity.class);
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        RxBus.getDefault().post(RxHelper.TAG_SPEECH_RECOVER, RxHelper.TAG_SPEECH_RECOVER);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        RxBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
