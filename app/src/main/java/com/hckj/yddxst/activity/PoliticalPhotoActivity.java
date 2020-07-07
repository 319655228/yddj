package com.hckj.yddxst.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.android.cameraview.CameraView;
import com.hckj.yddxst.Constant;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.base.BaseActivity;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.task.ImageUploadTask;
import com.hckj.yddxst.utils.FileIOUtils;
import com.hckj.yddxst.widget.YddxProgressDialog;

import java.io.File;
import java.security.MessageDigest;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;

public class PoliticalPhotoActivity extends BaseActivity implements View.OnClickListener, ImageUploadTask.UploadCallback {

    @BindView(R.id.iv_preview)
    ImageView ivPreview;
    @BindView(R.id.camera_view)
    CameraView cameraView;
    @BindView(R.id.btn_photo)
    ImageView btnPhoto;
    @BindView(R.id.qr_code)
    ImageView qrCode;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_bot_circle)
    ImageView ivBotCircle;
    @BindView(R.id.layout_bottom)
    RelativeLayout layoutBottom;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    //    @BindView(R.id.bg_photo)
//    ImageView bgPhoto;
    @BindView(R.id.bg_qr_code)
    ImageView bgQrCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.btn_rephoto)
    ImageView btnRephoto;
    @BindView(R.id.btn_upload)
    ImageView btnUpload;
    @BindView(R.id.tv_failed)
    TextView tvFailed;
    private Bitmap rotateBitmap;
    private Callback mCallback;
    private String photoUrl;
    private YddxProgressDialog mDialog;


    public static void startActivity(BaseActivity activity) {
        Intent intent = new Intent(activity, PoliticalPhotoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_political_photo;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        viewInit();
        btnUpload.setOnClickListener(this);
        btnRephoto.setOnClickListener(this);
        startCamera(new Callback() {
            @Override
            public void onPictureSuccess(Bitmap bmp, String filePath) {
            }

            @Override
            public void onPictureError(String errMsg) {

            }

            @Override
            public void onPreviousClick() {
                closeCamera();
            }

            @Override
            public void onNextClick() {
                closeCamera();
            }
        });
    }

    private void viewInit() {
        btnPhoto.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        swithPhotoLayout(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photo:  // 拍照
                qrCode.setVisibility(GONE);
                if (cameraView != null && cameraView.isCameraOpened()) {
                    cameraView.takePicture();
                }
                break;
            case R.id.iv_back:  // 返回
                finish();
                break;
//            case R.id.upload:  // 重新上传
//                new ImageUploadTask(PoliticalPhotoActivity.this).execute(new File(filePathV));//上传图片
//                reUpload.setVisibility(GONE);
//                tvUplode.setText("正在为您上传留影中，请稍等...");
//                break;
            case R.id.btn_rephoto: // 重拍
                swithPhotoLayout(true);
                tvFailed.setVisibility(GONE);
                tvCode.setVisibility(GONE);
                qrCode.setVisibility(GONE);
                bgQrCode.setVisibility(GONE);
                break;
            case R.id.btn_upload:  // 上传
                if (mCallback != null) {
                    tvFailed.setVisibility(GONE);
//                    btnUpload.setVisibility(GONE);
                    showDialog("正在为您上传留影中，请稍等...");
                    mCallback.onPictureSuccess(rotateBitmap, photoUrl);
                    new ImageUploadTask(PoliticalPhotoActivity.this).execute(new File(photoUrl));//上传图片
                }
                break;
        }
    }


    public void startCamera(Callback cb) {
        this.mCallback = cb;
        try {
            if (cameraView != null) {
                cameraView.setVisibility(View.VISIBLE);
                cameraView.addCallback(new CameraView.Callback() {
                    @Override
                    public void onPictureTaken(CameraView cameraView, byte[] data) {
                        super.onPictureTaken(cameraView, data);
                        handleCameraData(data);
                    }
                });
                cameraView.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (this.mCallback != null) {
                mCallback.onPictureError("摄像头开启出错");
            }
        }
    }

    public void closeCamera() {
        try {
            cameraView.setVisibility(GONE);
            cameraView.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCameraData(byte[] data) {
        btnPhoto.setVisibility(GONE);
        Observable.just(data)
                .map(bytes -> {
                    String path = Constant.PHOTO_PATH + "political_photo.jpg";
                    File file = new File(path);
                    FileIOUtils.writeFileFromBytesByStream(file, data);
                    return path;
                })
                .compose(RxHelper.io2m())
//                .as(RxLife.as(this))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        tvTips.setVisibility(View.VISIBLE);
                        btnPhoto.setVisibility(GONE);
                    }

                    @Override
                    public void onNext(String filePath) {
                        photoUrl = filePath;
                        Log.e("拍照路径", "handleCameraData: " + photoUrl);
                        Glide.with(PoliticalPhotoActivity.this)
                                .asBitmap()
                                .load(filePath)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .transform(new RotateTransformation(0f))
                                .into(ivPreview);
                        swithPhotoLayout(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("PhotoLayout", "handleCameraData: " + e.getMessage());
                        mCallback.onPictureError("拍照出错！");
                        btnPhoto.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        tvTips.setVisibility(GONE);
                    }
                });
    }

    private void swithPhotoLayout(boolean showPhoto) {
        if (showPhoto) {
            ivPreview.setVisibility(GONE);//拍照时tv
            btnPhoto.setVisibility(View.VISIBLE);//拍照留影
            btnUpload.setVisibility(GONE);//上传
            btnRephoto.setVisibility(GONE);//重拍
        } else {
            ivPreview.setVisibility(View.VISIBLE);//拍照时tv
            btnPhoto.setVisibility(GONE);//拍照留影
            btnUpload.setVisibility(View.VISIBLE);//上传
            btnRephoto.setVisibility(View.VISIBLE);//重拍
            if (mCallback != null) {
                mCallback.onPictureSuccess(rotateBitmap, photoUrl);
            }

        }
    }

    @Override
    public void uploadFailure() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);//1秒后，取消ProgressDialog
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hideDialog();
            }
        });
        t.start();
        tvFailed.setVisibility(View.VISIBLE);
        btnUpload.setVisibility(View.VISIBLE);
        btnUpload.setImageResource(R.drawable.cxsc);
    }

    @Override
    public void uploadSucess(String rdCode) {
        hideDialog();
        btnUpload.setImageResource(R.drawable.bg_meeting_upload);
        btnUpload.setVisibility(GONE);
        tvFailed.setVisibility(GONE);
        tvCode.setVisibility(View.VISIBLE);
        qrCode.setVisibility(View.VISIBLE);
        bgQrCode.setVisibility(View.VISIBLE);
        Glide.with(PoliticalPhotoActivity.this)
                .load(rdCode)
                .into(qrCode);
    }


    /**
     * 描述: 相机成功失败回调 <br>
     * 日期: 2019-11-09 20:03 <br>
     * 作者: 林明健 <br>
     */
    public interface Callback {
        void onPictureSuccess(Bitmap bmp, String filePath);

        void onPictureError(String errMsg);

        void onPreviousClick();

        void onNextClick();
    }

    public class RotateTransformation extends BitmapTransformation {
        private final String ID = "com.yddx.RotateTransformation";
        private final byte[] ID_BYTES = ID.getBytes(CHARSET);
        private float rotateRotationAngle = 0f;

        RotateTransformation(float rotateRotationAngle) {
            this.rotateRotationAngle = rotateRotationAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            if (rotateRotationAngle == 0) {
                rotateBitmap = toTransform;
            } else {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotateRotationAngle);
                rotateBitmap = Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
            }
            return rotateBitmap;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof RotateTransformation;
        }

        @Override
        public int hashCode() {
            return ID.hashCode() + (int) rotateRotationAngle;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(ID_BYTES);
        }
    }
}