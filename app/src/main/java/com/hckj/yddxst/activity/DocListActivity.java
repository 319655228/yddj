package com.hckj.yddxst.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.DocAdapter;
import com.hckj.yddxst.bean.DocClassifyInfo;
import com.hckj.yddxst.bean.DocInfo;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 文档列表页面 <br>
 * 日期: 2019-09-30 15:08 <br>
 * 作者: 林明健 <br>
 */
public class DocListActivity extends BaseActivity {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private List<DocInfo> docList;
    private DocAdapter mAdapter;

    /**
     * 描述: 启动文档列表页面 <br>
     * 日期: 2019-09-30 15:09 <br>
     * 作者: 林明健 <br>
     *
     * @param activity 启动页面
     * @param info     DocClassifyInfo
     * @return void
     */
    public static void startActivity(BaseActivity activity, DocClassifyInfo info, String title) {
        Intent intent = new Intent(activity, DocListActivity.class);
        intent.putExtra("info", info);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_doc_list;
    }

    @Override
    protected void initView() {
        super.initView();
        // 初始化Rv相关
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        docList = new ArrayList<>();
        mAdapter = new DocAdapter(R.layout.item_doc_classify_list, docList);
        mAdapter.openLoadAnimation();
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            DocInfo item = (DocInfo) adapter.getItem(position);
            DocDetailActivity.startActivity(this, item);
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        DocClassifyInfo info = (DocClassifyInfo) intent.getSerializableExtra("info");
        if (info == null) {
            showAlertDialogWithClose("页面加载错误");
            return;
        }
        tvTitle.setText(getIntent().getStringExtra("title") + "");
        request().getDocumentList(info.getId() + "")
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<DocInfo>>(this, false) {
                    @Override
                    public void onSuccess(List<DocInfo> res) {
                        docList.clear();
                        docList.addAll(res);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
