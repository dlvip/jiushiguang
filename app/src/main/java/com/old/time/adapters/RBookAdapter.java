package com.old.time.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.RBookEntity;

import java.util.List;

/**
 * Created by wcl on 2019/6/17.
 */

public class RBookAdapter extends BaseQuickAdapter<RBookEntity, BaseViewHolder> {

    public RBookAdapter(@Nullable List<RBookEntity> data) {
        super(R.layout.adapter_book, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, RBookEntity item) {

    }
}
