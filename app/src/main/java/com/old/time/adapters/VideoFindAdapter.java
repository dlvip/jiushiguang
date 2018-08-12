package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dueeeke.videoplayer.demo.VideoBean;
import com.old.time.R;
import com.old.time.activitys.VideoPagerActivity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/8/12.
 */

public class VideoFindAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {

    public VideoFindAdapter(@Nullable List<VideoBean> data) {
        super(R.layout.adapter_find_video, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, VideoBean item) {
        ImageView img_find_video = helper.getView(R.id.img_find_video);
        GlideUtils.getInstance().setImageView(mContext, item.getThumb(), img_find_video);

        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = helper.getLayoutPosition();
                VideoPagerActivity.startVideoPagerActivity(mContext, position);

            }
        });
    }
}
