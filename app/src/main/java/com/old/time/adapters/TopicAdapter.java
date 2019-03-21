package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.TopicBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/8/7.
 */

public class TopicAdapter extends BaseQuickAdapter<TopicBean, BaseViewHolder> {

    public TopicAdapter(@Nullable List<TopicBean> data) {
        super(R.layout.adapter_topic, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final TopicBean item) {
        helper.setText(R.id.tv_topic_title, item.getTopic());
        ImageView img_topic_bg = helper.getView(R.id.img_topic_bg);
        GlideUtils.getInstance().setRadiusImageView(mContext, item.getPic(), img_topic_bg, 5);

    }
}
