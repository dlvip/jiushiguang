package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.MusicActivity;
import com.old.time.beans.TeacherBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/7/21.
 */

public class HMusicAdapter extends BaseQuickAdapter<TeacherBean, BaseViewHolder> {

    public HMusicAdapter(@Nullable List<TeacherBean> data) {
        super(R.layout.adapter_h_music, data);

    }

    @Override
    protected void convert(final BaseViewHolder helper, TeacherBean item) {
        helper.setText(R.id.tv_user_name, item.userName + "," + item.vocation);
        ImageView img_book_pic = helper.getView(R.id.img_book_pic);
        GlideUtils.getInstance().setRadiusImageView(mContext, item.avatar, img_book_pic, 5);
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
