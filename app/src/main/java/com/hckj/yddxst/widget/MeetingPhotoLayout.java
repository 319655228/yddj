package com.hckj.yddxst.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.android.cameraview.CameraView;
import com.hckj.yddxst.Constant;
import com.hckj.yddxst.R;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.utils.FileIOUtils;
import com.rxjava.rxlife.RxLife;

import java.io.File;
import java.security.MessageDigest;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 描述: 会议模式拍照布局 <br>
 * 日期: 2019-11-09 20:04 <br>
 * 作者: 林明健 <br>
 */
public class MeetingPhotoLayout extends LinearLayout implements View.OnClickListener {
    private CameraView cameraView;
    private ImageView ivPreview;
    private ImageView btnPhoto;
    private ImageView btnRephoto;
    private ImageView btnUpload;
    private ImageView btnRotate;
    private String photoUrl;
    private Callback mCallback;
    private ImageView btnPrevious;
    private ImageView btnNext;
    private View viewTips;
    private float floatAngle = -90f;
    private Bitmap rotateBitmap;
    public MeetingPhotoLayout(Context context) {
        super(context);
        init(context);
    }

    public MeetingPhotoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeetingPhotoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_photo, this);
        cameraView = view.findViewById(R.id.camera_view);
        ivPreview = view.findViewById(R.id.iv_preview);
        btnPhoto = view.findViewById(R.id.btn_photo);
        btnRephoto = view.findViewById(R.id.btn_rephoto);
        btnUpload = view.findViewById(R.id.btn_upload);
        btnPrevious = view.findViewById(R.id.btn_previous);
        btnRotate = view.findViewById(R.id.btn_rotate);
        btnNext = view.findViewById(R.id.btn_next);
        viewTips = view.findViewById(R.id.tv_tips);

        btnPhoto.setOnClickListener(this);
        btnRephoto.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        swithPhotoLayout(true);
    }

    public void startCamera(Callback cb) {
        this.mCallback = cb;
        try {
            if (cameraView != null) {
                cameraView.setVisibility(VISIBLE);
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
                    String path = Constant.FILE_DIR + new Date().getTime() + ".jpg";
                    File file = new File(path);
                    FileIOUtils.writeFileFromBytesByStream(file, data);
                    return path;
                })
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        viewTips.setVisibility(VISIBLE);
                        btnUpload.setVisibility(GONE);
                        btnRephoto.setVisibility(GONE);
                        btnPhoto.setVisibility(GONE);
                        btnRotate.setVisibility(GONE);
                    }

                    @Override
                    public void onNext(String filePath) {
                        photoUrl = filePath;
                        Log.e("拍照路径", "handleCameraData: " + photoUrl);
                        rotateImage(filePath);
                        swithPhotoLayout(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("PhotoLayout", "handleCameraData: " + e.getMessage());
                        mCallback.onPictureError("拍照出错！");
                        btnPhoto.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        viewTips.setVisibility(GONE);
                    }
                });
    }

    private void rotateImage(String filePath) {
        floatAngle += 90f;
        if (floatAngle >= 360f || floatAngle < 0f) {
            floatAngle = 0f;
        }
        Log.e("DIdi", "rotateImage: " + floatAngle);
        Glide.with(MeetingPhotoLayout.this)
                .asBitmap()
                .load(filePath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new RotateTransformation(floatAngle))
                .into(ivPreview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photo:  // 拍照
                if (cameraView != null && cameraView.isCameraOpened()) {
                    cameraView.takePicture();
                }

                break;
            case R.id.btn_rephoto: // 重拍
                swithPhotoLayout(true);
                break;
            case R.id.btn_rotate:
                rotateImage(photoUrl);
                break;
            case R.id.btn_upload:  // 上传
                if (mCallback != null) {
                    mCallback.onPictureSuccess(rotateBitmap, photoUrl);
                }
                break;
            case R.id.btn_previous:
                if (mCallback != null) {
                    mCallback.onPreviousClick();
                }
                break;
            case R.id.btn_next:
                if (mCallback != null) {
                    mCallback.onNextClick();
                }
                break;
            default:
                break;
        }
    }

    private void swithPhotoLayout(boolean showPhoto) {
        if (showPhoto) {
            ivPreview.setVisibility(GONE);
            btnPhoto.setVisibility(VISIBLE);
            btnRephoto.setVisibility(GONE);
            btnUpload.setVisibility(GONE);
            btnRotate.setVisibility(GONE);
        } else {
            ivPreview.setVisibility(VISIBLE);
            btnPhoto.setVisibility(GONE);
            btnRephoto.setVisibility(VISIBLE);
            btnUpload.setVisibility(VISIBLE);
            if (mCallback != null) {
                mCallback.onPictureSuccess(rotateBitmap, photoUrl);
            }
            btnRotate.setVisibility(VISIBLE);
        }
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
