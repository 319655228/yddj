package com.hckj.yddxst.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.NewClassifyInfo;

import java.util.List;

public class NewClassifyAdapter extends BaseQuickAdapter<NewClassifyInfo, BaseViewHolder> {
    private String mSelectId;

    public NewClassifyAdapter(int layoutResId, @Nullable List<NewClassifyInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewClassifyInfo item) {
        String name = item.getName() == null ? "" : item.getName();
        String id = item.getId() == null ? "" : item.getId();
        if (id.equals(mSelectId)) {
            helper.setTextColor(R.id.tv_name, Color.parseColor("#FFB72A3A")).setBackgroundRes(R.id.tv_name, R.drawable.bg_new_classify_selected);
        } else {
            helper.setTextColor(R.id.tv_name, Color.parseColor("#FFEDCD91")).setBackgroundRes(R.id.tv_name, R.drawable.bg_new_classify);
        }
        helper.setText(R.id.tv_name, name);
    }

    public String getSelectId() {
        return mSelectId;
    }

    public void notifyDataSetChanged(String mSelectId) {
        this.mSelectId = mSelectId;
        this.notifyDataSetChanged();
    }


}
