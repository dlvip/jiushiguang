package com.old.time.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;

import java.util.List;

/**
 * Created by wcl on 2018/8/7.
 */

public class TopicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TopicAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_topic, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
