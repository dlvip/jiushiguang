package com.dueeeke.dkplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dueeeke.dkplayer.beans.VideoBean;
import com.dueeeke.dkplayer.player.IjkVideoView;
import com.dueeeke.dkplayer.player.PlayerConfig;
import com.dueeeke.dkplayer.ui.StandardVideoController;

import java.util.List;

import earnmoney.download.com.dkplayer.R;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.VideoHolder> {


    private List<VideoBean> videos;
    private Context context;

    public VideoRecyclerViewAdapter(List<VideoBean> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_video_auto_play, parent, false);
        return new VideoHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, int position) {
        VideoBean videoBean = videos.get(position);
        Glide.with(context)
                .load(videoBean.getThumb())
                .into(holder.controller.getThumb());
        holder.ijkVideoView.setPlayerConfig(holder.mPlayerConfig);
        holder.ijkVideoView.setUrl(videoBean.getUrl());
        holder.ijkVideoView.setTitle(videoBean.getTitle());
        holder.ijkVideoView.setVideoController(holder.controller);
        holder.title.setText(videoBean.getTitle());
//            holder.ijkVideoView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        private IjkVideoView ijkVideoView;
        private StandardVideoController controller;
        private TextView title;
        private PlayerConfig mPlayerConfig;

        VideoHolder(View itemView) {
            super(itemView);
            ijkVideoView = itemView.findViewById(R.id.video_player);
            int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            ijkVideoView.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, widthPixels * 9 / 16 + 1));
            controller = new StandardVideoController(context);
            title = itemView.findViewById(R.id.tv_title);
            mPlayerConfig = new PlayerConfig.Builder()
                    .enableCache()
                    .autoRotate()
                    .addToPlayerManager()//required
//                        .savingProgress()
                    .build();
        }
    }
}