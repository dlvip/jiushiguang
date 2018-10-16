package com.old.time.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.GoodsBean;

import java.util.List;

/**
 * Created by NING on 2018/10/16.
 */

public class GoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public GoodsAdapter(@Nullable List<GoodsBean> data) {
        super(R.layout.adapter_goods, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {

    }
}
