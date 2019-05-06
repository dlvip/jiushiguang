package com.old.time.mall;

import android.view.View;
import android.widget.ImageView;

import com.old.time.R;
import com.old.time.activitys.BaseCActivity;
import com.old.time.dialogs.DialogListManager;
import com.old.time.interfaces.OnClickManagerCallBack;
import com.old.time.utils.RecyclerItemDecoration;

import org.greenrobot.eventbus.EventBus;

public class MallBookActivity extends BaseCActivity {

    private View relative_layout_more, relative_layout_user;
    private ImageView img_more;

    private MallBookAdapter mAdapter;

    @Override
    protected void initView() {
        super.initView();
        img_more = findViewById(R.id.img_more);
        img_more.setImageResource(R.mipmap.icon_black_more);
        findViewById(R.id.left_layout).setVisibility(View.GONE);
        relative_layout_more = findViewById(R.id.relative_layout_more);
        relative_layout_more.setVisibility(View.VISIBLE);
        setTitleText("期待觅邮");
        mAdapter = new MallBookAdapter();
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(mContext, RecyclerItemDecoration.VERTICAL_LIST, 10));
        mRecyclerView.setAdapter(mAdapter);

        View headerView = View.inflate(mContext, R.layout.header_post_cart, null);
        relative_layout_user = headerView.findViewById(R.id.relative_layout_user);
        headerView.findViewById(R.id.recycler_view).setVisibility(View.GONE);
        mAdapter.removeAllHeaderView();
        mAdapter.addHeaderView(headerView);
        mAdapter.setHeaderAndEmpty(true);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        relative_layout_user.setOnClickListener(this);
        relative_layout_more.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.relative_layout_user:


                break;
            case R.id.relative_layout_more:


                break;
        }
    }

    private DialogListManager dialogListManager;

    /**
     * 更多弹框
     */
    private void showMoreDialog() {
        if(dialogListManager == null){
            dialogListManager = new DialogListManager(mContext, new OnClickManagerCallBack() {
                @Override
                public void onClickRankManagerCallBack(int position, String typeName) {


                }
            });
        }
        dialogListManager.setDialogViewData("更多操作",new String[]{"联系我们"});

    }

    @Override
    public void getDataFromNet(boolean isRefresh) {


    }
}
