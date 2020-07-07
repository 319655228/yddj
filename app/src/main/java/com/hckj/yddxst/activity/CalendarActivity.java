package com.hckj.yddxst.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.RlContextAdapter;
import com.hckj.yddxst.adapter.RlTimeAdapter;
import com.hckj.yddxst.adapter.RlTitleAdapter;
import com.hckj.yddxst.bean.RlContextInfo;
import com.hckj.yddxst.bean.RlInfo;
import com.hckj.yddxst.bean.RlTimeInfo;
import com.hckj.yddxst.task.CalendarListTask;
import com.hckj.yddxst.task.SxkListTask;
import com.hckj.yddxst.utils.EmptyUtils;
import com.hckj.yddxst.utils.ListViewUtil;
import com.hckj.yddxst.utils.ListViewUtil2;
import com.hckj.yddxst.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarActivity extends BaseActivity {

    @BindView(R.id.title_list)
    ListView titleList;
    @BindView(R.id.context_list)
    ListView contextList;
    @BindView(R.id.time_list)
    ListViewUtil2 timeList;
    private RlTimeAdapter rlTimeAdapter;
    private RlTitleAdapter rlTitleAdapter;
    private RlContextAdapter rlContextAdapter;
    private List<RlTimeInfo> rlTimeInfoList = new ArrayList<>();
    private List<RlInfo> rlInfoList = new ArrayList<>();
    private List<RlContextInfo> rlContextInfoList = new ArrayList<>();
    private int timePosition = 0;
    private int titlePosition = 0;
    private int page;
    private int total;

    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, CalendarActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        rlTimeAdapter = new RlTimeAdapter(this, rlTimeInfoList);
        timeList.setAdapter(rlTimeAdapter);
        rlTitleAdapter = new RlTitleAdapter(this, rlInfoList);
        titleList.setAdapter(rlTitleAdapter);
        rlContextAdapter = new RlContextAdapter(this, rlContextInfoList);
        contextList.setAdapter(rlContextAdapter);
        showDialog("加载中");
        List list=new ArrayList<>();
        list.add(0,"1");
        list.add(1,"11");
        new CalendarListTask(this, false).execute(list);
        timeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选模式
        timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long l) {
                rlTimeAdapter.changeSelected(position);
                titleList.performItemClick(titleList.getChildAt(0),0,titleList.getItemIdAtPosition(0));
                RlTimeInfo rlTimeInfo = rlTimeInfoList.get(position);
                timePosition = position;
                rlInfoList = rlTimeInfo.getRlInfoList();
                rlTitleAdapter.setNewsList(rlInfoList);
                rlTitleAdapter.notifyDataSetChanged();

                initThirdListView(position);

            }
        });
        titleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long l) {
//                RlInfo rlInfo = rlInfoList.get(position);
                rlTitleAdapter.changeSelected(0);
                rlTitleAdapter.changeSelected(position);
                titlePosition = position;
                rlContextInfoList = rlTimeInfoList.get(timePosition).getRlInfoList().get(titlePosition).getRlContextInfoArrayList();
                rlContextAdapter.setNewsList(rlContextInfoList);
                rlContextAdapter.notifyDataSetChanged();
            }
        });
        contextList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long l) {
                String web_url = rlTimeInfoList.get(timePosition).getRlInfoList().get(titlePosition).getRlContextInfoArrayList().get(position).getWeb_url();
                Intent intent = new Intent(CalendarActivity.this, RLActivity.class);
                intent.putExtra("web_url", web_url);
                startActivity(intent);
            }
        });

        timeList.setOnLoadMoreListener(new ListViewUtil2.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (rlTimeAdapter.getCount() == total) {
                            ToastUtil.showShortToast(CalendarActivity.this, "没有更多了！");
                            timeList.finishLoadMore();
                        } else {
                            List list=new ArrayList<>();
                            String.valueOf(page++);
                            list.add(0,String.valueOf(page++));
                            list.add(1,"11");
                            showDialog("加载中");
                            new CalendarListTask(CalendarActivity.this, true).execute(list);
                            rlTimeAdapter.notifyDataSetChanged();
                            timeList.finishLoadMore();
                        }
                    }
                }, 500);
            }
        });
    }

    //初始化第一个ListView
    public void initThirdListView(int position) {
        List<RlContextInfo> list = new ArrayList<>();
        rlContextAdapter = new RlContextAdapter(this, list);
        contextList.setAdapter(rlContextAdapter);
        rlContextAdapter.setNewsList(rlTimeInfoList.get(position).getRlInfoList().get(0).getRlContextInfoArrayList());
        rlTimeAdapter.notifyDataSetChanged();
    }

    public void sxkFailure() {

    }

    public void sxkSuccess(List<RlTimeInfo> listBeans, String total, String page, boolean flag) {
        hideDialog();
        this.page= Integer.parseInt(page);
        this.total= Integer.parseInt(total);
        if (flag){
            rlTimeAdapter.appendNewsList(listBeans);
            rlTimeAdapter.notifyDataSetChanged();
        }else {
            rlTimeInfoList = listBeans;
            rlTimeAdapter.setNewsList(listBeans);
            rlTimeAdapter.notifyDataSetChanged();

            rlTitleAdapter.setNewsList(rlTimeInfoList.get(0).getRlInfoList());
            rlTimeAdapter.notifyDataSetChanged();

            rlContextAdapter.setNewsList(rlTimeInfoList.get(0).getRlInfoList().get(0).getRlContextInfoArrayList());
            rlTimeAdapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

}