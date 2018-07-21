package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/7/21.
 */

public class IconTabAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public static String[] picStrs = {"http://o.longbeidata.com/classroomBanner/3c912f2e-89a5-4570-a4eb-7731727f2522"
            , "http://o.longbeidata.com/classroomBanner/91ca9a93-bac2-4f6f-b84e-f2d5a46aa74c"
            , "http://o.longbeidata.com/classroomBanner/e1faedda-c1ab-4693-afdd-b99be469deea"
            , "http://o.longbeidata.com/classroomBanner/5acf1524-8a53-43cd-9ea8-3b213fc4379d"
            , "http://o.longbeidata.com/classroomBanner/31c3e0e4-1c67-4a63-beb4-31e19c64c9be"};

    public IconTabAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_tab_icon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView img_icon_pic = helper.getView(R.id.img_icon_pic);
        GlideUtils.getInstance().setRoundImageView(mContext, picStrs[helper.getLayoutPosition()], img_icon_pic);
    }
}
