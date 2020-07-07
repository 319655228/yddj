package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.SocketInfo;

import java.util.List;

public class MeetingSpeakAdapter extends BaseQuickAdapter<SocketInfo.Content, BaseViewHolder> {
    public MeetingSpeakAdapter(int layoutResId, @Nullable List<SocketInfo.Content> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SocketInfo.Content item) {
        helper.setText(R.id.tv_content, item.getNickname() + ":" + item.getChat_content() );
    }
}
