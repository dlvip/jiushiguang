package com.old.time.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.TopicDetailCActivity;
import com.old.time.activitys.VideoDetailActivity;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

public class TopicDAdapter extends BaseQuickAdapter<TopicBean, BaseViewHolder> {

    public TopicDAdapter(@Nullable List<TopicBean> data) {
        super(R.layout.adapter_dynamic_topic, data);

    }

    @Override
    protected void convert(final BaseViewHolder helper, final TopicBean item) {
        ImageView img_topic_pic = helper.getView(R.id.img_topic_pic);
        GlideUtils.getInstance().setRadiusImageView(mContext, item.getPic(), img_topic_pic, 3);
        helper.setText(R.id.tv_topic_title, item.getTopic());
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicDetailCActivity.startTopicDetailActivity((Activity) mContext, item);

            }
        });
    }
}
