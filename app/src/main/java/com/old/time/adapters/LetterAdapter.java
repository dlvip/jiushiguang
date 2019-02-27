package com.old.time.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.PostCartBean;

import java.util.List;

public class LetterAdapter extends BaseQuickAdapter<PostCartBean, BaseViewHolder> {

    public LetterAdapter(@Nullable List<PostCartBean> data) {
        super(R.layout.item_text, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, PostCartBean item) {
        helper.setText(R.id.tv_item_text, item.getCodeKey());

    }
}
