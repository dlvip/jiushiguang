package com.old.time.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;

import java.util.List;

/**
 * Created by NING on 2018/10/10.
 */

public class EmptyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public EmptyAdapter(List<String> list) {
        super(R.layout.empty_view, list);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
