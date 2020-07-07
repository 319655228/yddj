package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.adapter.MeetingSpeakAdapter;
import com.hckj.yddxst.bean.SocketInfo;

import java.util.ArrayList;
import java.util.List;

public class MeetingSpeakView extends LinearLayout {
    RecyclerView lvData;
    ImageView btnPre;
    ImageView btnNext;
    private List<SocketInfo.Content> mList = new ArrayList<>();
    private MeetingSpeakAdapter mAdapter;

    public MeetingSpeakView(Context context) {
        super(context);
        init(context);
    }

    public MeetingSpeakView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeetingSpeakView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_speak, this);
        lvData = view.findViewById(R.id.lv_data);
        btnPre = view.findViewById(R.id.btn_pre);
        btnNext = view.findViewById(R.id.btn_next);

        mAdapter = new MeetingSpeakAdapter(R.layout.item_meeting_speak, mList);
        mAdapter.openLoadAnimation();
        lvData.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        // layoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, Integer.MIN_VALUE);
        lvData.setLayoutManager(layoutManager);

        mAdapter = new MeetingSpeakAdapter(R.layout.item_meeting_speak, mList);
        mAdapter.openLoadAnimation();
        lvData.setAdapter(mAdapter);

        btnPre.setOnClickListener(v -> {
            lvData.smoothScrollBy(0, -50);
        });
        btnNext.setOnClickListener(v -> {
            lvData.smoothScrollBy(0, 50);
        });
    }

    public void setListData(SocketInfo.Content info) {
        if (info == null) {
            mList.clear();
        } else {
            mList.add(info);
        }
        mAdapter.notifyDataSetChanged();
        int position = mAdapter.getItemCount() - 1;
        if (position >= 0) {
            lvData.smoothScrollToPosition(position);
        }
    }
}
