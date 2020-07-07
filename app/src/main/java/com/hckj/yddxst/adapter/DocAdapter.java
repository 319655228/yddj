package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.DocInfo;

import java.util.List;

public class DocAdapter extends BaseQuickAdapter<DocInfo, BaseViewHolder> {
    public DocAdapter(int layoutResId, @Nullable List<DocInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DocInfo item) {
        helper.setText(R.id.tv_title, item.getName()+"");
    }
}
