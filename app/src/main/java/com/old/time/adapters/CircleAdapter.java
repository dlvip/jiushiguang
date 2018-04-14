package com.old.time.adapters;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.CircleBean;
import com.old.time.views.ExpandableTextView;
import com.old.time.views.MultiImageView;

import java.util.List;

/**
 * Created by wcl on 2018/3/9.
 */

public class CircleAdapter extends BaseQuickAdapter<CircleBean, BaseViewHolder> {

    public CircleAdapter(List<CircleBean> data) {
        super(R.layout.adapter_circle, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CircleBean item) {
        helper.setText(R.id.tv_content_time, item.createTimeStr.substring(0, 10));
        ExpandableTextView expand_text_view = helper.getView(R.id.expand_text_view);
        if (TextUtils.isEmpty(item.conetent)) {
            expand_text_view.setVisibility(View.GONE);

        } else {
            expand_text_view.setVisibility(View.VISIBLE);
            expand_text_view.setText(item.conetent);

        }
        MultiImageView mMultiImageView = helper.getView(R.id.multiImagView);
        if (item.conetentImages == null || item.conetentImages.size() == 0) {
            mMultiImageView.setVisibility(View.GONE);

        } else {
            mMultiImageView.setVisibility(View.VISIBLE);
            mMultiImageView.setList(item.conetentImages);

        }
    }
}
