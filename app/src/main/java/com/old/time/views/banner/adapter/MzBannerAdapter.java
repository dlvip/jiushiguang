package com.old.time.views.banner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.old.time.R;
import com.old.time.beans.BannerBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.views.banner.BannerLayout;

import java.util.List;

/**
 * Created by test on 2017/11/22.
 */


public class MzBannerAdapter extends RecyclerView.Adapter<MzBannerAdapter.MzViewHolder> {

    private Context context;
    private List<BannerBean> urlList;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;

    public MzBannerAdapter(Context context, List<BannerBean> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public MzBannerAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(MzBannerAdapter.MzViewHolder holder, final int position) {
        if (urlList == null || urlList.isEmpty()) return;
        final int P = position % urlList.size();
        BannerBean url = urlList.get(P);
        ImageView img = holder.imageView;
        GlideUtils.getInstance().setImageView(context, url.getPicUrl(), img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(P);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MzViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
