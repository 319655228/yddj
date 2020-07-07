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
import com.hckj.yddxst.bean.MeetingInfo;

import java.util.ArrayList;
import java.util.List;

public class MeetingMultiSelectLayout extends LinearLayout implements View.OnClickListener {
    private RelativeLayout layoutOuter;
    private LinearLayout layoutInner;
    private RecyclerView rvOuter;
    private RecyclerView rvInner;
    private ImageView btnPrevious;
    private ImageView btnNext;
    private ImageView btnReturn;
    private TextView tvInnerTitle;
    private MultiSelectListener mSelectListener;
    private MeetingOuterAdapter mOutAdapter;
    private MeetingInnerAdapter mInnerAdapter;
    private List<MeetingClassifyInfo> mOutList = new ArrayList<>();
    private List<MeetingInfo> mInnerList = new ArrayList<>();

    public MeetingMultiSelectLayout(Context context) {
        super(context);
        initView(context);
    }

    public MeetingMultiSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MeetingMultiSelectLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_multi_select, this);
        layoutOuter = view.findViewById(R.id.layout_outer);
        layoutInner = view.findViewById(R.id.layout_inner);
        rvOuter = view.findViewById(R.id.rv_outer);
        rvInner = view.findViewById(R.id.rv_inner);
        btnPrevious = view.findViewById(R.id.btn_previous);
        btnNext = view.findViewById(R.id.btn_next);
        btnReturn = view.findViewById(R.id.btn_return);
        tvInnerTitle = view.findViewById(R.id.tv_inner_title);

        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        layoutOuter.setVisibility(VISIBLE);
        layoutInner.setVisibility(GONE);

        rvOuter.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvInner.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mOutAdapter = new MeetingOuterAdapter(R.layout.item_doc_list, mOutList);
        mInnerAdapter = new MeetingInnerAdapter(R.layout.item_doc_classify_list, mInnerList);

        rvOuter.setAdapter(mOutAdapter);
        rvOuter.addItemDecoration(new SpacesItemDecoration(45));
        rvInner.setAdapter(mInnerAdapter);


        mOutAdapter.setOnItemClickListener((adapter, view1, position) -> {
            MeetingClassifyInfo item = (MeetingClassifyInfo) adapter.getItem(position);
            tvInnerTitle.setText(item.getName() + "");
            if (mSelectListener != null) {
                mSelectListener.onMeetingClassSelect(item);
            }

            mInnerAdapter.notifyDataSetChanged();
            layoutInner.setVisibility(VISIBLE);
            layoutOuter.setVisibility(GONE);
        });

        mInnerAdapter.setOnItemClickListener((adapter, view12, position) -> {
            MeetingInfo item = (MeetingInfo) adapter.getItem(position);
            if (mSelectListener != null) {
                mSelectListener.onMeetingSelect(item);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous:
                rvOuter.smoothScrollBy(0, -1006);
                break;
            case R.id.btn_next:
                rvOuter.smoothScrollBy(0, 1006);
                break;
            case R.id.btn_return:
                if (layoutInner.getVisibility() == VISIBLE) {
                    layoutOuter.setVisibility(VISIBLE);
                    layoutInner.setVisibility(GONE);
                } else {
                    if (mSelectListener != null) {
                        mSelectListener.onBack();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void setOnSelectListener(MultiSelectListener l) {
        this.mSelectListener = l;
    }

    public void setOuterData(List<MeetingClassifyInfo> list) {
        mOutList.clear();
        if (list != null) {
            mOutList.addAll(list);
        }
        mOutAdapter.notifyDataSetChanged();
        layoutOuter.setVisibility(VISIBLE);
        layoutInner.setVisibility(GONE);
    }

    public void setInnerData(List<MeetingInfo> list) {
        mInnerList.clear();
        if (list != null) {
            mInnerList.addAll(list);
        }
        mInnerAdapter.notifyDataSetChanged();
        ;
        layoutOuter.setVisibility(GONE);
        layoutInner.setVisibility(VISIBLE);
    }

    private class MeetingOuterAdapter extends BaseQuickAdapter<MeetingClassifyInfo, BaseViewHolder> {
        MeetingOuterAdapter(int layoutResId, @Nullable List<MeetingClassifyInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, MeetingClassifyInfo item) {
            helper.setText(R.id.tv_title, item.getName() + "");
        }
    }


    private class MeetingInnerAdapter extends BaseQuickAdapter<MeetingInfo, BaseViewHolder> {
        MeetingInnerAdapter(int layoutResId, @Nullable List<MeetingInfo> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, MeetingInfo item) {
            helper.setText(R.id.tv_title, item.getTitle() + "");
        }
    }

    public interface MultiSelectListener {
        void onMeetingClassSelect(MeetingClassifyInfo info);

        void onMeetingSelect(MeetingInfo info);

        void onBack();
    }
}
