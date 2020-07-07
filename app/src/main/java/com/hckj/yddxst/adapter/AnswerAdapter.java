package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.VideoInfo;

import java.util.List;

public class AnswerAdapter extends BaseQuickAdapter<VideoInfo, BaseViewHolder> {
    public AnswerAdapter(int layoutResId, @Nullable List<VideoInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VideoInfo item) {
        helper.setText(R.id.tv_name, item.getTitle() + "");
    }
}
