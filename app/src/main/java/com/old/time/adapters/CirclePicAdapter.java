package com.old.time.adapters;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by NING on 2018/3/10.
 */

public class CirclePicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public CirclePicAdapter(List<String> data) {
        super(R.layout.adapter_pic, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView img_phone_pic = helper.getView(R.id.img_phone_pic);
        GlideUtils.getInstance().setImageView(mContext, item, img_phone_pic);

    }
}
