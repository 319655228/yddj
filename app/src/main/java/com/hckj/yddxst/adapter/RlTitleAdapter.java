package com.hckj.yddxst.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.RlInfo;
import com.hckj.yddxst.bean.RlTimeInfo;

import java.util.ArrayList;
import java.util.List;

public class RlTitleAdapter extends BaseAdapter {
    private Context context;
    private int mSelect;   //选中项
    private List<RlInfo> newsList = new ArrayList<>(1);
    static class ViewHolder {
        public TextView type_name;
        public TextView count;
        public LinearLayout ll_title;
    }
    public RlTitleAdapter(Context context, List<RlInfo> newsList) {
        this.context = context;
        this.newsList = newsList;
    }
    public List<RlInfo> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<RlInfo> newsList) {
        this.newsList = newsList;
    }


    public void appendNewsList(List<RlInfo> newsList) {
        this.newsList.addAll( newsList );
    }
    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public RlInfo getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final RlInfo rlInfo = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_rl_title, parent, false);
            holder.type_name = (TextView) convertView.findViewById(R.id.tv_title);
            holder.count = (TextView) convertView.findViewById(R.id.tv_number);
            holder.ll_title = (LinearLayout) convertView.findViewById(R.id.ll_title);
            convertView.setTag(holder);
        }
        if (mSelect == position) {
            holder.ll_title.setBackgroundResource(R.drawable.item_bg2);
        } else {
            holder.ll_title.setBackgroundColor(Color.parseColor("#00000000"));
        }
        holder.type_name.setText(rlInfo.getType_name());
        holder.count.setText(rlInfo.getCount());
        // 查看按钮事件
        return convertView;
    }
    //刷新方法
    public void changeSelected(int position) {
        if (position != mSelect) {
            mSelect = position;
            notifyDataSetChanged();
        }
    }

}