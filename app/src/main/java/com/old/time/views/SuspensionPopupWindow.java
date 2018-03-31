package com.old.time.views;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.utils.UIHelper;

/**
 * Created by NING on 2018/3/31.
 */

public class SuspensionPopupWindow extends PopupWindow {

    private TextView iv_menu;

    public SuspensionPopupWindow(Context mContext, View.OnClickListener onClickListener) {
        View view = View.inflate(mContext, R.layout.popup_windiw_suspens, null);
        iv_menu = (TextView) view.findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(onClickListener);
        setContentView(view);
        setWidth(UIHelper.dip2px(48));
        setHeight(UIHelper.dip2px(48));
        setFocusable(false);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable());

    }

    /**
     * 设置文本内容
     */
    public void showAtLocation(String typeStr, View parent, int gravity, int x, int y) {
        showAtLocation(parent, gravity, x, y);
        if (iv_menu == null || TextUtils.isEmpty(typeStr)) {

            return;
        }
        iv_menu.setText(typeStr);
    }
    /**
     * 设置文本内容
     */
    public void showAtLocationXY(View parent, int gravity, int x, int y) {
        showAtLocation(parent, gravity, x, y);

    }
}
