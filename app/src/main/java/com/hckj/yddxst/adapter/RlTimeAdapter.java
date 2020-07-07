package com.hckj.yddxst.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.SxkDetailActivity;
import com.hckj.yddxst.activity.SxkSecondActivity;
import com.hckj.yddxst.bean.NewsInfo2;
import com.hckj.yddxst.bean.RlTimeInfo;

import java.util.ArrayList;
import java.util.List;

public class RlTimeAdapter extends BaseAdapter {
    private Context context;
    private List<RlTimeInfo> newsList = new ArrayList<>(1);
    private int mSelect;   //选中项
    static class ViewHolder {
        public TextView time;
    }
    public RlTimeAdapter(Context context, List<RlTimeInfo> newsList) {
        this.context = context;
        this.newsList = newsList;
    }
    public List<RlTimeInfo> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<RlTimeInfo> newsList) {
        this.newsList = newsList;
    }
    public void appendNewsList(List<RlTimeInfo> newsList) {
        this.newsList.addAll( newsList );
    }
    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public RlTimeInfo getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final RlTimeInfo rlTimeInfo = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_rl_time, parent, false);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }
        if (mSelect == position) {
            holder.time.setBackgroundResource(R.drawable.item_bg1);
        } else {
            holder.time.setBackgroundColor(Color.parseColor("#00000000"));
        }
        holder.time.setText(rlTimeInfo.getTitle());
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