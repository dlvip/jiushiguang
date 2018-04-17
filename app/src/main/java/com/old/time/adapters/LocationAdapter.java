package com.old.time.adapters;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;

import java.util.List;

/**
 * Created by NING on 2018/4/17.
 */

public class LocationAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {

    public LocationAdapter(List<PoiItem> data) {
        super(R.layout.adapter_location, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItem item) {
        helper.setText(R.id.tv_location, item.getTitle())
                .setText(R.id.tv__detail_location, item.getCityName() + item.getProvinceName() + item.getBusinessArea());


    }
}
