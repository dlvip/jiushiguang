package com.old.time.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;

import java.util.List;

/**
 * Created by NING on 2018/3/28.
 */

public class UserOrderAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public UserOrderAdapter(List<String> data) {
        super(R.layout.adapter_user_order, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
