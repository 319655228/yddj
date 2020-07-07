package com.hckj.yddxst.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.HotSpotAdapter;
import com.hckj.yddxst.bean.CourseInfo;
import com.hckj.yddxst.bean.VideoInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HotSpotActivity extends BaseActivity {
    @BindView(R.id.rv_data)
    RecyclerView rvData;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;

    private List<CourseInfo> mList = new ArrayList<>();
    private HotSpotAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotspot;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvData.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new HotSpotAdapter(R.layout.item_course_list, mList);
        mAdapter.openLoadAnimation();
        rvData.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            CourseInfo courseInfo = mList.get(position);
            VideoActivity.startActivity(HotSpotActivity.this, new VideoInfo(courseInfo));

        });

        request().getHotVideoList(100, 0)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<CourseInfo>>(this) {
                    @Override
                    public void onSuccess(List<CourseInfo> res) {
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

    @OnClick({R.id.rv_data, R.id.btn_back, R.id.btn_previous, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_previous:
                rvData.smoothScrollBy(0, -1100);
                break;
            case R.id.btn_next:
                rvData.smoothScrollBy(0, 1100);
                break;
        }
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
