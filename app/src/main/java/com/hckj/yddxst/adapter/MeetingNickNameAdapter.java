package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.SocketInfo;

import java.util.List;

public class MeetingNickNameAdapter extends BaseQuickAdapter<SocketInfo.Content, BaseViewHolder> {


    public MeetingNickNameAdapter(int layoutResId, @Nullable List<SocketInfo.Content> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SocketInfo.Content item) {
        String nickName = item.getNickname() + (item.getJoin_mode() == 0 ? "(现场)" : "(远程)");
        helper.setText(R.id.tv_nick_name, nickName);
    }
}
