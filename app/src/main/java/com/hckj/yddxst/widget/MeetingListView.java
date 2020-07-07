package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hckj.yddxst.R;
import com.hckj.yddxst.adapter.MeetingAdapter;
import com.hckj.yddxst.bean.MeetingInfo;

import java.util.ArrayList;
import java.util.List;

public class MeetingListView extends RelativeLayout {
    RecyclerView lvData;
    ImageView btnPre;
    ImageView btnNext;
    private List<MeetingInfo> mList = new ArrayList<>();
    private MeetingAdapter mAdapter;

    public MeetingListView(Context context) {
        super(context);
        init(context);
    }

    public MeetingListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeetingListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_list_view, this);
        lvData = view.findViewById(R.id.lv_data);
        btnPre = view.findViewById(R.id.btn_pre);
        btnNext = view.findViewById(R.id.btn_next);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        lvData.setLayoutManager(layoutManager);

        mAdapter = new MeetingAdapter(R.layout.item_meetings_list, mList);
        mAdapter.openLoadAnimation();
        lvData.setAdapter(mAdapter);
        lvData.addItemDecoration(new SpacesItemDecoration(45));

        btnPre.setOnClickListener(v -> {
            lvData.smoothScrollBy(0, -100);
        });
        btnNext.setOnClickListener(v -> {
            lvData.smoothScrollBy(0, 100);
        });
    }

    public void setListData(List<MeetingInfo> list, BaseQuickAdapter.OnItemClickListener listener) {
        if (list == null || list.isEmpty()) {
            mList.clear();
        } else {
            mList.clear();
            mList.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(listener);
    }
}
