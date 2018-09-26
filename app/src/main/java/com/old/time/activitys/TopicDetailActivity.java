package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.adapters.CommentAdapter;
import com.old.time.beans.CommentBean;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.dialogs.DialogInputBottom;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;
import com.old.time.views.CustomNetView;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends CBaseActivity {

    public static final String TOPIC_BEAN = "TopicBean";

    /**
     * 话题详情
     *
     * @param mContext
     */
    public static void startTopicDetailActivity(Activity mContext, TopicBean mTopicBean) {
        Intent intent = new Intent(mContext, TopicDetailActivity.class);
        intent.putExtra(TOPIC_BEAN, mTopicBean);
        ActivityUtils.startActivity(mContext, intent);

    }

    private List<CommentBean> commentBeans = new ArrayList<>();
    private CommentAdapter adapter;
    private TopicBean mTopicBean;

    private CustomNetView mCustomNetView;

    private View headerView;

    @Override
    protected void initView() {
        this.mTopicBean = (TopicBean) getIntent().getSerializableExtra(TOPIC_BEAN);
        super.initView();
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        headerView = View.inflate(mContext, R.layout.header_topic_detail, null);
        adapter = new CommentAdapter(commentBeans);
        adapter.removeAllHeaderView();
        adapter.addHeaderView(headerView);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext));
        mRecyclerView.setAdapter(adapter);
        linear_layout_more.setVisibility(View.VISIBLE);
        linear_layout_more.removeAllViews();
        View bottomView = View.inflate(mContext, R.layout.comment_bottom_view, null);
        linear_layout_more.addView(bottomView);
        linear_layout_more.setLayoutParams(layoutParams);

        mCustomNetView = new CustomNetView(mContext, CustomNetView.NO_DATA);
        adapter.setHeaderAndEmpty(true);
        setHeaderView(mTopicBean);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        linear_layout_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();

            }
        });
    }

    private DialogInputBottom dialogInputBottom;

    /**
     * 显示评论弹框
     */
    private void showCommentDialog() {
        if (dialogInputBottom == null) {
            dialogInputBottom = new DialogInputBottom(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {
                    submitComment(typeName);

                }
            });
        }
        dialogInputBottom.showDialog(R.string.input_content, R.string.dialog_true);

    }

    private ProgressDialog pd;

    /**
     * 提交评论
     *
     * @param comment
     */
    private void submitComment(String comment) {
        if (TextUtils.isEmpty(comment)) {

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("topicId", mTopicBean.getTopicId());
        params.put("comment", comment);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_INSERT_COMMENT, new JsonCallBack<ResultBean<CommentBean>>() {
            @Override
            public void onSuccess(ResultBean<CommentBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);

            }

            @Override
            public void onError(ResultBean<CommentBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);

            }
        });
    }

    private TextView tv_topic_title, tv_topic_detail, tv_topic_count;

    /**
     * 设置头部数据
     */
    private void setHeaderView(TopicBean mTopicBean) {
        if (headerView == null || mTopicBean == null) {

            return;
        }
        if (tv_topic_title == null) {
            tv_topic_title = headerView.findViewById(R.id.tv_topic_title);
            tv_topic_detail = headerView.findViewById(R.id.tv_topic_detail);
            tv_topic_count = headerView.findViewById(R.id.tv_topic_count);

        }
        tv_topic_title.setText(mTopicBean.getTopicTitle());
        tv_topic_detail.setText(mTopicBean.getTopicContent());
        tv_topic_count.setText(mTopicBean.getTopicCount());

    }

    private int pageNum = 0;

    @Override
    public void getDataFromNet(final boolean isRefresh) {
        if (isRefresh) {
            pageNum = 0;

        } else {
            pageNum++;

        }
        HttpParams params = new HttpParams();
        params.put("topicId", mTopicBean.getTopicId());
        params.put("pageNum", pageNum);
        params.put("pageSize", Constant.PageSize);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_COMMENT_LIST, new JsonCallBack<ResultBean<List<CommentBean>>>() {
            @Override
            public void onSuccess(ResultBean<List<CommentBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    commentBeans.clear();
                    adapter.setNewData(commentBeans);

                }
                if (mResultBean.data.size() < Constant.PageSize) {
                    adapter.loadMoreEnd();

                } else {
                    adapter.loadMoreComplete();

                }
                adapter.addData(mResultBean.data);
                if (adapter.getItemCount() - adapter.getHeaderLayoutCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NO_DATA);
                    adapter.setEmptyView(mCustomNetView);

                }
            }

            @Override
            public void onError(ResultBean<List<CommentBean>> mResultBean) {
                mSwipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(mContext, mResultBean.msg);
                if (adapter.getItemCount() == 0) {
                    mCustomNetView.setDataForView(CustomNetView.NET_ERREY);
                    adapter.setEmptyView(mCustomNetView);

                } else {
                    adapter.loadMoreFail();

                }
            }
        });
    }
}
