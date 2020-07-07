package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hckj.yddxst.R;
import com.hckj.yddxst.adapter.TipAdapter;
import com.hckj.yddxst.bean.TipInfo;

import java.util.List;

public class TipLayout extends RelativeLayout {
    private RecyclerView rvTips;

    public TipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TipLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TipLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tip, this);
        rvTips = view.findViewById(R.id.rv_tips);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvTips.setLayoutManager(layoutManager);
        rvTips.addItemDecoration(new SpacesItemDecoration(15));
    }

    public  void setData(List<TipInfo> tipList, BaseQuickAdapter.OnItemClickListener listener) {
        TipAdapter adapter = new TipAdapter(R.layout.item_tip, tipList);
        rvTips.setAdapter(adapter);
        adapter.setOnItemClickListener(listener);
        adapter.notifyDataSetChanged();
    }
}
