package com.old.time.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;

import java.util.ArrayList;

/**
 * Created by NING on 2018/10/10.
 */

public class EmptyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public EmptyAdapter() {
        super(R.layout.empty_view, new ArrayList<String>());

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
