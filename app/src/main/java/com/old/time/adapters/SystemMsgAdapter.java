package com.old.time.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;

import java.util.List;

/**
 * Created by NING on 2018/3/28.
 */

public class SystemMsgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SystemMsgAdapter(List<String> data) {
        super(R.layout.adapter_msg, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
