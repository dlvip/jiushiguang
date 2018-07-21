package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.MusicActivity;
import com.old.time.glideUtils.GlideUtils;

import java.util.List;

/**
 * Created by wcl on 2018/7/21.
 */

public class HMusicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private String picUrl = "http://js010.speiyou.com//upload/010/teacher/ff8080813366d9e8013367ac591e0589/big.jpg";

    public HMusicAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_h_music, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView img_book_pic = helper.getView(R.id.img_book_pic);
        GlideUtils.getInstance().setRadiusImageView(mContext, picUrl, img_book_pic, 5);

        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicActivity.startMusicActivity(mContext);

            }
        });
    }
}
