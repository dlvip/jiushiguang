package com.old.time.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.zxing.utils.ImageFindQrUtils;
import com.old.time.R;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.BitmapUtils;
import com.old.time.utils.UIHelper;

public class DialogQRCode extends BaseDialog {

    private View.OnLongClickListener onLongClickListener;

    public DialogQRCode(@NonNull Activity context, View.OnLongClickListener onLongClickListener) {
        super(context, R.style.dialog_setting);
        this.onLongClickListener = onLongClickListener;

    }

    private LinearLayout linear_layout_parent;
    private ImageView img_code_pic;


    @Override
    protected void initDialogView() {
        linear_layout_parent = findViewById(R.id.linear_layout_parent);
        img_code_pic = findViewbyId(R.id.img_code_pic);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img_code_pic.getLayoutParams();
        params.width = UIHelper.dip2px(300);
        params.height = UIHelper.dip2px(300);
        img_code_pic.setLayoutParams(params);
        img_code_pic.setImageBitmap(ImageFindQrUtils.createQRCode("", UIHelper.dip2px(300)));
        linear_layout_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onLongClickListener != null) {
                    onLongClickListener.onLongClick(linear_layout_parent);

                }
                return false;
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_qr_code;
    }
}
