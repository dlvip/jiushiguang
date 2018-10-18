package com.old.time.activitys;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.model.HttpParams;
import com.old.time.R;
import com.old.time.beans.CourseBean;
import com.old.time.beans.CreateBean;
import com.old.time.beans.ResultBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.StringUtils;
import com.old.time.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateCActivity extends BaseCActivity {

    public static void startCreateActivity(Activity activity) {
        Intent intent = new Intent(activity, CreateCActivity.class);
        ActivityUtils.startActivity(activity, intent);

    }

    private List<CreateBean> createBeans = new ArrayList<>();
    private BaseQuickAdapter<CreateBean, BaseViewHolder> adapter;

    @Override
    protected void initView() {
        super.initView();
        createBeans.clear();
        createBeans.add(CreateBean.getInstance("添加活动", CreateActionActivity.class));
        createBeans.add(CreateBean.getInstance("添加轮播", CreateBannerActivity.class));
        createBeans.add(CreateBean.getInstance("添加宝贝", CreateGoodsActivity.class));
        createBeans.add(CreateBean.getInstance("显示宝贝", GoodsCActivity.class));
        createBeans.add(CreateBean.getInstance("添加课程", null));
        createBeans.add(CreateBean.getInstance("添加章节", null));

        adapter = new BaseQuickAdapter<CreateBean, BaseViewHolder>(R.layout.dialog_manager_item, createBeans) {
            @Override
            protected void convert(BaseViewHolder helper, CreateBean item) {
                helper.setText(R.id.tv_text_name, item.getTitle());

            }
        };
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == createBeans.size() - 1) {
                    addCourse();

                    return;
                }
                if (position == createBeans.size() - 2) {
                    String string = StringUtils.getJson("courses.json", mContext);
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("albumResults");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject courseObj = jsonArray.getJSONObject(i);
                            addMusic(courseObj.getString("albumId"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return;
                }
                Intent intent = new Intent(mContext, createBeans.get(position).getaClass());
                ActivityUtils.startActivity(mContext, intent);

            }
        });
    }

    @Override
    public void getDataFromNet(boolean isRefresh) {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    /**
     * 添加课程
     */
    private void addCourse() {
        String string = StringUtils.getJson("courses.json", mContext);
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("albumResults");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject courseObj = jsonArray.getJSONObject(i);
                HttpParams params = new HttpParams();
                params.put("userId", "06l6pkk0");
                params.put("title", courseObj.getString("albumTitle"));
                params.put("coursePic", courseObj.getString("albumCover"));
                params.put("albumId", courseObj.getString("albumId"));
                OkGoUtils.getInstance().postNetForData(params, Constant.COURSE_ADD_COURSE, new JsonCallBack<ResultBean<CourseBean>>() {
                    @Override
                    public void onSuccess(ResultBean<CourseBean> mResultBean) {
                        DebugLog.e("mResultBean=", mResultBean.toString());

                    }

                    @Override
                    public void onError(ResultBean<CourseBean> mResultBean) {
                        UIHelper.ToastMessage(mContext, mResultBean.msg);

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加章节
     *
     * @param jsonName
     */
    private void addMusic(String jsonName) {
        if (TextUtils.isEmpty(jsonName)) {

            return;
        }
        String string = StringUtils.getJson(jsonName + ".json", mContext);
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject musicObj = jsonArray.getJSONObject(i);
                DebugLog.d("musicObj", musicObj.toString());
                HttpParams params = new HttpParams();
                params.put("userId", "06l6pkk0");
                params.put("albumId", musicObj.getString("albumId"));
                params.put("chapterId", musicObj.getString("trackId"));
                params.put("musicUrl", musicObj.getString("playUrl64"));
                params.put("musicPic", musicObj.getString("coverLarge"));
                params.put("musicTitle", musicObj.getString("title"));
                params.put("musicTime", musicObj.getString("playtimes"));
                params.put("orderNo", musicObj.getString("orderNo"));
                OkGoUtils.getInstance().postNetForData(params, Constant.MUSIC_ADD_MUSIC, new JsonCallBack<ResultBean<CourseBean>>() {
                    @Override
                    public void onSuccess(ResultBean<CourseBean> mResultBean) {
                        DebugLog.e("mResultBean::::", mResultBean.toString());

                    }

                    @Override
                    public void onError(ResultBean<CourseBean> mResultBean) {


                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
