package com.old.time.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.DynamicSActivity;
import com.old.time.activitys.DynamicDetailCActivity;
import com.old.time.beans.DynamicBean;
import com.old.time.views.ExpandableTextView;
import com.old.time.views.NineImageView;

import java.util.List;

/**
 * Created by wcl on 2018/3/9.
 */

public class DynamicAdapter extends BaseQuickAdapter<DynamicBean, DynamicAdapter.DynamicViewHolder> {

    public DynamicAdapter(List<DynamicBean> data) {
        super(R.layout.adapter_dynamic, data);

    }

    private RecyclerView mRecyclerView;

    private boolean isScrolling;

    @Override
    protected void convert(DynamicAdapter.DynamicViewHolder helper, final DynamicBean item) {
        if (mRecyclerView == null) {
            mRecyclerView = getRecyclerView();
            if (mRecyclerView != null) {
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            isScrolling = false;
                            notifyDataSetChanged();

                        } else {
                            isScrolling = true;

                        }
                    }
                });
            }
        }
        helper.setText(R.id.tv_content_time, item.createTimeStr.substring(0, 10));
        ExpandableTextView expand_text_view = helper.mExpandableTextView;
        if (TextUtils.isEmpty(item.conetent)) {
            expand_text_view.setVisibility(View.GONE);

        } else {
            expand_text_view.setVisibility(View.VISIBLE);
            expand_text_view.setText(item.conetent);

        }
        NineImageView mNineImageView = helper.mNineImageView;
        if (item.conetentImages == null || item.conetentImages.size() == 0) {
            mNineImageView.setVisibility(View.GONE);

        } else {
            mNineImageView.setVisibility(View.VISIBLE);
            mNineImageView.setDataForView(item.conetentImages);
            mNineImageView.setRecyclerViewOnScrolling(isScrolling);

        }
        helper.getView(R.id.img_user_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DynamicSActivity.startDynamicActivity((Activity) mContext, "");

            }
        });
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DynamicDetailCActivity.startDynamicDetailActivity((Activity) mContext, item);

            }
        });
    }

    public class DynamicViewHolder extends BaseViewHolder {

        private NineImageView mNineImageView;
        private ExpandableTextView mExpandableTextView;

        public DynamicViewHolder(View view) {
            super(view);
            mNineImageView = view.findViewById(R.id.nineImageView);
            mExpandableTextView = view.findViewById(R.id.expand_text_view);

        }
    }
}
