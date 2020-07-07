package com.hckj.yddxst.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.CourseListAdapter;
import com.hckj.yddxst.bean.CourseInfo;
import com.hckj.yddxst.bean.VideoInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseListActivity extends BaseActivity {
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;
    private List<CourseInfo> mList = new ArrayList<>();
    private CourseListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course_list;
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new CourseListAdapter(R.layout.item_course_list, mList);
        mAdapter.openLoadAnimation();
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            VideoActivity.startActivity(this, new VideoInfo(mList.get(position)));
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String typeName = getIntent().getStringExtra("type");
        if (typeName == null) {
            Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
            return;
        }

        tvMode.setText(typeName);
        request().getCourseListByType(typeName)
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

    public static void startActivity(Context ctx, String type) {
        Intent intent = new Intent(ctx, CourseListActivity.class);
        intent.putExtra("type", type);
        ctx.startActivity(intent);
    }


    @OnClick({R.id.btn_previous, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                rvList.smoothScrollBy(0, -1100);
                break;
            case R.id.btn_next:
                rvList.smoothScrollBy(0, 1100);
                break;
        }
    }
}
