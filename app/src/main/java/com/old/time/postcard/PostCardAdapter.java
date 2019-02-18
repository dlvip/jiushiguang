package com.old.time.postcard;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.PhoneInfo;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

public class PostCardAdapter extends BaseQuickAdapter<PhoneInfo, BaseViewHolder> {

    PostCardAdapter(@Nullable List<PhoneInfo> data) {
        super(R.layout.adapter_post_card, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final PhoneInfo item) {
        ImageView img_user_pic = helper.getView(R.id.img_user_pic);
        GlideUtils.getInstance().setRoundImageView(mContext, item.getPhoto(), img_user_pic);
        helper.setText(R.id.tv_user_name, item.getName())//
                .setGone(R.id.view_line_1, helper.getLayoutPosition() != 0);

        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PCardDetailActivity.startPCardDetailActivity(mContext, item);

            }
        });
    }
}
