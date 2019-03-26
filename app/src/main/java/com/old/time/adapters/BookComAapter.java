package com.old.time.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.beans.BookEntity;

import java.util.List;

/**
 * Created by wcl on 2019/3/26.
 */

public class BookComAapter extends BaseQuickAdapter<BookEntity,BaseViewHolder> {

    public BookComAapter(@Nullable List<BookEntity> data) {
        super(data);

    }

    @Override
    protected void convert(BaseViewHolder helper, BookEntity item) {

    }
}
