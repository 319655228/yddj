package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.MeetingClassifyInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MeetingInfSelectLayout extends LinearLayout implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView btnPrevious;
    private ImageView btnNext;
    private RecyclerView rvDir;
    private RecyclerView rvMeeting;
    private RelativeLayout layoutDir;
    private LinearLayout layoutMeeting;
    private ImageView btnReturn;

    private LinkedList<List<MeetingClassifyInfo>> classifyList = new LinkedList<>();
    private MeetingInfSelectListener mListener;
    private MeetingClassifyAdapter mDirAdapter;
    private MeetingClassifyAdapter mMeetingAdapter;
    private List<MeetingClassifyInfo> mDirList = new ArrayList<>();
    private List<MeetingClassifyInfo> mMeetingList = new ArrayList<>();

    public MeetingInfSelectLayout(Context context) {
        super(context);
        init(context);
    }

    public MeetingInfSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeetingInfSelectLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_inf_select, this);
        tvTitle = view.findViewById(R.id.tv_title);
        btnPrevious = view.findViewById(R.id.btn_previous);
        btnNext = view.findViewById(R.id.btn_next);
        rvDir = view.findViewById(R.id.rv_dir);
        rvMeeting = view.findViewById(R.id.rv_meeting);
        layoutDir = view.findViewById(R.id.layout_dir);
        layoutMeeting = view.findViewById(R.id.layout_meeting);
        btnReturn = view.findViewById(R.id.btn_return);

        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        mDirAdapter = new MeetingClassifyAdapter(R.layout.item_doc_list, mDirList);
        mMeetingAdapter = new MeetingClassifyAdapter(R.layout.item_doc_classify_list, mMeetingList);

        rvDir.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvMeeting.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        rvDir.setAdapter(mDirAdapter);
        rvDir.addItemDecoration(new SpacesItemDecoration(45));
        rvMeeting.setAdapter(mMeetingAdapter);

        mDirAdapter.setOnItemClickListener((adapter, view1, position) -> {
            MeetingClassifyInfo item = (MeetingClassifyInfo) adapter.getItem(position);
            if (item == null) {
                return;
            }
            tvTitle.setText(tvTitle.getText().toString() + ">" + item.getName());
            if (mListener != null) {
                mListener.onClassifySelect(item);
            }
        });

        mMeetingAdapter.setOnItemClickListener((adapter, view12, position) -> {
            MeetingClassifyInfo item = (MeetingClassifyInfo) adapter.getItem(position);
            if (item == null) {
                return;
            }
            if (mListener != null) {
                mListener.onMeetingSelect(item);
            }
        });
        layoutDir.setVisibility(GONE);
        layoutMeeting.setVisibility(GONE);
    }

    public void setSelectListener(MeetingInfSelectListener l) {
        this.mListener = l;
    }

    public void setList(List<MeetingClassifyInfo> list, String type) {
        if ("meeting".equals(type)) {
            if (mDirList != null && !mDirList.isEmpty()) {
                classifyList.add(new LinkedList<>(mDirList));
            }
            mMeetingList.clear();
            if (list != null) {
                mMeetingList.addAll(list);
            }
            mMeetingAdapter.notifyDataSetChanged();
            layoutMeeting.setVisibility(VISIBLE);
            layoutDir.setVisibility(GONE);
        } else {
            classifyList.add(new LinkedList<>(list));
            mDirList.clear();
            if (list != null) {
                mDirList.addAll(list);
            }
            mDirAdapter.notifyDataSetChanged();
            layoutMeeting.setVisibility(GONE);
            layoutDir.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous:
                rvDir.smoothScrollBy(0, -300);
                break;
            case R.id.btn_next:
                rvDir.smoothScrollBy(0, 300);
                break;
            case R.id.btn_return:
                if (classifyList.size() <= 1) {
                    mListener.onBackBtnClick();
                } else {
                    List<MeetingClassifyInfo> popList = classifyList.pop();
                    mDirList.clear();
                    mDirList.addAll(popList);
                    mDirAdapter.notifyDataSetChanged();
                }
                String title = tvTitle.getText().toString();
                int lastIndex = title.lastIndexOf(">");
                title = lastIndex >= 0 ? title.substring(0, lastIndex) : "";
                tvTitle.setText(title);
                if (layoutMeeting.getVisibility() == VISIBLE) {
                    layoutMeeting.setVisibility(GONE);
                    layoutDir.setVisibility(VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private class MeetingClassifyAdapter extends BaseQuickAdapter<MeetingClassifyInfo, BaseViewHolder> {

        MeetingClassifyAdapter(int layoutResId, @Nullable List<MeetingClassifyInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, MeetingClassifyInfo item) {
            helper.setText(R.id.tv_title, item.getName() + "");
        }
    }


    public interface MeetingInfSelectListener {
        void onMeetingSelect(MeetingClassifyInfo info);

        void onClassifySelect(MeetingClassifyInfo info);

        void onBackBtnClick();
    }

}
