package com.hckj.yddxst.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.bean.DocInfo;

import java.util.List;

import butterknife.BindView;

/**
 * 描述:  文档详情页面<br>
 * 日期: 2019-10-13 10:39 <br>
 * 作者: 林明健 <br>
 */
public class DocDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_content)
    ImageView ivContent;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;

    public static void startActivity(BaseActivity activity, DocInfo item) {
        Intent intent = new Intent(activity, DocDetailActivity.class);
        intent.putExtra("info", item);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_doc_detail;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        DocInfo info = (DocInfo) intent.getSerializableExtra("info");
        if (info == null) {
            showToast("系统异常，请打开重试");
            finish();
            return;
        }

        tvTitle.setText(info.getName() + "");

        Glide.with(this)
                .load(info.getQrcode_url())
                .into(ivQrcode);

        List<String> img_list = info.getImg_list();
        if (img_list != null && !img_list.isEmpty()) {
            Glide.with(this)
                    .load(img_list.get(0))
                    .into(ivContent);
        }
    }
}
