package com.hckj.yddxst.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.CourseInfo;

import java.util.List;

public class ClassifyAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {
    public ClassifyAdapter(int layoutResId, @Nullable List<CourseInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CourseInfo item) {
        String name = TextUtils.isEmpty(item.getName()) ? item.getTitle() : item.getName();
        name = name == null ? "" : name;
        helper.setText(R.id.tv_name, name);
    }
}
