package com.old.time.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;

import java.util.List;

/**
 * Created by wcl on 2018/3/21.
 */

public class UserDressAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public UserDressAdapter(List<String> data) {
        super(R.layout.adapter_manager_address, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
