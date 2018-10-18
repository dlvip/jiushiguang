package com.old.time.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.GoodsBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogInputBottom;
import com.old.time.dialogs.DialogListManager;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

import java.util.List;

/**
 * Created by NING on 2018/10/16.
 */

public class GoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    public GoodsAdapter(@Nullable List<GoodsBean> data) {
        super(R.layout.adapter_goods, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final GoodsBean item) {
        ImageView img_goods_pic = helper.getView(R.id.img_goods_pic);
        GlideUtils.getInstance().setImageViewWH(mContext, item.getPicKey(), img_goods_pic, UIHelper.dip2px(150));
        helper.setText(R.id.tv_goods_title, item.getTitle())//
                .setText(R.id.tv_goods_price, item.getPrice())//
                .setText(R.id.tv_goods_go, item.getDetailStr());

        helper.getConvertView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (UserLocalInfoUtils.instance().isAdmin()) {
                    showDialogInputBottom(item);

                } else {
                    showDialogListManager(item);

                }
            }
        });
        img_goods_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private GoodsBean mGoodsBean;

    private DialogInputBottom dialogInputBottom;

    /**
     * 输入弹框
     *
     * @param mGoodsBean
     */
    private void showDialogInputBottom(final GoodsBean mGoodsBean) {
        if (mGoodsBean == null) {

            return;
        }
        this.mGoodsBean = mGoodsBean;
        if (dialogInputBottom == null) {
            dialogInputBottom = new DialogInputBottom(mContext, new OnClickManagerCallBack() {

                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    setmGoodsBeanDetailId(mGoodsBean, typeName);

                }
            });
        }
        dialogInputBottom.show();
    }


    /**
     * 设置宝贝连接id
     *
     * @param goodsBean
     */
    private void setmGoodsBeanDetailId(final GoodsBean goodsBean, String detailId) {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("goodsId", goodsBean.getGoodsId());
        params.put("detailId", detailId);
        OkGoUtils.getInstance().postNetForData(params, Constant.BASE_TEST_URL, new JsonCallBack<ResultBean<GoodsBean>>() {
            @Override
            public void onSuccess(ResultBean<GoodsBean> mResultBean) {
                int position = getParentPosition(goodsBean);
                if (position == -1) {

                    return;
                }
                setData(position, mResultBean.data);

            }

            @Override
            public void onError(ResultBean<GoodsBean> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    private DialogListManager dialogListManager;

    /**
     * 删除弹框
     *
     * @param goodsBean
     */
    private void showDialogListManager(GoodsBean goodsBean) {
        if (mGoodsBean == null) {

            return;
        }
        this.mGoodsBean = goodsBean;
        if (dialogListManager == null) {
            dialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    deleteGoodsDetail(mGoodsBean);

                }
            });
        }
        dialogListManager.setDialogViewData(mGoodsBean.getTitle(), new String[]{"删除"});
    }

    /**
     * 删除宝贝
     *
     * @param mGoodsBean
     */
    private void deleteGoodsDetail(GoodsBean mGoodsBean) {
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("goodsId", mGoodsBean.getGoodsId());
        OkGoUtils.getInstance().postNetForData(params, Constant.BASE_TEST_URL, new JsonCallBack<ResultBean>() {
            @Override
            public void onSuccess(ResultBean mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }

            @Override
            public void onError(ResultBean mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }
}
