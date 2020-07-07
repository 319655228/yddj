package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.DocClassifyInfo;

import java.util.List;

public class DocClassifyAdapter extends BaseQuickAdapter<DocClassifyInfo, BaseViewHolder> {
    public DocClassifyAdapter(int layoutResId, @Nullable List<DocClassifyInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DocClassifyInfo item) {
        helper.setText(R.id.tv_title, item.getName() + "");
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
