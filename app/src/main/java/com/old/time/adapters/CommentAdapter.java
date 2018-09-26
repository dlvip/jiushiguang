package com.old.time.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.CommentBean;

import java.util.List;

/**
 * Created by NING on 2018/4/16.
 */

public class CommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {


    public CommentAdapter(List<CommentBean> data) {
        super(R.layout.adapter_dynamic_comment, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {

    }
}
