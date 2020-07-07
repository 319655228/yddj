package com.hckj.yddxst.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.StudyBtnInfo;

import java.util.List;

/**
 * 描述: 党课适配器<br>
 * 日期: 2019-09-27 00:18 <br>
 * 作者: 林明健 <br>
 */
public class ClassifyPreAdater extends BaseQuickAdapter<StudyBtnInfo, BaseViewHolder> {
    public ClassifyPreAdater(int layoutResId, @Nullable List<StudyBtnInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StudyBtnInfo item) {
        String name = TextUtils.isEmpty(item.getButton_name()) ? item.getButton_name() : item.getButton_name();
        name = name == null ? "" : name;
        helper.setText(R.id.tv_name, name);
    }
}
