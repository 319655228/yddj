package com.hckj.yddxst.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.DeviceUtils;
import com.hckj.yddxst.Constant;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.net.BaseResponse;
import com.hckj.yddxst.net.RxException;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.utils.FileIOUtils;
import com.hckj.yddxst.utils.SpUtil;
import com.hckj.yddxst.utils.StaUnityUtils;
import com.rxjava.rxlife.RxLife;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 描述：闪屏页，负责权限检查等操作
 * 作者：林明健
 * 日期：2019-08-22 11:29
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.et_device_num)
    EditText etDeviceNum;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.layout_device_num)
    LinearLayout layoutDeviceNum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        requestPermission();
    }

    /**
     * 描述：请求权限
     * 作者：林明健
     * 日期：2019-08-22 11:43
     */
    private void requestPermission() {
        new RxPermissions(this)
                .request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_NOTIFICATION_POLICY
                )
                .as(RxLife.as(this))
                .subscribe(granted -> {
                    // 授权成功
                    if (granted) {
                        String deviceNum = SpUtil.get(this, SpUtil.DEVICE_NUM, "");
                        if (TextUtils.isEmpty(deviceNum)) {
                            layoutDeviceNum.setVisibility(View.VISIBLE);
                            showAlertDialog("设备号不存在，请重新输入");
                        } else {
                            getDeviceAuth(deviceNum);
                        }
                    } else {
                        showAlertDialogWithClose("请授予设备必要的权限，否则无法运行本程序");
                    }
                }, err -> {
                    showAlertDialogWithClose("请授予设备必要的权限，否则无法运行本程序");
                });
    }

    /**
     * 确认按钮点击
     */
    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        String deviceNum = etDeviceNum.getText().toString().trim();
        if (TextUtils.isEmpty(deviceNum)) {
            showToast("请输入正确的设备编号");
            return;
        }
        getDeviceAuth(deviceNum);
    }

    /**
     * 网络校验时候是正确的设备号
     *
     * @param deviceNum 设备号
     */
    private void getDeviceAuth(String deviceNum) {
        // 获取设备唯一识别码
        String deviceId = DeviceUtils.getUniqueDeviceId();
        Log.e(TAG, "设备唯一码: " + deviceId);
        Observable<File> obsFile = request().getCertUrl(deviceId)
                .flatMap((Function<BaseResponse<String>, ObservableSource<ResponseBody>>) certResp -> {
                    if (!"success".equals(certResp.getLevel())) {
                        throw new RxException(certResp.getMessage() + "");
                    }
                    return request().getCertFile(certResp.getData());
                })
                .map(body -> {
                    if (!"application/octet-stream".equals(body.contentType().toString())) {
                        throw new RxException("证书文件下载错误");
                    }
                    byte[] bytes = body.bytes();
                    File bundleFile = new File(Constant.BUNDLE_PATH);
                    if (bundleFile.exists()) {
                        bundleFile.delete();
                    }
                    FileIOUtils.writeFileFromBytesByStream(bundleFile, bytes);
                    return bundleFile;
                });
        Observable<BaseResponse<String>> obsAuth = request().getDeviceAuth(deviceNum);
        Observable.zip(obsAuth, obsFile, (authResp, file) -> {
            if (!"true".equals(authResp.getData())) {
                throw new RxException(authResp.getMessage());
            }
            return file;
        })
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showDialog("加载证书中");
                    }

                    @Override
                    public void onNext(File file) {
                        SpUtil.save(SplashActivity.this, SpUtil.DEVICE_NUM, deviceNum);
                        handleInit();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                        showAlertDialogWithClose(RxHelper.exceptionHandler(e));
                    }

                    @Override
                    public void onComplete() {
                        hideDialog();
                    }
                });

        /*
        // 获取设备唯一识别码
        String deviceId = DeviceUtils.getUniqueDeviceId();
        Log.e(TAG, "设备唯一码: " + deviceId);
        request().getDeviceAuth(deviceNum)
                .flatMap((Function<BaseResponse<String>, ObservableSource<BaseResponse<String>>>) authResp -> {
                    if (!"true".equals(authResp.getData())) {
                        throw new RxException(authResp.getMessage());
                    }
                    return request().getCertUrl(deviceId, deviceNum);
                })
                .flatMap((Function<BaseResponse<String>, ObservableSource<ResponseBody>>) certResp -> {
                    if (!"success".equals(certResp.getLevel())) {
                        throw new RxException(certResp.getMessage() + "");
                    }
                    return request().getCertFile(certResp.getData());
                })
                .map(body -> {
                    if (!"application/octet-stream".equals(body.contentType().toString())) {
                        throw new RxException("证书文件下载错误");
                    }
                    byte[] bytes = body.bytes();
                    File bundleFile = new File(Constant.BUNDLE_PATH);
                    if (bundleFile.exists()) {
                        bundleFile.delete();
                    }
                    FileIOUtils.writeFileFromBytesByStream(bundleFile, bytes);
                    return bundleFile;
                })
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showDialog("加载证书中");
                    }

                    @Override
                    public void onNext(File file) {
                        SpUtil.save(SplashActivity.this, SpUtil.DEVICE_NUM, deviceNum);
                        handleInit();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                        String errMsg = RxHelper.exceptionHandler(e);
                        if (e instanceof RxException) {
                            layoutDeviceNum.setVisibility(View.VISIBLE);
                            showAlertDialog(errMsg);
                        } else {
                            showAlertDialogWithClose(errMsg);
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideDialog();
                    }
                });
         */
    }

    /**
     * 描述: 初始化STA SDK，清空缓存文件夹 <br>
     * 日期: 2019-10-31 00:06 <br>
     * 作者: 林明健 <br>
     */
    private void handleInit() {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            // 相芯科技SDK初始化
            StaUnityUtils.getInstance().init(getApplicationContext());
            StaUnityUtils.getInstance().getFUStaEngine().auth();
            FileIOUtils.deleteDir(Constant.FILE_DIR);
            emitter.onNext(true);
            emitter.onComplete();
        }).compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showDialog("初始化中");
                    }

                    @Override
                    public void onNext(Boolean b) {
                        Log.e(TAG, "onNext: ");
                        startActivity(IndexActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        e.printStackTrace();
                        showAlertDialogWithClose("初始化异常，请退出重试");
                    }

                    @Override
                    public void onComplete() {
                        hideDialog();
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }
}
