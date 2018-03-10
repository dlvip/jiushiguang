package com.old.time.adapters;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.beans.PicsManageBean;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.UIHelper;

import java.util.List;

/**
 * Created by NING on 2018/3/10.
 */

public class PicsManageAdapter extends BaseMultiItemQuickAdapter<PicsManageBean, BaseViewHolder> {


    private boolean isShowSelect;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PicsManageAdapter(List<PicsManageBean> data) {
        super(data);
        addItemType(PicsManageBean.TEXT, R.layout.comment_item);
        addItemType(PicsManageBean.PICS, R.layout.adapter_record_pic_video);

    }

    public void setShowSelect(boolean isShowSelect) {
        this.isShowSelect = isShowSelect;
        notifyDataSetChanged();

    }

    @Override
    protected void convert(BaseViewHolder helper, PicsManageBean item) {
        switch (item.getItemType()) {
            case PicsManageBean.TEXT:
                TextView tv_comment_user_name = helper.getView(R.id.tv_comment_user_name);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_comment_user_name.getLayoutParams();
                params.setMargins(UIHelper.dip2px(10), UIHelper.dip2px(10), UIHelper.dip2px(10), UIHelper.dip2px(10));
                tv_comment_user_name.setLayoutParams(params);
                tv_comment_user_name.setText(item.getCreatetime());
                tv_comment_user_name.setTextSize(15f);
                tv_comment_user_name.setTextColor(mContext.getResources().getColor(R.color.color_000));

                break;
            case PicsManageBean.PICS:
                helper.setVisible(R.id.img_right_btn, isShowSelect)
                        .setImageResource(R.id.img_right_btn, item.isSelected
                                ? R.drawable.radio_selected : R.drawable.radio_default);
                helper.setVisible(R.id.img_right_btn, false);
                ImageView img_record_pic = helper.getView(R.id.img_record_pic);
                GlideUtils.getInstance().setImageViewWH(mContext, item.getPickey(), img_record_pic, UIHelper.dip2px(100));

                break;
        }
    }
}
