package com.old.time.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.MusicDetailActivity;
import com.old.time.beans.CourseBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by NING on 2018/8/15.
 */

public class HCourseAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    public HCourseAdapter(@Nullable List<CourseBean> data) {
        super(R.layout.adapter_h_course, data);

    }

    @Override
    protected void convert(final BaseViewHolder helper, CourseBean item) {
        helper.setText(R.id.tv_course_title, item.title);
        ImageView img_course_pic = helper.getView(R.id.img_course_pic);
        GlideUtils.getInstance().setImageView(mContext, item.coursePic, img_course_pic);
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseBean mCourseBean = getItem(helper.getLayoutPosition());
                MusicDetailActivity.startMusicDetailActivity((Activity) mContext, mCourseBean);

            }
        });
    }
}
