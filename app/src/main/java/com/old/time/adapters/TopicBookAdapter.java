package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.BookEntity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2019/3/26.
 */

public class TopicBookAdapter extends BaseQuickAdapter<BookEntity, BaseViewHolder> {

    public TopicBookAdapter(@Nullable List<BookEntity> data) {
        super(R.layout.adapter_topic_book, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookEntity item) {
        ImageView img_topic_bg = helper.getView(R.id.img_topic_bg);
        GlideUtils.getInstance().setImageView(mContext, item.getImages_medium(), img_topic_bg);
        helper.setText(R.id.tv_topic_title, item.getTitle());

    }
}
