package com.old.time.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;

import java.util.List;

/**
 * Created by wcl on 2018/3/9.
 */

public class CircleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CircleAdapter(List<String> data) {
        super(R.layout.adapter_circle, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {


    }
}
