package com.old.time.adapters;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.CommentBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by NING on 2018/4/16.
 */

public class CommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {


    public CommentAdapter(List<CommentBean> data) {
        super(R.layout.adapter_dynamic_comment, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {
        UserInfoBean userInfoBean = item.getUserEntity();
        if (userInfoBean == null) {

            return;
        }
        ImageView img_user_header = helper.getView(R.id.img_user_header);
        GlideUtils.getInstance().setRoundImageView(mContext, userInfoBean.getAvatar(), img_user_header);
        helper.setText(R.id.tv_user_name, userInfoBean.getUserName())//
                .setText(R.id.tv_create_time, item.getCreateTime())//
                .setText(R.id.tv_comment_content, item.getComment());

    }
}
