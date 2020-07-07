package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.BaikeInfo;

import java.util.List;

public class BaikeDetailAdapter extends BaseQuickAdapter<BaikeInfo, BaseViewHolder> {
    public BaikeDetailAdapter(int layoutResId, @Nullable List<BaikeInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaikeInfo item) {
        helper.setText(R.id.tv_name, item.getTitle() + "");
    }
}
