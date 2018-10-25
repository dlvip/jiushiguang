package com.old.time.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.aidl.ChapterBean;

import java.util.List;

/**
 * Created by NING on 2018/10/25.
 */

public class ChapterAdapter extends BaseQuickAdapter<ChapterBean, BaseViewHolder> {

    private int cPosition;

    public ChapterAdapter(@Nullable List<ChapterBean> data) {
        super(R.layout.adapter_music, data);
    }

    public void setcPosition(int cPosition){
        this.cPosition = cPosition;
        notifyDataSetChanged();

    }

    @Override
    protected void convert(BaseViewHolder helper, ChapterBean item) {
        int position = helper.getLayoutPosition() - getHeaderLayoutCount() + 1;
        int colorSrc;
        if (position - 1 == cPosition) {
            colorSrc = mContext.getResources().getColor(R.color.color_ff4444);

        } else {
            colorSrc = mContext.getResources().getColor(R.color.color_000);

        }
        helper.setText(R.id.tv_music_index, position + " .")//
                .setTextColor(R.id.tv_music_index, colorSrc)//
                .setText(R.id.tv_music_title, item.getTitle())//
                .setTextColor(R.id.tv_music_title, colorSrc)//
                .setText(R.id.tv_music_time, item.getDurationStr())//
                .setTextColor(R.id.tv_music_time, colorSrc);
    }
}
