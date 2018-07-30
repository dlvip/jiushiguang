package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.CourseBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/7/21.
 */

public class HCourseAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {


    public HCourseAdapter(@Nullable List<CourseBean> data) {
        super(R.layout.adapter_h_course, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseBean item) {
        helper.setText(R.id.tv_course_title, item.title);
        ImageView img_course_pic = helper.getView(R.id.img_course_pic);
        GlideUtils.getInstance().setImageView(mContext, item.coursePic, img_course_pic);

    }
}
