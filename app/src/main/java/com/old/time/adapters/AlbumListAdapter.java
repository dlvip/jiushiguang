package com.old.time.adapters;


import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.models.AlbumModel;
import com.old.time.models.ImageSize;

import java.util.ArrayList;
import java.util.List;

public class AlbumListAdapter extends BaseAdapter {
    private List<AlbumModel> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int imgWH;

    public AlbumListAdapter(List<AlbumModel> list, Context context) {
        if (list == null) {
            list = new ArrayList<>();
        }
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mList = list;
        imgWH = dip2px(60);
    }

    public AlbumListAdapter(Context context) {
        mList = new ArrayList<>();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        imgWH = dip2px(60);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public AlbumModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list__albums_item, parent, false);
            holder = new ViewHolder();
            holder.albumImage = (ImageView) convertView.findViewById(R.id.iv_album);
            holder.albumName = (TextView) convertView.findViewById(R.id.tv_album_name);
            holder.imageSize = new ImageSize(imgWH, imgWH);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        AlbumModel model = getItem(position);
        holder.albumName.setText(model.getName() + "(" + model.getCount() + ")");
        GlideUtils.getInstance().setImageViewWH(mContext,model.getRecent(),holder.albumImage, holder.imageSize.getWidth());
        return convertView;
    }

    class ViewHolder {
        ImageSize imageSize;
        ImageView albumImage;
        TextView albumName;
    }


    /**
     * 更新数据
     */
    public void notifyDataSetChanged(List<AlbumModel> models, boolean isRefresh) {
        if (isRefresh) {
            this.mList.clear();
        }
        if (models == null || models.size() == 0) {
            this.notifyDataSetChanged();
            return;
        }

        this.mList.addAll(models);
        this.notifyDataSetChanged();
    }

    /**
     * dip转换px
     */
    public int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mContext.getResources().getDisplayMetrics());
    }
}
