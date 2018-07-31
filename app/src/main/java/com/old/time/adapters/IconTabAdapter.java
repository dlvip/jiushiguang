package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.IconBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/7/21.
 */

public class IconTabAdapter extends BaseQuickAdapter<IconBean, BaseViewHolder> {

    public IconTabAdapter(@Nullable List<IconBean> data) {
        super(R.layout.adapter_tab_icon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IconBean item) {
        helper.setText(R.id.tv_icon_title, item.getIconTitle());
        ImageView img_icon_pic = helper.getView(R.id.img_icon_pic);
        GlideUtils.getInstance().setRoundImageView(mContext, item.getIconUrl(), img_icon_pic);

    }
}
