package com.hckj.yddxst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.NewsInfo2;
import com.hckj.yddxst.widget.PointDividerView;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends BaseAdapter {
    private Context context;

    public List<NewsInfo2> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsInfo2> newsList) {
        this.newsList = newsList;
    }

    private List<NewsInfo2> newsList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;
    static class ViewHolder {
        public TextView tvNewsTime, newsContent,newsTitle;
        public PointDividerView tvTopLine;
        private TextView tvDot;
        private ImageView NewsImg;
    }
    public NewsListAdapter(Context context, List<NewsInfo2> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public NewsInfo2 getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final NewsInfo2 newsInfo2 = getItem(position);
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
            holder.tvNewsTime = (TextView) convertView.findViewById(R.id.news_time);
            holder.newsContent = (TextView) convertView.findViewById(R.id.news_content);
            holder.newsTitle = (TextView) convertView.findViewById(R.id.news_title);
            holder.tvTopLine = (PointDividerView) convertView.findViewById(R.id.tvTopLine);
            holder.tvDot = (TextView) convertView.findViewById(R.id.tvDot);
            holder.NewsImg = (ImageView) convertView.findViewById(R.id.news_img);
            convertView.setTag(holder);
        }

        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
            holder.tvTopLine.setVisibility(View.INVISIBLE);
            holder.NewsImg.setVisibility(View.GONE);
            // 字体颜色加深
//            holder.tvAcceptTime.setTextColor(0xff555555);
//            holder.tvAcceptStation.setTextColor(0xff555555);
//            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            holder.tvTopLine.setVisibility(View.VISIBLE);
            holder.NewsImg.setVisibility(View.VISIBLE);
            holder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }
        if(!newsInfo2.getImgs_url().equals(null)){
            holder.NewsImg.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(newsInfo2.getImgs_url())
                    .into(holder.NewsImg);
        }else {
            holder.NewsImg.setVisibility(View.INVISIBLE);
        }
        holder.tvNewsTime.setText(newsInfo2.getContent_day());
        holder.newsContent.setText(newsInfo2.getDesc());
        holder.newsTitle.setText(newsInfo2.getTitle());
        return convertView;
    }

    @Override
    public int getItemViewType(int id) {
        if (id == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }


}