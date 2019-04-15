package com.old.time.rongIM;

import android.net.Uri;
import android.view.View;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.activitys.TopicDetailCActivity;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.UIHelper;

public class ConversationActivity extends BaseActivity {

    private String targetId;

    @Override
    protected void initView() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            targetId = uri.getQueryParameter("targetId");

        }
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        getCartRoomInfo();
    }

    /**
     * 获取聊天室
     */
    private void getCartRoomInfo() {
        HttpParams params = new HttpParams();
        params.put("topicId", targetId);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_TOPIC_DETAIL, new JsonCallBack<ResultBean<TopicBean>>() {
            @Override
            public void onSuccess(final ResultBean<TopicBean> mResultBean) {
                if (mResultBean == null || mResultBean.data == null) {

                    return;
                }
                setDataForView(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<TopicBean> mResultBean) {
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    private TopicBean topicBean;

    /**
     * 设置聊天信息
     *
     * @param topicBean
     */
    private void setDataForView(TopicBean topicBean) {
        if (topicBean == null) {

            return;
        }
        this.topicBean = topicBean;
        setTitleText(topicBean.getTopic());
        setSendText("##");
        setSendTextColor(R.color.color_2e6cd3);

    }

    @Override
    public void save(View view) {
        super.save(view);
        if (topicBean == null) {

            return;
        }
        TopicDetailCActivity.startTopicDetailActivity(mContext, topicBean);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_conversation;
    }
}
