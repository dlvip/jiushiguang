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

public class HMusicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public HMusicAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_h_music, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView img_book_pic = helper.getView(R.id.img_book_pic);
        GlideUtils.getInstance().setRadiusImageView(mContext, item, img_book_pic, 5);


    }
}
