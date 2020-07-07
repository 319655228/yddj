package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.BuildingInfo;

import java.util.List;

public class BuildingAdapter extends BaseQuickAdapter<BuildingInfo, BaseViewHolder> {
    public BuildingAdapter(int layoutResId, @Nullable List<BuildingInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BuildingInfo item) {
        helper.setText(R.id.btn_building, (item.getButton_name() + "").trim());
    }
}
