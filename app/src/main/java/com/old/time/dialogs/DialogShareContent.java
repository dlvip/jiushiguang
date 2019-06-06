package com.old.time.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.old.time.R;
import com.old.time.beans.UMShareBean;
import com.old.time.utils.BitmapUtils;
import com.old.time.utils.MyGridLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcliang on 2019/6/6.
 */

public class DialogShareContent extends BaseDialog {

    private ShareModelCallBackListener shareModelCallBackListener;

    /**
     * 分享弹框
     *
     * @param context
     * @param shareModelCallBackListener
     */
    public DialogShareContent(@NonNull Activity context, ShareModelCallBackListener shareModelCallBackListener) {
        super(context, R.style.transparentFrameWindowStyle);
        this.shareModelCallBackListener = shareModelCallBackListener;

    }

    private TextView tv_dialog_title, tv_cancel;
    private RecyclerView recycler_view;

    private List<ItemBean> itemBeans;
    private BaseQuickAdapter<ItemBean, BaseViewHolder> adapter;

    @Override
    protected void initDialogView() {
        tv_dialog_title = findViewbyId(R.id.tv_dialog_title);
        tv_dialog_title.setText("分享");
        tv_cancel = findViewbyId(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        recycler_view = findViewbyId(R.id.recycler_view);
        recycler_view.setLayoutManager(new MyGridLayoutManager(mContext, 3));
        recycler_view.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.HORIZONTAL_LIST));
        recycler_view.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10, R.color.transparent));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recycler_view.getLayoutParams();
        params.height = UIHelper.dip2px(170);
        recycler_view.setLayoutParams(params);
        adapter = new BaseQuickAdapter<ItemBean, BaseViewHolder>(R.layout.share_menu_item, itemBeans) {

            @Override
            protected void convert(BaseViewHolder helper, ItemBean item) {
                helper.setText(R.id.socialize_text_view, item.name)//
                        .setImageResource(R.id.socialize_image_view, item.imgRes);

            }
        };
        recycler_view.setAdapter(adapter);
        itemBeans = new ArrayList<>();
        itemBeans.clear();
        itemBeans.add(new ItemBean("微信", R.drawable.umeng_socialize_wechat, SHARE_MEDIA.WEIXIN));
        itemBeans.add(new ItemBean("朋友圈", R.drawable.umeng_socialize_wxcircle, SHARE_MEDIA.WEIXIN_CIRCLE));
        itemBeans.add(new ItemBean("qq", R.drawable.umeng_socialize_qq, SHARE_MEDIA.QQ));
        itemBeans.add(new ItemBean("空间", R.drawable.umeng_socialize_qzone, SHARE_MEDIA.QZONE));
        itemBeans.add(new ItemBean("微博", R.drawable.umeng_socialize_sina, SHARE_MEDIA.SINA));
        itemBeans.add(new ItemBean("更多", R.mipmap.btn_more, null));
        adapter.setNewData(itemBeans);

        tv_dialog_title.setText("分享");
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
        recycler_view.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                dismiss();

                ItemBean itemBean = itemBeans.get(position);
                if (itemBean.platform != null) {
                    shareMorePlatform(itemBean.platform);

                } else {
                    showMoreShare();

                }
            }
        });
    }

    public void showShareContentDialog(View mAtLocationView) {
        if (!isShowing()) {
            show();

        }
        this.mAtLocationView = mAtLocationView;

    }

    private View mAtLocationView;

    private ShareAction mShareAction;

    /**
     * 分享到更多平台
     */
    private void shareMorePlatform(SHARE_MEDIA platform) {
        if (platform == SHARE_MEDIA.SINA) {

            return;
        }
        if (shareModelCallBackListener == null || mAtLocationView == null) {

            return;
        }
        UMShareBean umShareBean = shareModelCallBackListener.getShareModel();
        if (umShareBean == null) {

            return;
        }
        if (mShareAction == null) {
            mShareAction = new ShareAction(mContext);
            mShareAction.setCallback(umShareListener);

        }
        mShareAction.setPlatform(platform);
        UMImage image;
        if (TextUtils.isEmpty(umShareBean.getImgUrl())) {
            Bitmap bitmap = BitmapUtils.createBitmapFromView(mAtLocationView);
            image = new UMImage(mContext, bitmap);

        } else {
            image = new UMImage(mContext, umShareBean.getImgUrl());

        }
        if (!TextUtils.isEmpty(umShareBean.getShareUrl())) {
            UMWeb web = new UMWeb(umShareBean.getShareUrl());
            web.setTitle(umShareBean.getTitle());//标题
            web.setThumb(image);//缩略图
            web.setDescription(umShareBean.getDescription());//描述
            mShareAction.withMedia(web);

        } else {
            mShareAction.withMedia(image);

        }
        mShareAction.share();
    }

    /**
     * 更多分享
     */
    private void showMoreShare() {
        if (mAtLocationView == null) {

            return;
        }
        Uri uri = BitmapUtils.saveBitmap(mAtLocationView);
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/*");
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        mContext.startActivity(Intent.createChooser(imageIntent, "分享"));

    }

    /**
     * 获取分享model
     */
    public interface ShareModelCallBackListener {
        UMShareBean getShareModel();

    }

    /**
     * 分享回调监听
     */
    private UMShareListener umShareListener = new UMShareListener() {


        /**
         * @param platform 平台类型
         * @descrption 分享开始的回调
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @param platform 平台类型
         * @descrption 分享成功的回调
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            UIHelper.ToastMessage(mContext, "分享成功");

        }

        /**
         * @param platform 平台类型
         * @param t        错误原因
         * @descrption 分享失败的回调
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            UIHelper.ToastMessage(mContext, "分享失败：error【" + t.getMessage() + "】");

        }

        /**
         * @param platform 平台类型
         * @descrption 分享取消的回调
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            UIHelper.ToastMessage(mContext, "分享取消");

        }
    };

    private class ItemBean {

        ItemBean(String name, int imgRes, SHARE_MEDIA platform) {
            this.name = name;
            this.imgRes = imgRes;
            this.platform = platform;

        }

        /**
         * 名称
         */
        private String name;

        /**
         * 图片
         */
        private int imgRes;

        /**
         * 分享平台
         */
        private SHARE_MEDIA platform;

    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_manager_list;
    }
}
