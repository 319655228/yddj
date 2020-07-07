package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.MeetingInfo;

import java.util.List;

public class MeetingAdapter extends BaseQuickAdapter<MeetingInfo, BaseViewHolder> {

    public MeetingAdapter(int layoutResId, @Nullable List<MeetingInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MeetingInfo item) {
        helper.setText(R.id.tv_title, item.getTitle() + "");
    }
}
