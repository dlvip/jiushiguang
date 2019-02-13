package com.old.time.postcard;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.FastMailBean;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

public class FastMailAdapter extends BaseQuickAdapter<FastMailBean, BaseViewHolder> {

    public FastMailAdapter(@Nullable List<FastMailBean> data) {
        super(R.layout.adapter_fast_mail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FastMailBean item) {
        ImageView imageView = helper.getView(R.id.img_mail_pic);
        GlideUtils.getInstance().setImageView(mContext, item.getIcon(), imageView);
        helper.setText(R.id.tv_mail_name, item.getName());
    }
}
