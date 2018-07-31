package com.old.time.fragments;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.R;
import com.old.time.activitys.DynamicActivity;
import com.old.time.activitys.VideoDetailActivity;
import com.old.time.beans.EventBean;
import com.old.time.beans.IconBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/3/5.
 */

public class FindFragment extends CBaseFragment {

    private BaseQuickAdapter<EventBean, BaseViewHolder> mAdapter;
    private List<EventBean> eventBeans;

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        eventBeans = new ArrayList<>();
        mAdapter = new BaseQuickAdapter<EventBean, BaseViewHolder>(R.layout.adapter_find_type_pic, eventBeans) {
            @Override
            protected void convert(BaseViewHolder helper, EventBean item) {
                helper.setText(R.id.tv_event_title, item.getTitle())
                        .setText(R.id.tv_event_price, "￥ " + item.getPrice())
                        .setText(R.id.tv_join_count, item.getJoinCount() + " 人参与");
                ImageView img_event_pic = helper.getView(R.id.img_event_pic);
                GlideUtils.getInstance().setImageView(mContext, item.getPicUrl(), img_event_pic);

            }
        };
        View headerView = View.inflate(mContext, R.layout.header_find, null);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DynamicActivity.startDynamicActivity(mContext, "");

            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoDetailActivity.startVideoDetailActivity(mContext);

            }
        });
    }

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        OkGoUtils.getInstance().postNetForData(Constant.GET_EVENT_LIST, new JsonCallBack<ResultBean<List<EventBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<EventBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    eventBeans.clear();
                    mAdapter.setNewData(eventBeans);

                }
                if (mResultBean.status == Constant.STATUS_FRIEND_00) {
                    eventBeans.addAll(mResultBean.data);
                    mAdapter.setNewData(eventBeans);

                } else {
                    UIHelper.ToastMessage(mContext, mResultBean.msg);

                }
            }

            @Override
            public void onError(ResultBean<List<EventBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });

    }
}
