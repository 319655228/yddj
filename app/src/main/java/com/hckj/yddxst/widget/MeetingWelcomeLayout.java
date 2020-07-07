package com.hckj.yddxst.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hckj.yddxst.R;
import com.hckj.yddxst.adapter.MeetingUserAdapter;
import com.hckj.yddxst.bean.SocketInfo;

import java.util.LinkedList;
import java.util.List;

public class MeetingWelcomeLayout extends LinearLayout {
    private ImageView ivQrcode;
    private RecyclerView rvMeetingPeople;
    private TextView tvJoinNumber;
    private ImageView btnPrevious;
    private ImageView btnNext;
    private List<SocketInfo.Content> peopleList;
    private MeetingUserAdapter meetingPeopleAdapter;

    public MeetingWelcomeLayout(Context context) {
        super(context);
        init(context);
    }

    public MeetingWelcomeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeetingWelcomeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_meeting_welcome, this);
        ivQrcode = view.findViewById(R.id.iv_qrcode);
        rvMeetingPeople = view.findViewById(R.id.rv_meeting_people);
        btnPrevious = view.findViewById(R.id.btn_previous);
        btnNext = view.findViewById(R.id.btn_next);
        tvJoinNumber = view.findViewById(R.id.tv_join_number);

        peopleList = new LinkedList<>();
        meetingPeopleAdapter = new MeetingUserAdapter(R.layout.item_meeting_people, peopleList);
        meetingPeopleAdapter.openLoadAnimation(); // 加载动画
        rvMeetingPeople.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvMeetingPeople.setAdapter(meetingPeopleAdapter);
    }

    public void animLoadImg(String imgUrl, String joinNumber) {
        peopleList.clear();
        meetingPeopleAdapter.notifyDataSetChanged();

        tvJoinNumber.setText("会议口令号：" + joinNumber);
        Glide.with(getContext())
                .load(imgUrl + "")
                .into(ivQrcode);
        //Glide新版动画改版了，使用原生动画取代放大功能
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(0);
        ivQrcode.setAnimation(animation);
    }

    public void addPeople(SocketInfo.Content people) {
        if (people == null)
            return;
        peopleList.add(people);
        meetingPeopleAdapter.notifyDataSetChanged();
    }

    public void removePeople(String clientId) {
        clientId = clientId + "";
        for (SocketInfo.Content content : peopleList) {
            if (clientId.equals(content.getClient_idX())) {
                peopleList.remove(content);
                break;
            }
        }
        meetingPeopleAdapter.notifyDataSetChanged();
    }

    public void setClickListener(OnClickListener l1, OnClickListener l2) {
        if (btnPrevious != null) {
            btnPrevious.setOnClickListener(l1);
        }
        if (btnNext != null) {
            btnNext.setOnClickListener(l2);
        }
    }
}
