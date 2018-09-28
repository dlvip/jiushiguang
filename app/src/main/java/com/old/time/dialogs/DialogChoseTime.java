package com.old.time.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.old.time.R;
import com.old.time.utils.TimeUtil;
import com.old.time.views.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/9/28.
 */

public class DialogChoseTime extends DialogBaseChoose {

    private OnChooseTimeCallBack onChooseTimeCallBack;
    private int type;

    public DialogChoseTime(@NonNull Context context, OnChooseTimeCallBack onChooseTimeCallBack) {
        super(context, R.style.transparentFrameWindowStyle);
        this.onChooseTimeCallBack = onChooseTimeCallBack;

    }

    private long currentTime;

    /**
     * 显示dialog
     *
     * @param dialogTitle
     * @param startTime   毫秒值
     * @param maxDay      能选择的最大天数（第一列）
     * @param type        type 0:开始时间  1：结束时间
     */
    public void showChooseTimeDialog(int type, String dialogTitle, long startTime, int maxDay) {
        show();
        this.type = type;
        setDialogTitle(dialogTitle);
        if (startTime == 0) {
            currentTime = System.currentTimeMillis() + 1 * 1000 * 60 * 60;//N小时以后

        } else {
            currentTime = startTime;

        }
        wv_first.setVisibility(View.VISIBLE);
        wv_second.setVisibility(View.VISIBLE);
        wv_third.setVisibility(View.VISIBLE);
        if (type == 0) {

        }
        List<String> firstStrs = initFirstData(currentTime, maxDay);
        wv_first.setData(firstStrs);
        wv_first.setDefault(0);

        List<String> secondStrs = initSecondData(firstStrs.get(0));
        wv_second.setData(secondStrs);
        wv_second.setDefault(0);

        List<String> thirdStrs = initThirdData(firstStrs.get(0), secondStrs.get(0));
        wv_third.setData(thirdStrs);
        wv_third.setDefault(0);

    }

    @Override
    protected void initDialogView() {
        super.initDialogView();
        wv_first.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                List<String> secondStrs = initSecondData(text);
                wv_second.setData(secondStrs);
                wv_second.setDefault(0);

                List<String> thirdStrs = initThirdData(text, wv_second.getItemText(0));
                wv_third.setData(thirdStrs);
                wv_third.setDefault(0);

            }

            @Override
            public void selecting(int id, String text) {


            }
        });

        wv_second.setOnSelectListener(new WheelView.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                List<String> thirdStrs = initThirdData(wv_first.getSelectedText(), text);
                wv_third.setData(thirdStrs);
                wv_third.setDefault(0);

            }

            @Override
            public void selecting(int id, String text) {


            }
        });
    }

    @Override
    public void onClickTrueView() {
        super.onClickTrueView();
        if (onChooseTimeCallBack == null) {

            return;
        }
        String firstStr = wv_first.getSelectedText();
        if (firstStr.equals("今天")) {
            firstStr = TimeUtil.timeh(System.currentTimeMillis());

        }

        String timeStr = TimeUtil.getNowYear() + "年" + firstStr;
        long timeLong = TimeUtil.getlongTime(timeStr);
        long todayLong = TimeUtil.getlongTime(TimeUtil.longToNYRhz(System.currentTimeMillis()));
        if (timeLong < todayLong) {//跨年逻辑
            timeStr = (TimeUtil.getNowYear() + 1) + "年" + firstStr;

        }
        timeStr = timeStr + wv_second.getSelectedText() + wv_third.getSelectedText();
        timeStr = TimeUtil.getDateTime(TimeUtil.getTimeLong(timeStr)).replace("-", ".");
        onChooseTimeCallBack.timeCallBack(type, timeStr);
    }

    private List<String> firstStrs = new ArrayList<>();
    private List<String> secondStrs = new ArrayList<>();
    private List<String> thirdStrs = new ArrayList<>();

    /**
     * 初始化第一个数据
     */
    private List<String> initFirstData(long initTime, int size) {
        firstStrs.clear();
        String todayStr = TimeUtil.timeh(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            String time = TimeUtil.timeh(initTime + ((long) i) * 1000 * 60 * 60 * 24);
            if (time.equals(todayStr)) {//今天
                firstStrs.add("今天");

            } else {
                firstStrs.add(time);

            }
        }
        return firstStrs;
    }

    /**
     * 初始化第二个
     *
     * @return
     */
    private List<String> initSecondData(String firstStr) {
        int star = 0;
        if (firstStr.equals("今天") || TimeUtil.yymmddTolong("2018年" + firstStr) < currentTime) {
            String timeH_MStr = TimeUtil.longToH_M(currentTime);
            String[] H_M = timeH_MStr.split(":");
            star = Integer.parseInt(H_M[0]);
        }
        secondStrs.clear();
        for (int i = star; i < 24; i++) {
            secondStrs.add((i < 10 ? "0" + i : i + "") + "时");

        }
        return secondStrs;
    }

    /**
     * 初始化第三个
     *
     * @return
     */
    private List<String> initThirdData(String firstStr, String secondStr) {
        int star = 0;
        if (firstStr.equals("今天")  || TimeUtil.yymmddTolong("2018年" + firstStr) < currentTime) {
            String timeH_MStr = TimeUtil.longToH_M(currentTime);
            String[] H_M = timeH_MStr.split(":");
            int H = Integer.parseInt(H_M[0]);//小时
            if (H == Integer.parseInt(secondStr.replace("时", ""))) {//
                star = Integer.parseInt(H_M[1]);

            }
        }
        thirdStrs.clear();
        for (int i = star; i < 60; i++) {
            thirdStrs.add((i < 10 ? "0" + i : i + "") + "分");

        }

        return thirdStrs;
    }

    public interface OnChooseTimeCallBack {
        void timeCallBack(int type, String timeStr);

    }
}
