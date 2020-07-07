package com.hckj.yddxst.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.utils.FileIOUtils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：更新页面
 * 作者：林明健
 * 日期：2019-08-30 10:04
 */
public class UpgradeActivity extends AppCompatActivity {
    private String TAG = "UpgradeActivity";
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_start)
    Button btnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initListener();
        initData(savedInstanceState);
    }

    protected int getLayoutId() {
        return R.layout.activity_upgrade;
    }

    protected void initListener() {
        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask task) {
                updateBtnState(task);
                Log.e(TAG, "onReceive: " + task.getSavedLength());
            }

            @Override
            public void onCompleted(DownloadTask task) {
                updateBtnState(task);
                Log.e(TAG, "onReceive: " + task.getSavedLength());
            }

            @Override
            public void onFailed(DownloadTask task, int code, String extMsg) {
                updateBtnState(task);
                Log.e(TAG, "onFailed: " + extMsg);
            }
        });
    }

    protected void initData(Bundle savedInstanceState) {
        UpgradeInfo info = Beta.getUpgradeInfo();
        if (info != null) {
            String content = "";
            content += "文件大小：" + FileIOUtils.getPrintSize(info.fileSize) + "\n";
            content += "更新时间：" + stampToDate(info.publishTime) + "\n";
            content += "更新内容：" + info.newFeature + "\n";
            tvVersion.setText(info.title + "");
            tvInfo.setText(content + "");
            Log.e(TAG, "initData: " + info.toString());
        }
    }

    @OnClick({R.id.btn_cancel, R.id.btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                Beta.unregisterDownloadListener();
                Beta.cancelDownload();
                finish();
                break;
            case R.id.btn_start:
                DownloadTask task = Beta.startDownload();
                updateBtnState(task);
                break;
        }
    }

    public void updateBtnState(DownloadTask task) {
        double persent = 0;
        if (task != null && task.getTotalLength() != 0) {
            persent = ((double) task.getSavedLength() / task.getTotalLength()) * 100;
        }

        /*根据下载任务状态设置按钮*/
        switch (task.getStatus()) {
            case DownloadTask.INIT:
            case DownloadTask.DELETED:
            case DownloadTask.FAILED: {
                btnStart.setText("开始下载");
            }
            break;
            case DownloadTask.COMPLETE: {
                btnStart.setText("安装");
                if (btnCancel.getVisibility() != View.VISIBLE) {
                    btnCancel.setVisibility(View.VISIBLE);
                }
            }
            break;
            case DownloadTask.DOWNLOADING: {
                btnStart.setText(String.format("暂停 (%.2f %%)", persent));
                if (btnCancel.getVisibility() == View.VISIBLE) {
                    btnCancel.setVisibility(View.GONE);
                }
            }
            break;
            case DownloadTask.PAUSED: {
                btnStart.setText("继续下载");
                btnCancel.setVisibility(View.VISIBLE);
            }
            break;
        }
    }

    public static String stampToDate(long s) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(s);
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
