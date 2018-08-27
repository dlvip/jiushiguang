package com.old.time.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.old.time.R;

/**
 * Created by diliang on 2017/4/27.
 */
public class CustomNetView extends LinearLayout {

    public static final int NO_DATA = 0;
    public static final int NET_ERREY = 1;
    private int status;
    private TextView tv_no_data;
    private ImageView img_no_data;

    public CustomNetView(Context context, int status) {
        super(context);
        this.status = status;
        initView();

    }

    public CustomNetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public CustomNetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.empty_view, this);
        img_no_data = view.findViewById(R.id.img_no_data);
        tv_no_data = view.findViewById(R.id.tv_no_data);
        setDataForView(status);
    }

    /**
     * 设置显示内容
     *
     * @param status
     */
    public void setDataForView(int status) {
        if (status == NO_DATA) {
            setDataForView(R.string.loading_show_empty, R.mipmap.img_nodata);

        } else {
            setDataForView(R.string.error_net, R.mipmap.img_nonetwork);

        }
    }

    /**
     * 设置显示内容
     *
     * @param strRes 文字描述资源id
     * @param imgRes 图片资源id
     */
    public void setDataForView(int strRes, int imgRes) {
        if (tv_no_data == null || img_no_data == null) {

            return;
        }
        tv_no_data.setText(strRes);
        img_no_data.setImageResource(imgRes);
    }

    public void setDataForView(String textStr, int imgRes) {
        if (tv_no_data == null || img_no_data == null) {

            return;
        }
        tv_no_data.setText(textStr);
        img_no_data.setImageResource(imgRes);
    }
}
