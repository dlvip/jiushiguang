package com.old.time.dialogs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.beans.VideoBean;
import com.old.time.utils.ScreenTools;

/**
 * @ClassName: wcLiang
 * @Description:${TODO}
 * @author: 龙杯科技
 * @date: ${date} ${time}
 * <p>
 * ${tags}
 * @Copyright: ${year} www.longbei.com
 * 注意：本内容仅限于北京龙杯信息技术有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class DialogVideoDetail extends BaseDialog {
    public DialogVideoDetail(@NonNull Activity context) {
        super(context, R.style.transparentFrameWindowStyle);

    }

    private RelativeLayout relative_layout_parent;
    private TextView tv_video_name, tv_video_detail;

    @Override
    protected void initDialogView() {
        relative_layout_parent = findViewbyId(R.id.relative_layout_parent);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relative_layout_parent.getLayoutParams();
        params.height = ScreenTools.instance(mContext).getScreenHeight() - ScreenTools.instance(mContext).getScreenWidth() * 618 / 1000;
        relative_layout_parent.setLayoutParams(params);
        tv_video_name = findViewbyId(R.id.tv_video_name);
        tv_video_detail = findViewbyId(R.id.tv_video_detail);
        findViewbyId(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
    }

    public void showDialog(VideoBean videoBean) {
        if (videoBean == null) {

            return;
        }
        tv_video_name.setText(videoBean.getName());
        tv_video_detail.setText(videoBean.getDetail());
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_video_detail;
    }
}
