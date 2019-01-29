package com.old.time.postcard;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.PhoneInfo;

import java.util.List;

public class PostCardAdapter extends BaseQuickAdapter<PhoneInfo, BaseViewHolder> {


    public PostCardAdapter(@Nullable List<PhoneInfo> data) {
        super(R.layout.adapter_post_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneInfo item) {
        helper.setText(R.id.tv_user_pic, item.getNamePic())//
                .setText(R.id.tv_user_name, item.getName())//
                .setVisible(R.id.view_line, item.getIsShow())//
                .setVisible(R.id.tv_name_letter, item.getIsShow())//
                .setText(R.id.tv_name_letter, item.getSortKey());

    }
}
