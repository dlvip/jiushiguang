package com.old.time.rongIM;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.activitys.TopicDetailCActivity;
import com.old.time.activitys.VideoDetailActivity;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.beans.VideoBean;
import com.old.time.constants.Constant;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.UIHelper;

public class ConversationActivity extends BaseActivity {

    private String targetId;
    private View relative_layout_big;
    private ImageView img_big_pic, img_close;

    private View relative_layout_small;
    private ImageView img_small_pic;

    @Override
    protected void initView() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            targetId = uri.getQueryParameter("targetId");

        }
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        relative_layout_big = findViewById(R.id.relative_layout_big);
        img_big_pic = findViewById(R.id.img_big_pic);
        img_close = findViewById(R.id.img_close);

        relative_layout_small = findViewById(R.id.relative_layout_small);
        img_small_pic = findViewById(R.id.img_small_pic);

        getCartRoomInfo();
        getVideoDetail();
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
    protected void initEvent() {
        super.initEvent();
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative_layout_big.setVisibility(View.GONE);
                relative_layout_small.setVisibility(View.VISIBLE);

            }
        });
        relative_layout_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoDetailActivity.startVideoDetailActivity(mContext, targetId);

            }
        });
        relative_layout_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoDetailActivity.startVideoDetailActivity(mContext, targetId);

            }
        });
    }

    /**
     * 获取视频信息
     */
    private void getVideoDetail() {
        if (TextUtils.isEmpty(targetId)) {

            return;
        }
        HttpParams params = new HttpParams();
        params.put("type", 0);//0:话题id，1：视频id，2：图书id
        params.put("videoId", targetId);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_VIDEO_DETAIL, new JsonCallBack<ResultBean<VideoBean>>() {
            @Override
            public void onSuccess(ResultBean<VideoBean> mResultBean) {
                if (mResultBean == null) {

                    return;
                }
                setDataForVideoView(mResultBean.data);

            }

            @Override
            public void onError(ResultBean<VideoBean> mResultBean) {


            }
        });
    }

    /**
     * 设置视频
     */
    private void setDataForVideoView(VideoBean videoView) {
        if (videoView == null) {

            return;
        }
        relative_layout_big.setVisibility(View.VISIBLE);
        relative_layout_small.setVisibility(View.GONE);
        GlideUtils.getInstance().setImageView(mContext, videoView.getPic(), img_big_pic);
        GlideUtils.getInstance().setImageView(mContext, videoView.getPic(), img_small_pic);

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
