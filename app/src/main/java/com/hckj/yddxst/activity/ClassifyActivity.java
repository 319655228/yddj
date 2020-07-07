package com.hckj.yddxst.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.ClassifyAdapter;
import com.hckj.yddxst.bean.ClassifyInfo;
import com.hckj.yddxst.bean.CourseInfo;
import com.hckj.yddxst.bean.VideoInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ClassifyActivity extends BaseActivity {
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.rv_data)
    RecyclerView rvData;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String type = "classify";
    private String classifyType;
    private ClassifyAdapter mAdapter;
    private List<CourseInfo> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classify;
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvData.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new ClassifyAdapter(R.layout.item_course_list, mList);
        mAdapter.openLoadAnimation();
        rvData.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            CourseInfo courseInfo = mList.get(position);
            if ("classify".equals(type)) {
                ClassifyActivity.startActivity(this, courseInfo.getId(), classifyType, type);
            } else if ("video".equals(type)) {
                VideoActivity.startActivity(this, new VideoInfo(courseInfo));
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int classifyId = intent.getIntExtra("classifyId", 0);

        type = intent.getStringExtra("type");
        classifyType = intent.getStringExtra("classifyType");

        if (classifyType == null) {
            classifyType = "teach";
        }
        if ("teach".equals(classifyType)) {
            tvTitle.setText("党建讲师");
        } else if ("guide".equals(classifyType)) {
            tvTitle.setText("学习引导");
        }

        request().getTeachClassifyOrVideo(classifyId, classifyType)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<ClassifyInfo>(this) {
                    @Override
                    public void onSuccess(ClassifyInfo res) {
                        type = res.getType();
                        mList.clear();
                        mList.addAll(res.getList());
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
                rvData.smoothScrollBy(0, -1100);
                break;
            case R.id.btn_next:
                rvData.smoothScrollBy(0, 1100);
                break;
        }
    }

    public static void startActivity(Context ctx, int classifyId, String classifyType, String type) {
        Intent intent = new Intent(ctx, ClassifyActivity.class);
        intent.putExtra("classifyId", classifyId);
        intent.putExtra("classifyType", classifyType);
        intent.putExtra("type", type);
        ctx.startActivity(intent);
    }

}
