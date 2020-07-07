package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;
import com.hckj.yddxst.adapter.MeetingNickNameAdapter;
import com.hckj.yddxst.bean.MeetingContentInfo;
import com.hckj.yddxst.bean.SocketInfo;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MeetingContentLayout extends LinearLayout {
    private int curContentIndex = 0;
    private TextView tvCurSegment;
    private TextView tvNextSegment;
    private MaequeeText tvListSegment;
    private ImageView ivVolume;
    private ImageView ivMeeting;
    private EmptyControlVideo vpMeeting;
    private TextView tvMeeting;
    private MeetingSpeakView layoutSpeakView;
    private RecyclerView rvMeetingNickname;
    private LayoutListener lister;
    // 参会人适配器
    private MeetingNickNameAdapter nickNameAdapter;

    // 参会人集合
    private List<SocketInfo.Content> peopleList;

    // 当期会议内容列表
    private LinkedList<MeetingContentInfo.SubContentInfo> meetingContentList;


    public MeetingContentLayout(Context context) {
        super(context);
        init(context);
    }

    public MeetingContentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeetingContentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_content, this);
        ImageView btnPrevious = view.findViewById(R.id.btn_content_previous);
        ImageView btnNext = view.findViewById(R.id.btn_content_next);
        ImageView btnNextSeg = view.findViewById(R.id.btn_next_seg);
        rvMeetingNickname = view.findViewById(R.id.rv_meeting_nickname);

        layoutSpeakView = view.findViewById(R.id.layoutSpeakView);
        tvMeeting = view.findViewById(R.id.tv_meeting);
        vpMeeting = view.findViewById(R.id.vp_meeting);
        ivMeeting = view.findViewById(R.id.iv_meeting);
        tvCurSegment = view.findViewById(R.id.tv_cur_segment);
        tvNextSegment = view.findViewById(R.id.tv_next_segment);
        tvListSegment = view.findViewById(R.id.tv_list_segment);
        ivVolume = view.findViewById(R.id.iv_volume);

        vpMeeting.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
                if (lister != null) {
                    lister.onError("播放异常，请检查视频资源以及现场网络状况");
                }
            }
        });

        ivVolume.setOnClickListener(v -> {
            if (lister != null) {
                lister.onVolume();
            }
        });

        peopleList = new LinkedList<>();

        nickNameAdapter = new MeetingNickNameAdapter(R.layout.item_meeting_nickname, peopleList);
        nickNameAdapter.openLoadAnimation(); // 加载动画
        rvMeetingNickname.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvMeetingNickname.setAdapter(nickNameAdapter);

        btnPrevious.setOnClickListener(v -> previous());
        btnNext.setOnClickListener(v -> next());
        btnNextSeg.setOnClickListener(v -> nextSeg());
    }

    public void loadMeetingContent(List<MeetingContentInfo> contentList) {
        layoutSpeakView.setListData(null);
        setChatContent(null);
        flatContentList(contentList);
        swithContent(0);
    }

    public void addPeople(SocketInfo.Content people) {
        if (people == null)
            return;
        peopleList.add(people);
        nickNameAdapter.notifyDataSetChanged();
        int position = nickNameAdapter.getItemCount() - 1;
        if (position >= 0) {
            rvMeetingNickname.smoothScrollToPosition(position);
        }
    }

    public void removePeople(String clientId) {
        clientId = clientId + "";
        for (SocketInfo.Content content : peopleList) {
            if (clientId.equals(content.getClient_idX())) {
                peopleList.remove(content);
                break;
            }
        }
        nickNameAdapter.notifyDataSetChanged();
        int position = nickNameAdapter.getItemCount() - 1;
        if (position >= 0) {
            rvMeetingNickname.smoothScrollToPosition(position);
        }
    }


    /**
     * 描述: 扁平化章节内容 <br>
     * 日期: 2019-10-17 22:59 <br>
     * 作者: 林明健 <br>
     */
    private void flatContentList(List<MeetingContentInfo> chapterList) {
        meetingContentList = new LinkedList<>();

        StringBuffer segList = new StringBuffer();
        int chapterSize = chapterList.size();
        for (int chapterIndex = 0; chapterIndex < chapterSize; chapterIndex++) {
            MeetingContentInfo meetingContentInfo = chapterList.get(chapterIndex);
            String chapterTitle = meetingContentInfo.getTitle();
            String nextChapterTitle = chapterIndex + 1 < chapterSize ? chapterList.get(chapterIndex + 1).getTitle() : "结束";

            LinkedList<MeetingContentInfo.SubContentInfo> subChapterList = meetingContentInfo.getContent_list();
            int subChapterSize = subChapterList == null ? 0 : subChapterList.size();
            for (int subIndex = 0; subIndex < subChapterSize; subIndex++) {
                MeetingContentInfo.SubContentInfo subContentInfo = subChapterList.get(subIndex);
                subContentInfo.setChapterTitle(chapterTitle);
                subContentInfo.setNextChapterTitle(nextChapterTitle);
                meetingContentList.add(subContentInfo);
            }

            if (segList.length() > 0) {
                segList.append(" > ");
            }
            segList.append(chapterTitle + "");
        }

        segList.append(" > 结束");
        tvListSegment.setText(segList.toString());
    }

    /**
     * 描述: 上一页/下一页切换内容 <br>
     * 日期: 2019-10-20 13:30 <br>
     * 作者: 林明健 <br>
     *
     * @param curIndex 页数序号
     */
    private void swithContent(int curIndex) {
        if (getVisibility() != View.VISIBLE) {
            return;
        }
        int contentSize = meetingContentList == null ? 0 : meetingContentList.size();
        // 会议没有内容时候
        if (contentSize == 0) {
            if (lister != null)
                lister.onWarn("会议信息异常，请退出重试");
            return;
        }

        if (curIndex < 0) {
            if (lister != null){
                lister.onQuit();
            }
            return;
        }

        // 会议结束
        if (curIndex + 1 > contentSize) {
            if (lister != null) {
                lister.onStop();
                lister.onComplete();
            }
            return;
        }
        if (lister != null) {
            lister.onStop();
        }
        curContentIndex = curIndex;
        MeetingContentInfo.SubContentInfo contentInfo = meetingContentList.get(curIndex);
        tvNextSegment.setText("下一环节：" + contentInfo.getNextChapterTitle());
        tvCurSegment.setText("" + contentInfo.getChapterTitle());

        String type = contentInfo.getType();
        if (type == null || "content".equals(type)) {
            Glide.with(getContext())
                    .load(contentInfo.getImg_url())
                    .into(ivMeeting);
            ivMeeting.setVisibility(View.VISIBLE);
            vpMeeting.setVisibility(View.GONE);
            if (1 == contentInfo.getIs_read() && lister != null) {
                lister.onSpeach(contentInfo.getContent() + "");
            }
        } else if ("video".equals(type)) {
            String url = contentInfo.getVideo_url();
            vpMeeting.start(url + "", false);
            ivMeeting.setVisibility(View.GONE);
            vpMeeting.setVisibility(View.VISIBLE);
        }
        tvMeeting.setText(contentInfo.getTitle() + "\n" + contentInfo.getContent());

        if (lister != null) {
            lister.onSwith(contentInfo.getMeeting_id() + "", contentInfo.getId() + "");
        }
    }

    /**
     * 描述: 上一环节 <br>
     * 日期: 2019-10-19 12:29 <br>
     * 作者: 林明健 <br>
     */
    public void preSeg() {
        if (getVisibility() != View.VISIBLE) {
            return;
        }
        int contentSize = meetingContentList == null ? 0 : meetingContentList.size();
        if (contentSize == 0 || curContentIndex >= contentSize) {
            return;
        }
        MeetingContentInfo.SubContentInfo curContentInfo = meetingContentList.get(curContentIndex);
        String chapterTitle = curContentInfo.getChapterTitle();
        for (int index = curContentIndex; index >= 0; index--) {
            if (!chapterTitle.equals(meetingContentList.get(index).getChapterTitle())) {
                swithContent(index);
                return;
            }
        }
    }

    /**
     * 描述: 下一环节 <br>
     * 日期: 2019-10-18 01:40 <br>
     * 作者: 林明健 <br>
     */
    public void nextSeg() {
        if (getVisibility() != View.VISIBLE) {
            return;
        }
        int contentSize = meetingContentList == null ? 0 : meetingContentList.size();
        if (contentSize == 0 || curContentIndex >= contentSize) {
            if (lister != null) {
                lister.onStop();
                lister.onComplete();
            }
            return;
        }
        MeetingContentInfo.SubContentInfo curContentInfo = meetingContentList.get(curContentIndex);
        String chapterTitle = curContentInfo.getChapterTitle();
        for (int index = curContentIndex + 1; index < contentSize; index++) {
            if (!chapterTitle.equals(meetingContentList.get(index).getChapterTitle())) {
                swithContent(index);
                return;
            }
        }
        if (lister != null) {
            lister.onStop();
            lister.onComplete();
        }
    }

    public void next() {
        if (getVisibility() != View.VISIBLE) {
            return;
        }
        if (lister != null) {
            lister.onStop();
        }
        swithContent(curContentIndex + 1);
    }

    public void previous() {
        if (getVisibility() != View.VISIBLE) {
            return;
        }
        if (lister != null) {
            lister.onStop();
        }
        swithContent(curContentIndex - 1);
    }

    public void backToFinal() {
        if (getVisibility() != View.VISIBLE) {
            return;
        }
        int iFinal = meetingContentList.size() - 1;
        swithContent(iFinal);
    }

    public interface LayoutListener {
        void onWarn(String msg);

        void onError(String msg);

        void onComplete();

        void onStop();

        void onSpeach(String word);

        void onSwith(String meetingId, String contentId);

        void onVolume();

        void onQuit();
    }

    public void setListener(LayoutListener l) {
        this.lister = l;
    }

    public void updateVolume(int volume) {
        if (volume <= 0) {
            Glide.with(this)
                    .load(R.drawable.bg_volume_close)
                    .into(ivVolume);
        } else {
            Glide.with(this)
                    .load(R.drawable.bg_volume_open)
                    .into(ivVolume);
        }
    }

    public void setChatContent(SocketInfo.Content chatInfo) {
        layoutSpeakView.setListData(chatInfo);
    }
}
