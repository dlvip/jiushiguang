package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.TabBooksActivity;
import com.old.time.beans.TabEntity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2019/6/22.
 */

public class BookMallAdapter extends BaseQuickAdapter<TabEntity, BaseViewHolder> {

    public BookMallAdapter(@Nullable List<TabEntity> data) {
        super(R.layout.adapter_book_mall, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final TabEntity item) {
        ImageView img_mall_pic = helper.getView(R.id.img_mall_pic);
        GlideUtils.getInstance().setImageView(mContext, item.getPic(), img_mall_pic);
        helper.setText(R.id.tv_mall_title, item.getName());
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabBooksActivity.startTabBooksActivity(mContext, item);

            }
        });
    }
}
