package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.ResultBean;
import com.old.time.beans.TopicBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.UIHelper;
import com.old.time.utils.UserLocalInfoUtils;

public class CreateTopicActivity extends BaseActivity {

    /**
     * 创建话题
     *
     * @param activity
     */
    public static void startCreateTalkActivity(Activity activity) {
        if (!UserLocalInfoUtils.instance().isUserLogin()) {
            UserLoginActivity.startUserLoginActivity(activity);

            return;
        }
        Intent intent = new Intent(activity, CreateTopicActivity.class);
        ActivityUtils.startActivity(activity, intent);

    }

    private EditText edt_topic_title, edt_topic_detail;

    @Override
    protected void initView() {
        findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.right_layout_send).setVisibility(View.VISIBLE);
        setTitleText("提问");
        edt_topic_title = findViewById(R.id.edt_topic_title);
        edt_topic_detail = findViewById(R.id.edt_topic_detail);


    }

    @Override
    public void save(View view) {
        super.save(view);
        createTopic();

    }

    private ProgressDialog pd;

    /**
     * 创建话题
     */
    private void createTopic() {
        String topicTitleStr = edt_topic_title.getText().toString().trim();
        if (TextUtils.isEmpty(topicTitleStr)) {
            UIHelper.ToastMessage(mContext, "请输入问题并以问号结束");

            return;
        }
        String lastStr = topicTitleStr.substring(topicTitleStr.length() - 1);
        if (!"?".equals(lastStr) && !"？".equals(lastStr)) {
            UIHelper.ToastMessage(mContext, "请输入问题并以问号结束");

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        String topicDetail = edt_topic_detail.getText().toString().trim();
        HttpParams params = new HttpParams();
        params.put("userId", UserLocalInfoUtils.instance().getUserId());
        params.put("topic", topicTitleStr);
        params.put("pic", topicDetail);
        OkGoUtils.getInstance().postNetForData(params, Constant.INSERT_TOPIC, new JsonCallBack<ResultBean<TopicBean>>() {
            @Override
            public void onSuccess(ResultBean<TopicBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                DebugLog.d(TAG, mResultBean.msg);

            }

            @Override
            public void onError(ResultBean<TopicBean> mResultBean) {
                UIHelper.dissmissProgressDialog(pd);
                UIHelper.ToastMessage(mContext, mResultBean.msg);

            }
        });
    }

    @Override
    protected int getLayoutID() {

        return R.layout.activity_create_topic;
    }
}
