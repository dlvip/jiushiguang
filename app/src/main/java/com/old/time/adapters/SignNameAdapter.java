package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.SignDetailActivity;
import com.old.time.beans.SignNameEntity;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.postcard.UserSignActivity;
import com.old.time.utils.StringUtils;

import java.util.List;

public class SignNameAdapter extends BaseQuickAdapter<SignNameEntity, BaseViewHolder> {

    public SignNameAdapter(@Nullable List<SignNameEntity> data) {
        super(R.layout.adapter_card_list, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final SignNameEntity item) {
        ImageView img_user_header = helper.getView(R.id.img_user_header);
        GlideUtils.getInstance().setRadiusImageView(mContext, item.getUserEntity().getAvatar(), img_user_header, 10);
        ImageView imageView = helper.getView(R.id.img_card_pic);
        GlideUtils.getInstance().setImageView(mContext, item.getPicUrl(), imageView);
        helper.setText(R.id.tv_user_name, item.getUserEntity().getUserName())//
                .setText(R.id.tv_book_name, "月亮与六便士")//
                .setText(R.id.tv_sign_content, item.getContent())//
                .setText(R.id.tv_sign_time, StringUtils.getCreateTime(item.getCreatTime()));

        img_user_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof UserSignActivity) {

                    return;
                }
                UserSignActivity.startUserSignActivity(mContext, item.getUserEntity());
            }
        });
        helper.getView(R.id.constraint_layout_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignDetailActivity.startSignDetailActivity(mContext, item);

            }
        });
        helper.getView(R.id.img_btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignDetailActivity.startSignDetailActivity(mContext, item);

            }
        });
    }
}
