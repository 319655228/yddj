package com.hckj.yddxst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.RlContextInfo;
import com.hckj.yddxst.bean.RlTimeInfo;

import java.util.ArrayList;
import java.util.List;

public class RlContextAdapter extends BaseAdapter {
    private Context context;
    private List<RlContextInfo> newsList = new ArrayList<>(1);
    static class ViewHolder {
        public TextView day;
        public TextView title;
        public TextView desc;
    }
    public RlContextAdapter(Context context, List<RlContextInfo> newsList) {
        this.context = context;
        this.newsList = newsList;
    }
    public List<RlContextInfo> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<RlContextInfo> newsList) {
        this.newsList = newsList;
    }


    public void appendNewsList(List<RlContextInfo> newsList) {
        this.newsList.addAll( newsList );
    }
    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public RlContextInfo getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final RlContextInfo rlContextInfo = getItem(position);

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_rl_content, parent, false);
            holder.day = (TextView) convertView.findViewById(R.id.tv_day);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.desc = (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        }
        holder.day.setText(rlContextInfo.getDay());
        holder.title.setText(rlContextInfo.getTitle());
        holder.desc.setText(rlContextInfo.getDesc());
        // 查看按钮事件
//        holder.time.setOnClickListener( new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {

//                LinearLayout linearLayout = (LinearLayout)v.getParent();
//                TextView tv = (TextView)linearLayout.findViewById( R.id.ids );
//                String id =  tv.getText().toString() ;
//                TextView type = (TextView)linearLayout.findViewById( R.id.type );
//                String typeV =  type.getText().toString() ;
//                if(typeV.equals("content")){
//                    Intent intent = new Intent( context, SxkDetailActivity.class );
//                    intent.putExtra( "id",id );
//                    context.startActivity( intent );
//                }else if(typeV.equals("catalog")){
//                    Intent intent = new Intent( context, SxkSecondActivity.class );
//                    intent.putExtra( "id",id );
//                    context.startActivity( intent );
//                }

//            }
//        } );
        return convertView;
    }

}