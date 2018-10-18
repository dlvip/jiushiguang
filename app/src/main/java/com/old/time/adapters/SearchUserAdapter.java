package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.UserInfoBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/10/18.
 */

public class SearchUserAdapter extends BaseQuickAdapter<UserInfoBean, BaseViewHolder> {

    public SearchUserAdapter(@Nullable List<UserInfoBean> data) {
        super(R.layout.adapter_goods_user, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoBean item) {
        ImageView img_user_header = helper.getView(R.id.img_user_header);
        GlideUtils.getInstance().setRoundImageView(mContext, item.getAvatar(), img_user_header);
        helper.setText(R.id.tv_user_phone, item.getMobile())//
                .setText(R.id.tv_goods_count, "剩余：" + item.getGoodsCount());

    }
}
