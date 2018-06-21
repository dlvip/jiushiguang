package com.old.time.adapters;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.models.AlbumModel;
import com.old.time.utils.UIHelper;

import java.util.List;

public class AlbumListAdapter extends BaseQuickAdapter<AlbumModel, BaseViewHolder> {

    private int imgWH = UIHelper.dip2px(60);

    public AlbumListAdapter(List<AlbumModel> mList) {
        super(R.layout.list__albums_item, mList);

    }


    @Override
    protected void convert(BaseViewHolder helper, AlbumModel item) {
        helper.setText(R.id.tv_album_name,item.getName() + "(" + item.getCount() + ")");
        ImageView albumImage = helper.getView(R.id.iv_album);
        GlideUtils.getInstance().setImageViewWH(mContext, item.getRecent(), albumImage, imgWH);

    }
}
