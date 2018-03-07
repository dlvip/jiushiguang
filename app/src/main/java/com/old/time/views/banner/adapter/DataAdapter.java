package com.old.time.views.banner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.old.time.R;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private int[] images = {R.drawable.beauty1, R.drawable.beauty2, R.drawable.beauty3,
            R.drawable.beauty4, R.drawable.beauty5, R.drawable.beauty6, R.drawable.beauty7,
            R.drawable.beauty8, R.drawable.beauty9, R.drawable.beauty10};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_over, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]);
        holder.imageView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "点击了" + v.getTag(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
