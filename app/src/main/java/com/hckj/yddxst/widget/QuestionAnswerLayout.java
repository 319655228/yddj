package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hckj.yddxst.R;
import com.hckj.yddxst.bean.Answerable;
import com.hckj.yddxst.bean.DocInfo;
import com.hckj.yddxst.bean.MeetingInfo;
import com.hckj.yddxst.bean.QaInfo;
import com.hckj.yddxst.bean.VideoInfo;

import java.util.List;

public class QuestionAnswerLayout extends RelativeLayout implements View.OnClickListener {
    private RecyclerView rvInner;
    private View layoutOuter;
    private View layoutInner;
    private TextView tvInnerTitle;
    private TextView tvVideo;
    private TextView tvMeeting;
    private TextView tvDocument;
    private View ivReturn;
    private View ivClose;
    private OnClickListener mListener;
    private QaInfo qaInfo;

    public QuestionAnswerLayout(Context context) {
        super(context);
        init(context);
    }

    public QuestionAnswerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public QuestionAnswerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_question_answer, this);
        rvInner = view.findViewById(R.id.rv_inner);
        layoutOuter = view.findViewById(R.id.layout_outer);
        layoutInner = view.findViewById(R.id.layout_inner);
        tvInnerTitle = view.findViewById(R.id.tv_inner_title);
        tvVideo = view.findViewById(R.id.tv_video);
        tvMeeting = view.findViewById(R.id.tv_meeting);
        tvDocument = view.findViewById(R.id.tv_document);
        ivReturn = view.findViewById(R.id.iv_return);
        ivClose = view.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
        tvVideo.setOnClickListener(this);
        tvMeeting.setOnClickListener(this);
        tvDocument.setOnClickListener(this);

        rvInner.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (mListener != null) {
                    mListener.onBackClick();
                }
                break;
            case R.id.iv_return:
                if (layoutInner.getVisibility() == VISIBLE) {
                    layoutInner.setVisibility(GONE);
                    layoutOuter.setVisibility(VISIBLE);
                } else {
                    if (mListener != null) {
                        mListener.onBackClick();
                    }
                }
                break;
            case R.id.tv_video:
                setInnerAdapter(0);
                tvInnerTitle.setText(tvVideo.getText().toString());
                break;
            case R.id.tv_meeting:
                setInnerAdapter(1);
                tvInnerTitle.setText(tvMeeting.getText().toString());
                break;
            case R.id.tv_document:
                setInnerAdapter(2);
                tvInnerTitle.setText(tvDocument.getText().toString());
                break;
            default:
                break;
        }
    }

    private void setInnerAdapter(int flag) {
        AnswerAdapter adapter;
        if (flag == 0) {
            adapter = new AnswerAdapter<VideoInfo>(R.layout.item_doc_classify_list, qaInfo.getVideo_list());
        } else if (flag == 1) {
            adapter = new AnswerAdapter<MeetingInfo>(R.layout.item_doc_classify_list, qaInfo.getMeeting_list());
        } else {
            adapter = new AnswerAdapter<DocInfo>(R.layout.item_doc_classify_list, qaInfo.getDocument_list());
        }
        rvInner.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Answerable item = (Answerable) adapter1.getItem(position);
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        });
        layoutOuter.setVisibility(GONE);
        layoutInner.setVisibility(VISIBLE);
    }

    public void setData(QaInfo qaInfo) {
        this.qaInfo = qaInfo;
        List<DocInfo> document_list = qaInfo.getDocument_list();
        List<MeetingInfo> meeting_list = qaInfo.getMeeting_list();
        List<VideoInfo> video_list = qaInfo.getVideo_list();

        tvDocument.setText("公文"+ getResultCount(document_list));
        tvMeeting.setText("会议"+ getResultCount(meeting_list));
        tvVideo.setText("视频"+ getResultCount(video_list));
    }

    private String getResultCount(List<?> list){
        int count = 0;
        if (list != null) {
            count = list.size();
        }
        return " （查询结果："+count+"个）";
    }
    public void setClickListener(OnClickListener listener){
        this.mListener = listener;
    }

    public interface OnClickListener {
        void onBackClick();

        void onItemClick(Answerable info);
    }

    private class AnswerAdapter<T extends Answerable> extends BaseQuickAdapter<T, BaseViewHolder> {
        AnswerAdapter(int layoutResId, @Nullable List<T> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, T item) {
            helper.setText(R.id.tv_title, item.getDisplayName());
        }
    }
}
