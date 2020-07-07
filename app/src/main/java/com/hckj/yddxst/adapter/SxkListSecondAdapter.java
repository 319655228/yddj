package com.hckj.yddxst.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.SxkDetailActivity;
import com.hckj.yddxst.activity.SxkThirdActivity;
import com.hckj.yddxst.bean.NewsInfo2;

import java.util.ArrayList;
import java.util.List;

public class SxkListSecondAdapter extends BaseAdapter {
    private Context context;
    private List<NewsInfo2> newsList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;
    static class ViewHolder {
        public TextView newsTitle;
        public TextView ids;
        public TextView type;
//        private ImageView NewsImg;
        private LinearLayout SxcItem;
    }
    public SxkListSecondAdapter(Context context, List<NewsInfo2> newsList) {
        this.context = context;
        this.newsList = newsList;
    }
    public List<NewsInfo2> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsInfo2> newsList) {
        this.newsList = newsList;
    }


    public void appendNewsList(List<NewsInfo2> newsList) {
        this.newsList.addAll( newsList );
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_sxk_second, parent, false);
            holder.newsTitle = (TextView) convertView.findViewById(R.id.news_title);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.ids = (TextView) convertView.findViewById(R.id.ids);
//            holder.NewsImg = (ImageView) convertView.findViewById(R.id.news_img);
            holder.SxcItem = (LinearLayout) convertView.findViewById(R.id.sxk_item);
            convertView.setTag(holder);
        }
        holder.ids.setText(newsInfo2.getId());
        holder.type.setText(newsInfo2.getType());
//        if(!newsInfo2.getImgs_url().equals(null)){
////            holder.NewsImg.setVisibility(View.VISIBLE);
////            Glide.with(context)
////                    .load(newsInfo2.getImgs_url())
////                    .into(holder.NewsImg);
////        }else {
////            holder.NewsImg.setVisibility(View.INVISIBLE);
////        }
        holder.newsTitle.setText(newsInfo2.getTitle());
        // 查看按钮事件
        holder.SxcItem.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                LinearLayout linearLayout = (LinearLayout)v.getParent();
                TextView tv = (TextView)linearLayout.findViewById( R.id.ids );
                String id =  tv.getText().toString() ;
                TextView type = (TextView)linearLayout.findViewById( R.id.type );
                String typeV =  type.getText().toString() ;
                if(typeV.equals("content")){
                    Intent intent = new Intent( context, SxkDetailActivity.class );
                    intent.putExtra( "id",id );
                    context.startActivity( intent );
                }else if(typeV.equals("catalog")){
                    Intent intent2 = new Intent( context, SxkThirdActivity.class );
                    intent2.putExtra( "id",id );
                    context.startActivity( intent2 );
                }
            }
        } );
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