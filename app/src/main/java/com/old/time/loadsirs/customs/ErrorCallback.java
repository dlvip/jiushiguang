package com.old.time.loadsirs.customs;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.loadsirs.callback.Callback;

/**
 * Created by NING on 2017/12/27.
 */

public class ErrorCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.empty_view;
    }

    @Override
    protected void onViewCreate(Context context, View view) {
        super.onViewCreate(context, view);
        ImageView img_no_data = findViewById(R.id.img_no_data);
        img_no_data.setImageResource(R.mipmap.img_nonetwork);
        TextView tv_no_data = findViewById(R.id.tv_no_data);
        tv_no_data.setText("网络异常");

    }
}
