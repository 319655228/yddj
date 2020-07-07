package com.hckj.yddxst.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.SocketInfo;

import java.util.List;

public class MeetingUserAdapter extends BaseQuickAdapter<SocketInfo.Content, BaseViewHolder> {
    public MeetingUserAdapter(int layoutResId, @Nullable List<SocketInfo.Content> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SocketInfo.Content item) {
        helper.setText(R.id.tv_nick_name, item.getNickname() + "");
        ImageView ivAvator = helper.getView(R.id.iv_avator);
        Glide.with(helper.itemView)
                .load(item.getPhoto_url())
                .into(ivAvator);
    }
}
