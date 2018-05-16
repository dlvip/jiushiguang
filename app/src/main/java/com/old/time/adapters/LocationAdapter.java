package com.old.time.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.PoiItemBean;

import java.util.List;

/**
 * Created by NING on 2018/4/17.
 */

public class LocationAdapter extends BaseQuickAdapter<PoiItemBean, BaseViewHolder> {

    public LocationAdapter(List<PoiItemBean> data) {
        super(R.layout.adapter_location, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItemBean item) {
        String locationStr = item.getCityName() + item.getProvinceName() + item.getBusinessArea();
        helper.setText(R.id.tv_location, item.getTitle())
                .setText(R.id.tv__detail_location, locationStr)
                .setImageResource(R.id.img_select, item.isSelect
                        ? R.mipmap.btn_selected : R.mipmap.icon_data_select_no);

    }
}
