package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2019/3/23.
 */

public class BooksAdapter extends BaseQuickAdapter<BookEntity, BaseViewHolder> {

    public BooksAdapter(@Nullable List<BookEntity> data) {
        super(R.layout.adapter_book, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, BookEntity item) {
        ImageView img_book_pic = helper.getView(R.id.img_book_pic);
        GlideUtils.getInstance().setImageView(mContext, item.getImages_large(), img_book_pic);
        helper.setText(R.id.tv_book_name, item.getTitle())//
                .setText(R.id.tv_book_author, item.getAuthor() + " / " + item.getBinding())//
                .setText(R.id.tv_book_describe, item.getSummary())//
                .setText(R.id.tv_book_leve, item.getLevelNum());

    }
}
