package com.hckj.yddxst.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.BaikeClassifyInfo;

import java.util.List;

/**
 * 描述: 百科分类适配器<br>
 * 日期: 2020-06-03 16:12 <br>
 * 作者: 林明健 <br>
 */
public class BaikeClassifyAdapter extends BaseQuickAdapter<BaikeClassifyInfo, BaseViewHolder> {
    public BaikeClassifyAdapter(int layoutResId, @Nullable List<BaikeClassifyInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaikeClassifyInfo item) {
        helper.setText(R.id.tv_name, item.getName()+"");
    }
}
