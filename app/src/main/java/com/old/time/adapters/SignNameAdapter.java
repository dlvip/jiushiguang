package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.SignNameEntity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

public class SignNameAdapter extends BaseQuickAdapter<SignNameEntity, BaseViewHolder> {


    public SignNameAdapter(@Nullable List<SignNameEntity> data) {
        super(R.layout.adapter_card_list, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, SignNameEntity item) {
        ImageView imageView = helper.getView(R.id.img_card_pic);
        GlideUtils.getInstance().setImageView(mContext, item.getPicUrl(), imageView);

    }
}
