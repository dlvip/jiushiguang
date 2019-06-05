package com.old.time.readLib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.beans.BookEntity;
import com.old.time.readLib.db.BookCatalogue;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.MyLinearLayoutManager;
import com.old.time.utils.RecyclerItemDecoration;
import com.old.time.utils.UIHelper;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class ReadActivity extends BaseActivity {

    /**
     * 阅读
     *
     * @param context
     * @param bookEntity
     */
    public static void openBook(Activity context, BookEntity bookEntity) {
        if (bookEntity == null) {

            throw new NullPointerException("BookEntity can not be null");
        }

        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_BOOK, bookEntity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityUtils.startActivity(context, intent);

    }

    private static final String TAG = "ReadActivity";
    private final static String EXTRA_BOOK = "bookEntity";
    private final static int MESSAGE_CHANGEPROGRESS = 1;

    private PageWidget bookpage;
    private TextView tv_pre;
    private SeekBar sb_progress;
    private TextView tv_next;
    private TextView tv_directory;
    private TextView tv_dayornight;
    private TextView tv_pagemode;
    private TextView tv_setting;
    private RelativeLayout rl_bottom;
    private View mainView;

    private Config config;
    private PageFactory pageFactory;
    private Boolean isShow = false;
    private SettingDialog mSettingDialog;
    private PageModeDialog mPageModeDialog;
    private Boolean mDayOrNight;
    private boolean isSpeaking = false;

    private DrawerLayout read_dl_slide;

    private RecyclerView recycler_view;

    // 接收电池信息更新的广播
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int level = intent.getIntExtra("level", 0);
                pageFactory.updateBattery(level);

            } else if (Intent.ACTION_TIME_TICK.equals(action)) {
                pageFactory.updateTime();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        super.onCreate(savedInstanceState);

    }

    private List<BookCatalogue> bookCatalogues = new ArrayList<>();
    private BookCatalogueAdapter bookCatalogueAdapter;

    @Override
    public int getLayoutID() {
        return R.layout.activity_read;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT < 19) {
            bookpage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        }
        mainView = findViewById(R.id.header_main);
        mainView.findViewById(R.id.view_line).setVisibility(View.GONE);
        mainView.setBackgroundResource(R.color.color_2c2c2c);
        bookpage = findViewById(R.id.bookpage);
        tv_pre = findViewById(R.id.tv_pre);
        sb_progress = findViewById(R.id.sb_progress);
        tv_next = findViewById(R.id.tv_next);
        tv_directory = findViewById(R.id.tv_directory);
        tv_dayornight = findViewById(R.id.tv_dayornight);
        tv_pagemode = findViewById(R.id.tv_pagemode);
        tv_setting = findViewById(R.id.tv_setting);
        rl_bottom = findViewById(R.id.rl_bottom);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new MyLinearLayoutManager(mContext));
        recycler_view.addItemDecoration(new RecyclerItemDecoration(mContext));
        bookCatalogueAdapter = BookCatalogueAdapter.getInstance(bookCatalogues);
        bookCatalogueAdapter.bindToRecyclerView(recycler_view);
        recycler_view.setAdapter(bookCatalogueAdapter);

        read_dl_slide = findViewById(R.id.read_dl_slide);
        //禁止滑动展示DrawerLayout
        read_dl_slide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //侧边打开后，返回键能够起作用
        read_dl_slide.setFocusableInTouchMode(false);

        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();

        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);

        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //改变屏幕亮度
        if (!config.isSystemLight()) {
            BrightnessUtil.setBrightness(this, config.getLight());

        }

        initDayOrNight();

        List<BookEntity> bookEntities = DataSupport.findAll(BookEntity.class);
        for (BookEntity b : bookEntities) {
            DebugLog.d(TAG, b.toString());

        }

        //获取intent中的携带的信息
        Intent intent = getIntent();
        BookEntity bookEntity = (BookEntity) intent.getSerializableExtra(EXTRA_BOOK);
        setBookForView(bookEntity);

    }

    /**
     * 开始阅读
     *
     * @param bookEntity
     */
    private void setBookForView(BookEntity bookEntity) {
        if (bookEntity == null) {

            return;
        }
        setTitleColor(R.color.color_fff);
        setTitleText(bookEntity.getTitle());

        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);

        try {
            pageFactory.openBook(bookEntity);

        } catch (IOException e) {
            e.printStackTrace();
            UIHelper.ToastMessage(mContext, "打开电子书失败");

        }
    }

    /**
     * 检查文件是否存在
     *
     * @param bookEntity
     */
    private void saveBookFile(BookEntity bookEntity) {
        BookEntity book = DataSupport.find(BookEntity.class, bookEntity.getId());
        if (book != null) {
            setBookForView(book);

            return;
        }
        List<BookEntity> bookEntities = new ArrayList<>();
        bookEntities.add(bookEntity);
        SaveBookToSqlLiteTask mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
        mSaveBookToSqlLiteTask.execute(bookEntities);

    }

    @SuppressLint("StaticFieldLeak")
    private class SaveBookToSqlLiteTask extends AsyncTask<List<BookEntity>, Void, Integer> {

        private static final int FAIL = 0;
        private static final int SUCCESS = 1;
        private static final int REPEAT = 2;
        private BookEntity repeatBookList;

        @Override
        protected Integer doInBackground(List<BookEntity>... params) {
            List<BookEntity> bookLists = params[0];
            for (BookEntity bookList : bookLists) {
                List<BookEntity> books = DataSupport.where("filePath = ?", bookList.getFilePath()).find(BookEntity.class);
                if (books.size() > 0) {
                    repeatBookList = bookList;

                    return REPEAT;
                }
            }

            try {
                DataSupport.saveAll(bookLists);

            } catch (Exception e) {
                e.printStackTrace();

                return FAIL;
            }
            return SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            String msg = "";
            switch (result) {
                case FAIL:
                    msg = "由于一些原因添加书本失败";

                    break;
                case SUCCESS:
                    msg = "添加书本成功";

                    break;
                case REPEAT:
                    msg = "书本" + repeatBookList.getTitle() + "重复了";
                    setBookForView(repeatBookList);

                    break;
            }
            UIHelper.ToastMessage(mContext, msg);

        }
    }

    /**
     * 翻页弹框
     */
    private void showPageModeDialog() {
        if (mPageModeDialog == null) {
            mPageModeDialog = new PageModeDialog(mContext, new PageModeDialog.PageModeListener() {

                @Override
                public void changePageMode(int pageMode) {
                    bookpage.setPageMode(pageMode);

                }
            });
        }
        mPageModeDialog.showDialog();
    }

    /**
     * 设置弹框
     */
    private void showSettingDialog() {
        if (mSettingDialog == null) {
            mSettingDialog = new SettingDialog(mContext, new SettingDialog.SettingListener() {
                @Override
                public void changeSystemBright(Boolean isSystem, float brightness) {
                    if (!isSystem) {
                        BrightnessUtil.setBrightness(ReadActivity.this, brightness);

                    } else {
                        int bh = BrightnessUtil.getScreenBrightness(ReadActivity.this);
                        BrightnessUtil.setBrightness(ReadActivity.this, bh);

                    }
                }

                @Override
                public void changeFontSize(int fontSize) {
                    pageFactory.changeFontSize(fontSize);

                }

                @Override
                public void changeTypeFace(Typeface typeface) {
                    pageFactory.changeTypeface(typeface);

                }

                @Override
                public void changeBookBg(int type) {
                    pageFactory.changeBookBg(type);

                }
            });
        }
        mSettingDialog.showDialog();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        setOnclick(new View[]{tv_pre, tv_next, tv_directory, tv_dayornight, tv_pagemode, tv_setting});
        bookCatalogueAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                read_dl_slide.openDrawer(Gravity.END);
                BookCatalogue bookCatalogue = (BookCatalogue) adapter.getItem(position);
                pageFactory.changeChapter(bookCatalogue != null ? bookCatalogue.getBookCatalogueStartPos() : 0);


            }
        });
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            float pro;

            @Override // 触发操作，拖动
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = (float) (progress / 10000.0);

            }

            @Override// 表示进度条刚开始拖动，开始拖动时候触发的操作
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override// 停止拖动时候
            public void onStopTrackingTouch(SeekBar seekBar) {
                pageFactory.changeProgress(pro);

            }
        });

        pageFactory.setPageEvent(new PageFactory.PageEvent() {
            @Override
            public void changeProgress(float progress) {
                Message message = new Message();
                message.what = MESSAGE_CHANGEPROGRESS;
                message.obj = progress;
                mHandler.sendMessage(message);

            }
        });
        bookpage.setTouchListener(new PageWidget.TouchListener() {
            @Override
            public void center() {
                if (isShow) {
                    hideReadSetting();

                } else {
                    showReadSetting();

                }
            }

            @Override
            public Boolean prePage() {
                if (isShow || isSpeaking) {

                    return false;
                }

                pageFactory.prePage();
                return !pageFactory.isfirstPage();
            }

            @Override
            public Boolean nextPage() {
                if (isShow || isSpeaking) {

                    return false;
                }

                pageFactory.nextPage();
                return !pageFactory.islastPage();
            }

            @Override
            public void cancel() {
                pageFactory.cancelPage();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_CHANGEPROGRESS:
                    float progress = (float) msg.obj;
                    setSeekBarProgress(progress);

                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageFactory.clear();
        bookpage = null;
        unregisterReceiver(myReceiver);
        isSpeaking = false;

    }

    public void initDayOrNight() {
        mDayOrNight = config.getDayOrNight();
        if (mDayOrNight) {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));

        } else {
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));

        }
    }

    //改变显示模式
    public void changeDayOrNight() {
        if (mDayOrNight) {
            mDayOrNight = false;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_night));

        } else {
            mDayOrNight = true;
            tv_dayornight.setText(getResources().getString(R.string.read_setting_day));

        }
        config.setDayOrNight(mDayOrNight);
        pageFactory.setDayOrNight(mDayOrNight);
    }

    public void setSeekBarProgress(float progress) {
        sb_progress.setProgress((int) (progress * 10000));
    }

    private void showReadSetting() {
        isShow = true;
        initMenuAnim();

        rl_bottom.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.VISIBLE);
        rl_bottom.startAnimation(mBottomInAnim);
        mainView.startAnimation(mTopInAnim);

    }

    private void hideReadSetting() {
        isShow = false;
        initMenuAnim();
        if (mainView.getVisibility() == View.VISIBLE) {
            mainView.startAnimation(mTopOutAnim);

        }
        if (rl_bottom.getVisibility() == View.VISIBLE) {
            rl_bottom.startAnimation(mBottomOutAnim);

        }
        rl_bottom.setVisibility(View.GONE);
        mainView.setVisibility(View.GONE);

    }

    private Animation mTopInAnim, mTopOutAnim, mBottomInAnim, mBottomOutAnim;

    //初始化菜单动画
    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }

    private void setOnclick(View[] views) {
        for (View view : views) {
            if (view != null) view.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_pre:
                pageFactory.preChapter();

                break;
            case R.id.tv_next:
                pageFactory.nextChapter();

                break;
            case R.id.tv_directory:
                hideReadSetting();
                bookCatalogueAdapter.setNewData(bookCatalogues);
                read_dl_slide.openDrawer(Gravity.START);

                break;
            case R.id.tv_dayornight:
                changeDayOrNight();

                break;
            case R.id.tv_pagemode:
                hideReadSetting();
                showPageModeDialog();

                break;
            case R.id.tv_setting:
                hideReadSetting();
                showSettingDialog();

                break;
        }
    }
}
