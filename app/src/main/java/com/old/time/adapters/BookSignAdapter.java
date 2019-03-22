package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

public class BookSignAdapter extends BaseQuickAdapter<BookEntity, BaseViewHolder> {

    public BookSignAdapter(@Nullable List<BookEntity> data) {
        super(R.layout.adapter_topic, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, BookEntity item) {
        ImageView img_topic_bg = helper.getView(R.id.img_topic_bg);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) img_topic_bg.getLayoutParams();
        params.dimensionRatio = "13:20";
        img_topic_bg.setLayoutParams(params);
        img_topic_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtils.getInstance().setImageView(mContext, item.getImages_medium(), img_topic_bg);
        helper.setVisible(R.id.tv_topic_title, false);

    }
}
