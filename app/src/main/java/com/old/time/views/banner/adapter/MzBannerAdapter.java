package com.old.time.views.banner.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by test on 2017/11/22.
 */

public class MzBannerAdapter extends BaseQuickAdapter<BookEntity, BaseViewHolder> {


    public MzBannerAdapter(@Nullable List<BookEntity> data) {
        super(R.layout.item_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookEntity item) {
        ImageView image = helper.getView(R.id.image);
        GlideUtils.getInstance().setImageView(mContext, item.getImages_large(), image);

    }
}
