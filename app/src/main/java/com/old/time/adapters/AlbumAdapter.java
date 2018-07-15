package com.old.time.adapters;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.AlbumBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;

import java.util.List;

/**
 * Created by wcl on 2018/7/14.
 */

public class AlbumAdapter extends BaseQuickAdapter<AlbumBean, BaseViewHolder> {

    private int width;

    public AlbumAdapter(List<AlbumBean> data) {
        super(R.layout.adapter_home, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumBean item) {
        if (width == 0) {
            width = ScreenTools.instance(mContext).getScreenWidth();

        }
        if (helper.getLayoutPosition() % 2 != 0) {
            helper.getConvertView().setPadding(0, 0, UIHelper.dip2px(5), 0);

        } else {
            helper.getConvertView().setPadding(UIHelper.dip2px(5), 0, 0, 0);

        }
        helper.setText(R.id.tv_photo_name, item.getTitle()).setText(R.id.tv_photo_money, item.getPrice());
        ImageView img_phone_pic = helper.getView(R.id.img_phone_pic);
        GlideUtils.getInstance().setImageViewWH(mContext, item.getPicUrl(), img_phone_pic, width / 2);
    }
}
