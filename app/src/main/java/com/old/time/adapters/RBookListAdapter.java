package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.RBookEntity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2019/6/22.
 */

public class RBookListAdapter extends BaseQuickAdapter<RBookEntity, BaseViewHolder> {

    public RBookListAdapter(@Nullable List<RBookEntity> data) {
        super(R.layout.header_recycler_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RBookEntity item) {
        ImageView img_book_pic = helper.getView(R.id.img_book_pic);
        GlideUtils.getInstance().setImageView(mContext, item.getImages_large(), img_book_pic);
        helper.setText(R.id.tv_book_name, item.getTitle())//
                .setText(R.id.tv_book_describe, item.getDes_cribe())//
                .setText(R.id.tv_book_author, item.getAuthor());


    }
}
