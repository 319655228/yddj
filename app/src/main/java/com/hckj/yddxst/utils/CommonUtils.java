package com.hckj.yddxst.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils{
    private static final int MIN_THRESHOLD = 120;
    private static final int MAX_THRESHOLD = 150;
    public static LinkedList<String> splitText(String text) {
        LinkedList<String> list = new LinkedList<>();
        if (text.length() <= MIN_THRESHOLD) {
            list.add(text);
            return list;
        }
        StringBuffer sb = new StringBuffer(MAX_THRESHOLD);

        String regEx = "[。？！?.!,，]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        String[] substrs = p.split(text);
        if (substrs.length > 0) {
            int count = 0;
            while (count < substrs.length) {
                if (m.find()) {
                    substrs[count] += m.group();
                }
                count++;
            }
        }
        for (int i = 0; i < substrs.length; i++) {
            if (substrs[i].length() < MIN_THRESHOLD) {    //语句小于要求的分割粒度
                sb.append(substrs[i]);
                //sb.append("||");
                if (sb.length() > MIN_THRESHOLD) {
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            } else {    //语句满足要求的分割粒度
                if (sb.length() != 0)    //此时如果缓存有内容则应该先将缓存存入再存substrs[i]的内容  以保证原文顺序
                {
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                }
                list.add(substrs[i]);
            }
        }

        if (!list.isEmpty() && sb.length() > 0 && !list.getLast().contains(sb.toString())) {
            list.add(sb.toString());
        }
        return list;
    }

    // 静音状态动作集合
    private static List<String> slientActionList = new LinkedList<String>() {
        {
            add("挥手");
            add("挥手");
            add("挥手");
            add("挥手");
            add("不耐烦");
            add("高兴");
            add("摊手");
            add("歪头");

        }
    };

    // 音频状态动作集合
    private static List<String> speakingActionList = new LinkedList<String>() {{
        add("左侧展示");
        add("自然双手");
        add("双手张开");
        add("双手张开");
        add("激情澎湃");
        add("激情澎湃");
        add("强调2");
        add("强调2");
        add("强调");
        add("单手强调");
        add("单手强调");
        add("单手握拳");
        add("单手握拳");
    }};

    /**
     * 获取随机动作
     * @param isSilent 是否静音状态
     * @return 动作
     */
    public static String getRandomAction(boolean isSilent) {
        if (isSilent) {
            return slientActionList.get((int) (slientActionList.size() * Math.random()));

        } else {
            return speakingActionList.get((int) (speakingActionList.size() * Math.random()));
        }
    }
}
