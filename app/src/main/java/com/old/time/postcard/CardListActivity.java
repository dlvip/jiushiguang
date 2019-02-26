package com.old.time.postcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.BaseCActivity;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DataUtils;

public class CardListActivity extends BaseCActivity {

    private String[] cardPicPath = new String[]{//
            "https://imgpub.chuangkit.com/designTemplate/2019/02/13/453094993_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/18/453672106_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/18/453672107_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/15/453375154_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/18/453671639_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/15/453320988_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/15/453320987_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/13/453094994_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/25/454702773_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/01/08/448920024_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/09/452654194_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/25/454697220_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/22/454422438_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/18/453672108_thumb"//
            , "https://imgpub.chuangkit.com/designTemplate/2019/02/15/453375153_thumb"};

    /**
     * 明信片模板列表
     *
     * @param context
     */
    public static void startCardListActivity(Context context) {
        Intent intent = new Intent(context, CardListActivity.class);
        ActivityUtils.startActivity((Activity) context, intent);

    }

    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    @Override
    protected void initView() {
        super.initView();
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_card_list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                ImageView img_card_pic = helper.getView(R.id.img_card_pic);
                GlideUtils.getInstance().setImageView(mContext, cardPicPath[helper.getLayoutPosition() % 15], img_card_pic);

            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setNewData(DataUtils.getDateStrings(15));

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
