package com.old.time.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.WebViewActivity;
import com.old.time.beans.ArticleBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;

import java.util.List;

/**
 * Created by wcl on 2018/7/14.
 */

public class ArticleAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {

    private String[] strings = new String[]{"爱智康", "教育指导", "家长帮", "学而思"};

    public ArticleAdapter(List<ArticleBean> data) {
        super(R.layout.adapter_article, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final ArticleBean item) {
        helper.setText(R.id.tv_article_title, item.getTitle())//
                .setText(R.id.tv_source, strings[helper.getLayoutPosition() % 4])//
                .setText(R.id.tv_look_count, item.getLookCount());

        ImageView img_article_pic = helper.getView(R.id.img_article_pic);
        GlideUtils.getInstance().setImageView(mContext, item.getPicUrl(), img_article_pic);

        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.startWebViewActivity((Activity) mContext, item.getDetailUrl());
                updateArticleReadCount(item.getId());

            }
        });
    }

    /**
     * 修改文章略读量
     *
     * @param articleId
     */
    private void updateArticleReadCount(String articleId) {
        HttpParams params = new HttpParams();
        params.put("articleId", articleId);
        OkGoUtils.getInstance().postNetForData(params, Constant.UPDATE_ARTICLE_READ_COUNT, new JsonCallBack<ResultBean>() {
            @Override
            public void onSuccess(ResultBean mResultBean) {

            }

            @Override
            public void onError(ResultBean mResultBean) {

            }
        });

    }
}
