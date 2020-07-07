package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.CourseInfo;

import java.util.List;

public class HotSpotAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {
    public HotSpotAdapter(int layoutResId, @Nullable List<CourseInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CourseInfo item) {
        helper.setText(R.id.tv_name, item.getTitle());
    }
}
