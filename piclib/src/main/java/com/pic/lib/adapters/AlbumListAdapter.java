package com.pic.lib.adapters;


import android.widget.ImageView;

import com.pic.lib.utils.ScreenTools;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pic.lib.R;
import com.pic.lib.glideUtils.GlideUtils;
import com.pic.lib.models.AlbumModel;

import java.util.List;

public class AlbumListAdapter extends BaseQuickAdapter<AlbumModel, BaseViewHolder> {

    public AlbumListAdapter(List<AlbumModel> mList) {
        super(R.layout.piclib_list__albums_item, mList);

    }

    private int imgWH;

    @Override
    protected void convert(BaseViewHolder helper, AlbumModel item) {
        helper.setText(R.id.tv_album_name, item.getName() + "(" + item.getCount() + ")");
        ImageView albumImage = helper.getView(R.id.iv_album);
        if (imgWH == 0) {
            imgWH = ScreenTools.dip2px(mContext, 60);

        }
        GlideUtils.getInstance().setImageViewWH(mContext, item.getRecent(), albumImage, imgWH);

    }
}
