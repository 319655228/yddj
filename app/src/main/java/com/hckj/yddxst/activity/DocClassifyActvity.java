package com.hckj.yddxst.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.DocClassifyAdapter;
import com.hckj.yddxst.bean.DocClassifyInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.utils.ImgChangeUtil;
import com.hckj.yddxst.widget.SpacesItemDecoration;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 文档分类页面<br>
 * 日期: 2019-09-30 11:12 <br>
 * 作者: 林明健 <br>
 */
public class DocClassifyActvity extends BaseActivity {
    @BindView(R.id.rv_data)
    RecyclerView rvData;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_previous)
    ImageView btnPrevious;
    @BindView(R.id.btn_next)
    ImageView btnNext;
    private DocClassifyAdapter mAdapter;
    private List<DocClassifyInfo> mList = new ArrayList<>();
    private String title;
    public String itemPosition;
    private final static int crollV = 1006;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_doc_class;
    }

    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, DocClassifyActvity.class);
        activity.startActivity(intent);
    }

    public static void startActivity(BaseActivity activity, ArrayList<DocClassifyInfo> infoList, String title) {
        Intent intent = new Intent(activity, DocClassifyActvity.class);
        intent.putExtra("infoList", infoList);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvData.setLayoutManager(layoutManager);

        //创建适配器
        mAdapter = new DocClassifyAdapter(R.layout.item_doc_list, mList);
        mAdapter.openLoadAnimation();
        rvData.setAdapter(mAdapter);
        rvData.addItemDecoration(new SpacesItemDecoration(45));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            DocClassifyInfo item = (DocClassifyInfo) adapter.getItem(position);
            if (item != null) {
                List<DocClassifyInfo> childList = item.getChild_classify_list();
                String nextTitle = TextUtils.isEmpty(title)? item.getName() : title + ">"+item.getName();
                if (childList != null && !childList.isEmpty()) {
                    DocClassifyActvity.startActivity(this, new ArrayList<>(childList), nextTitle);
                } else {
                    DocListActivity.startActivity(this, item, nextTitle);
                }
            }
        });
        ImgChangeUtil imgChangeUtil = new ImgChangeUtil();
        imgChangeUtil.imgChange(rvData,btnPrevious,btnNext,crollV);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        List<DocClassifyInfo> infoList = (List<DocClassifyInfo>) intent.getSerializableExtra("infoList");
        title = intent.getStringExtra("title");
        title = title == null ? "" : title;
        if (infoList != null) {
            mList.clear();
            mList.addAll(infoList);
            mAdapter.notifyDataSetChanged();
        } else
            request().getDocumentClassify()
                    .compose(RxHelper.io2m())
                    .as(RxLife.as(this))
                    .subscribe(new YddxObserver<List<DocClassifyInfo>>(this, true) {

                        @Override
                        public void onSuccess(List<DocClassifyInfo> list) {
                            mList.clear();
                            mList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Throwable e, String err) {
                            showAlertDialogWithClose("加载列表出错，" + err);
                        }
                    });
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
    }


    @OnClick({R.id.btn_previous, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                rvData.smoothScrollBy(0, -crollV);
                break;
            case R.id.btn_next:
                rvData.smoothScrollBy(0, crollV);
                break;
        }
    }
}
