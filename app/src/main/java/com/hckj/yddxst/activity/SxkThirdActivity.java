package com.hckj.yddxst.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.adapter.SxkListAdapter;
import com.hckj.yddxst.adapter.SxkListThirdAdapter;
import com.hckj.yddxst.bean.NewsInfo2;
import com.hckj.yddxst.task.SxkListTask;
import com.hckj.yddxst.utils.ListViewUtil;
import com.hckj.yddxst.utils.ToastUtil;
import com.hckj.yddxst.widget.YddxProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class SxkThirdActivity extends BaseActivity implements SxkListTask.SxkCallback {
    @BindView(R.id.sxk_list)
    ListViewUtil sxkList;
    private SxkListThirdAdapter sxcListAdapter;
    private List<NewsInfo2> newsInfoList = new ArrayList<>();
    private YddxProgressDialog mDialog;
    private int page;
    private int total;
    private String id;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sxk_third;
    }

    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, SXKActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        this.id = this.getIntent().getStringExtra("id");
        sxcListAdapter = new SxkListThirdAdapter(this, newsInfoList);
        sxkList.setEmptyView(findViewById(R.id.myText));
        sxkList.setAdapter(sxcListAdapter);
        List list=new ArrayList<>();
        list.add(0,"1");
        list.add(1,"10");
        list.add(2,"sixiangku");
        list.add(3,id);
        list.add(4, null);
        new SxkListTask(SxkThirdActivity.this,this,false).execute(list);
        showDialog2("加载中");
        sxkList.setOnLoadMoreListener(new ListViewUtil.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (sxcListAdapter.getCount() == total) {
                            ToastUtil.showShortToast(SxkThirdActivity.this, "没有更多了！");
                            sxkList.finishLoadMore();
                        } else {
                            List list=new ArrayList<>();
                            String.valueOf(page++);
                            list.add(0,String.valueOf(page++));
                            list.add(1,"10");
                            list.add(2,"sixiangku");
                            list.add(3,id);
                            list.add(4, null);
                            new SxkListTask(SxkThirdActivity.this,SxkThirdActivity.this,true).execute(list);
                            sxcListAdapter.notifyDataSetChanged();
                            sxkList.finishLoadMore();
                        }
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void sxkFailure() {
        if(mDialog != null){
            mDialog.dismiss();
        }
        Toast.makeText(this, "服务异常！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sxkSuccess(List<NewsInfo2> listBeans, String total, String page, Boolean flag) {
        this.total = Integer.parseInt(total);
        this.page = Integer.parseInt(page);
        if(mDialog != null){
            mDialog.dismiss();
        }
        if (flag){
            sxcListAdapter.appendNewsList(listBeans);
            sxcListAdapter.notifyDataSetChanged();
        }else {
            sxcListAdapter.setNewsList(listBeans);
            sxcListAdapter.notifyDataSetChanged();
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


    public void showDialog2(String message) {
        if (mDialog == null) {
            mDialog = new YddxProgressDialog(this);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(message);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }
    }



}