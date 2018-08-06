package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.BannerBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/8/6.
 */

public class MzBannerAdapter extends BaseQuickAdapter<BannerBean, BaseViewHolder> {


    public MzBannerAdapter(@Nullable List<BannerBean> data) {
        super(R.layout.item_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BannerBean item) {
        ImageView img = helper.getView(R.id.image);
        GlideUtils.getInstance().setImageView(mContext, item.getPicUrl(), img);
    }
}
