package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.BookComEntity;
import com.old.time.beans.BookEntity;
import com.old.time.beans.UserInfoBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.StringUtils;

import java.util.List;

/**
 * Created by wcl on 2019/3/26.
 */

public class BookComAapter extends BaseQuickAdapter<BookComEntity, BaseViewHolder> {

    public BookComAapter(@Nullable List<BookComEntity> data) {
        super(R.layout.adapter_com_book, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, BookComEntity item) {
        ImageView img_user_header = helper.getView(R.id.img_user_header);
        UserInfoBean userInfoBean = item.getUserEntity();
        if (userInfoBean != null) {
            helper.setText(R.id.tv_user_name, userInfoBean.getUserName());
            GlideUtils.getInstance().setRadiusImageView(mContext, item.getUserEntity().getAvatar(), img_user_header, 10);

        }
        helper.setText(R.id.expand_text_view, item.getComment())//
                .setText(R.id.tv_create_time, StringUtils.getCreateTime(item.getCreateTime()));
        BookEntity bookEntity = item.getBookEntity();
        if (bookEntity != null) {
            helper.setText(R.id.tv_book_name, "--" + bookEntity.getTitle());

        }
    }
}
