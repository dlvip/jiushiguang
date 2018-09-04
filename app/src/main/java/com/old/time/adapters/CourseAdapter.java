package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.MusicActivity;
import com.old.time.activitys.MusicPlayActivity;
import com.old.time.beans.CourseBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/7/21.
 */

public class CourseAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    public CourseAdapter(@Nullable List<CourseBean> data) {
        super(R.layout.adapter_course, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CourseBean item) {
        helper.setText(R.id.tv_course_title, item.title);
        ImageView img_course_pic = helper.getView(R.id.img_course_pic);
        GlideUtils.getInstance().setImageView(mContext, item.coursePic, img_course_pic);
        final int position = helper.getLayoutPosition() - getHeaderLayoutCount();
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseBean mCourseBean = getItem(position);
                MusicPlayActivity.startMusicPlayActivity(mContext, mCourseBean);

            }
        });
    }
}
