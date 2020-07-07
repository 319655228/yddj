package com.hckj.yddxst.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;

public class QrcodeImageView extends RelativeLayout {
    private ImageView imageView;

    public QrcodeImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public QrcodeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public QrcodeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.QrcodeImageView, defStyleAttr, 0);
        int ivWidth = (int) typedArray.getDimension(R.styleable.QrcodeImageView_iv_width, LayoutParams.MATCH_PARENT);
        int ivHeight = (int) typedArray.getDimension(R.styleable.QrcodeImageView_iv_height, LayoutParams.MATCH_PARENT);
        int ivMargin = (int) typedArray.getDimension(R.styleable.QrcodeImageView_iv_margin, 0);
        boolean defShow = typedArray.getBoolean(R.styleable.QrcodeImageView_iv_defv, false);
        Drawable bgDrawable = typedArray.getDrawable(R.styleable.QrcodeImageView_iv_src);

        View view = LayoutInflater.from(context).inflate(R.layout.qrcode_imageview, this);
        imageView = view.findViewById(R.id.image_view_qrcode);

        RelativeLayout.LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
        layoutParams.height = ivHeight;
        layoutParams.width = ivWidth;
        layoutParams.setMargins(ivMargin, ivMargin, ivMargin, ivMargin);
        imageView.setLayoutParams(layoutParams);

        typedArray.recycle();
        if (bgDrawable != null) {
            Glide.with(this).load(bgDrawable).into(imageView);
        }

        setVisibility(defShow ? VISIBLE : INVISIBLE);
    }

    public void show(String path) {
        if (!TextUtils.isEmpty(path)) {
            Glide.with(this)
                    .load(path)
                    .into(imageView);
        }
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(0);
        setAnimation(animation);
        this.setVisibility(VISIBLE);
    }

    public void show() {
        show(null);
    }
}
