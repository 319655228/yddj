package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.TipInfo;

import java.util.List;

public class TipAdapter extends BaseQuickAdapter<TipInfo, BaseViewHolder> {

    public TipAdapter(int layoutResId, @Nullable List<TipInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TipInfo item) {
        helper.setText(R.id.tv_tip, item.getTip());
    }
}
