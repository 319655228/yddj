package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.MeetingInfo;

import java.util.List;

/**
 * 描述: 会议模式选择议题列表 <br>
 * 日期: 2019-11-09 23:53 <br>
 * 作者: 林明健 <br>
 */
public class MeetingSelectLayout extends LinearLayout {
    private MeetingListView rvCurrency;
    private MeetingListView rvModel;
    private TextView tvTitle;
    private View btnReturn;

    public MeetingSelectLayout(Context context) {
        super(context);
        init(context);
    }

    public MeetingSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeetingSelectLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_select, this);
        rvCurrency = view.findViewById(R.id.rv_currency);
        rvModel = view.findViewById(R.id.rv_model);
        tvTitle = view.findViewById(R.id.tv_title);
        btnReturn = view.findViewById(R.id.btn_return);
    }

    public void setDataList(String title, List<MeetingInfo> currency, List<MeetingInfo> model, BaseQuickAdapter.OnItemClickListener listener) {
        tvTitle.setText(title + "议题");
        rvCurrency.setListData(currency, listener);
        rvModel.setListData(model, listener);
    }

    public void setBackListener(OnClickListener l) {
        if (btnReturn != null) {
            btnReturn.setOnClickListener(l);
        }
    }
}
