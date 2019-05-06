package com.old.time.mall;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.beans.BookEntity;

import java.util.ArrayList;

public class MallBookAdapter extends BaseQuickAdapter<BookEntity,BaseViewHolder> {

    public MallBookAdapter() {
        super(new ArrayList<BookEntity>());

    }

    @Override
    protected void convert(BaseViewHolder helper, BookEntity item) {

    }
}
