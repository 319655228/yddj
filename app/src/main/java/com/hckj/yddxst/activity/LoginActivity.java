package com.hckj.yddxst.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.cameraview.CameraView;
import com.hckj.yddxst.BuildConfig;
import com.hckj.yddxst.Constant;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.bean.SearchInfo;
import com.hckj.yddxst.bean.UserInfo;
import com.hckj.yddxst.net.BaseResponse;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.utils.FileIOUtils;
import com.hckj.yddxst.widget.EmptyControlVideo;
import com.rxjava.rxlife.RxLife;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.camera_view)
    CameraView cameraView;
    @BindView(R.id.layout_camera)
    RelativeLayout layoutCamera;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.layout_mark)
    LinearLayout layoutMark;
    @BindView(R.id.tv_user_info)
    TextView tvUserInfo;
    @BindView(R.id.iv_avator)
    ImageView ivAvator;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;
    @BindView(R.id.layout_login_info)
    LinearLayout layoutLoginInfo;
    @BindView(R.id.video_player)
    EmptyControlVideo videoPlayer;

    // Camera Callback
    private CameraView.Callback mCallback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            super.onCameraOpened(cameraView);
            if (cameraView != null) {
                cameraView.setAutoFocus(true);
            }
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.e(TAG, "onCameraClosed: ");
            super.onCameraClosed(cameraView);
        }

        @Override
        public void onPictureTaken(CameraView cameraView, byte[] data) {
            super.onPictureTaken(cameraView, data);
            handleCameraData(data);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        try {
            if (cameraView != null) {
                cameraView.addCallback(mCallback);
                cameraView.start();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        videoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                showAlertDialogWithClose("设备异常，请退出重试");
            }
        });
        videoPlayer.start("android.resource://"+ BuildConfig.APPLICATION_ID+"/" + R.raw.face_detect);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
        // 三秒后进行拍照处理
        Observable.timer(3, TimeUnit.SECONDS)
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(aLong -> takePicture(), throwable -> {
                    Toast.makeText(this, "设备异常退出", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "accept: " + throwable.getMessage());
                    finish();
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        Log.e(TAG, "Question Destroy");
    }

    /**
     * 进行拍照处理
     */
    private void takePicture() {
        if (cameraView != null && cameraView.isCameraOpened()) {
            cameraView.takePicture();
        }
    }

    /**
     * 确认按钮点击后
     */
    @OnClick(R.id.btn_confirm)
    public void onConfirmBtnClick() {
        finish();
    }


    /**
     * 处理相机数据，生成文件->压缩->上传
     *
     * @param data 相机数据
     */
    private void handleCameraData(byte[] data) {
        Observable.just(data)
                .map(bytes -> {
                    String path = Constant.FILE_DIR + new Date().getTime() + ".jpg";
                    File file = new File(path);
                    FileIOUtils.writeFileFromBytesByStream(file, data);
                    return Luban.with(LoginActivity.this)
                            .load(file)
                            .setTargetDir(Constant.FILE_DIR)
                            .get()
                            .get(0);
                })
                .flatMap((Function<File, ObservableSource<BaseResponse<SearchInfo>>>) compressFile -> {
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), compressFile);
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("image", compressFile.getName(), requestFile);
                    return request().searchFace(body);
                })
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<SearchInfo>(LoginActivity.this) {
                    @Override
                    public void onSuccess(SearchInfo res) {
                        Log.e(TAG, "onSuccess: " + res.toString());
                        showUserInfoView(res.getUserInfo());
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        Log.e(TAG, "onFailure: " + err);
                        showAlertDialogWithClose(err);
                    }

                    @Override
                    public void onFailureSpecial(SearchInfo errResp, String err) {
                        Log.e(TAG, "onFailureSpecial: " + err);
                        showQrcode(errResp);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        // 清空照片临时位置
                        FileIOUtils.deleteDir(Constant.FILE_DIR);
                    }
                });
    }

    /**
     * 携带登录key 每三秒循环判断是否登录扫码注册成功
     *
     * @param key login_key
     */
    private void checkLogin(String key) {
        Observable.interval(3, TimeUnit.SECONDS)
                .flatMap(aLong -> request().faceLogin(key))
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(resp -> {
                    if (resp.getUser_id() == 1 && resp.getUserInfo() != null) {
                        showUserInfoView(resp.getUserInfo());
                    }
                }, throwable -> showAlertDialogWithClose("人脸登录异常，请重新打开进行尝试"));
    }

    /**
     * 显示二维码提供给用户进行扫描
     *
     * @param errResp 错误响应对象
     */
    private void showQrcode(SearchInfo errResp) {
        Log.e(TAG, "showQrcode: ");
        layoutMark.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(errResp.getQrcode_login_url())
                .into(ivQrcode);
        checkLogin(errResp.getLogin_key());
    }

    /**
     * 显示登录信息
     *
     * @param userInfo 用户信息对象
     */
    private void showUserInfoView(UserInfo userInfo) {
        layoutLoginInfo.setVisibility(View.VISIBLE);
        tvUserInfo.setText(userInfo.getRealname() + "同志，欢迎您！");
        Glide.with(this)
                .load(userInfo.getPhoto_url())
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .into(ivAvator);
    }

}
