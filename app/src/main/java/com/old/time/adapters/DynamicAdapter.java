package com.old.time.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.TopicDetailCActivity;
import com.old.time.activitys.UserDynamicActivity;
import com.old.time.beans.DynamicBean;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.beans.TopicBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.StringUtils;
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

    @Override
    protected void convert(DynamicViewHolder helper, final DynamicBean item) {
        helper.setText(R.id.tv_create_time, StringUtils.getCreateTime(item.getCreateTime()));
        ExpandableTextView expand_text_view = helper.mExpandableTextView;
        if (TextUtils.isEmpty(item.getContent())) {
            expand_text_view.setVisibility(View.GONE);

        } else {
            expand_text_view.setVisibility(View.VISIBLE);
            expand_text_view.setText(item.getContent());

        }
        NineImageView mNineImageView = helper.mNineImageView;
        List<PhotoInfoBean> photoInfoBeans = item.getContentImages();
        if (photoInfoBeans == null || photoInfoBeans.size() == 0) {
            mNineImageView.setVisibility(View.GONE);

        } else {
            mNineImageView.setVisibility(View.VISIBLE);
            mNineImageView.setDataForView(photoInfoBeans);

        }
        ImageView img_user_header = helper.getView(R.id.img_user_header);
        final UserInfoBean userInfoBean = item.getUserEntity();
        if (userInfoBean != null) {
            GlideUtils.getInstance().setRadiusImageView(mContext, userInfoBean.getAvatar(), img_user_header, 10);
            helper.setText(R.id.tv_user_name, userInfoBean.getUserName());
            img_user_header.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!(mContext instanceof UserDynamicActivity)) {
                        UserDynamicActivity.startUserDynamicActivity(mContext, userInfoBean);

                    }
                }
            });
        }
        final TopicBean topicBean = item.getTopicEntity();
        if (topicBean != null) {
            helper.setText(R.id.tv_topic_title, topicBean.getTopic());
            helper.getView(R.id.tv_topic_title).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(mContext instanceof TopicDetailCActivity)) {
                        TopicDetailCActivity.startTopicDetailActivity((Activity) mContext, topicBean);

                    }
                }
            });
        }
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
