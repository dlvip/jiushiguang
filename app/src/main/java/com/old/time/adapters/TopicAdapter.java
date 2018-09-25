package com.old.time.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.TopicBean;

import java.util.List;

/**
 * Created by wcl on 2018/8/7.
 */

public class TopicAdapter extends BaseQuickAdapter<TopicBean, BaseViewHolder> {

    public TopicAdapter(@Nullable List<TopicBean> data) {
        super(R.layout.adapter_topic, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, TopicBean item) {
        helper.setText(R.id.tv_topic_title, item.getTopicTitle())//
                .setText(R.id.tv_topic_detail, "")//
                .setText(R.id.tv_topic_count, item.getTopicCount());

    }
}
