package com.old.time.readLib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.old.time.R;
import com.old.time.activitys.BaseActivity;
import com.old.time.readLib.db.BookList;
import com.old.time.utils.UIHelper;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class ReadActivity extends BaseActivity {

    private static final String TAG = "ReadActivity";
    private final static String EXTRA_BOOK = "bookList";
    private final static int MESSAGE_CHANGEPROGRESS = 1;

    private PageWidget bookpage;
    private TextView tv_progress;
    private RelativeLayout rl_progress;
    private TextView tv_pre;
    private SeekBar sb_progress;
    private TextView tv_next;
    private TextView tv_directory;
    private TextView tv_dayornight;
    private TextView tv_pagemode;
    private TextView tv_setting;
    private LinearLayout bookpop_bottom;
    private RelativeLayout rl_bottom;
    private View mainView;

    private Config config;
    private PageFactory pageFactory;
    private Boolean isShow = false;
    private SettingDialog mSettingDialog;
    private PageModeDialog mPageModeDialog;
    private Boolean mDayOrNight;
    private boolean isSpeaking = false;

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
    public int getLayoutID() {
        return R.layout.activity_read;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT < 19) {
            bookpage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        }
        setTitleText("");
        mainView = findViewById(R.id.header_main);
        mainView.setVisibility(View.GONE);
        bookpage = findViewById(R.id.bookpage);
        tv_progress = findViewById(R.id.tv_progress);
        rl_progress = findViewById(R.id.rl_progress);
        tv_pre = findViewById(R.id.tv_pre);
        sb_progress = findViewById(R.id.sb_progress);
        tv_next = findViewById(R.id.tv_next);
        tv_directory = findViewById(R.id.tv_directory);
        tv_dayornight = findViewById(R.id.tv_dayornight);
        tv_pagemode = findViewById(R.id.tv_pagemode);
        tv_setting = findViewById(R.id.tv_setting);
        bookpop_bottom = findViewById(R.id.bookpop_bottom);
        rl_bottom = findViewById(R.id.rl_bottom);

        config = Config.getInstance();
        pageFactory = PageFactory.getInstance();

        IntentFilter mfilter = new IntentFilter();
        mfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mfilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(myReceiver, mfilter);

        //获取屏幕宽高
        WindowManager manage = getWindowManager();
        Display display = manage.getDefaultDisplay();
        Point displaysize = new Point();
        display.getSize(displaysize);
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //改变屏幕亮度
        if (!config.isSystemLight()) {
            BrightnessUtil.setBrightness(this, config.getLight());

        }
        //获取intent中的携带的信息
        Intent intent = getIntent();
        BookList bookList = (BookList) intent.getSerializableExtra(EXTRA_BOOK);

        bookpage.setPageMode(config.getPageMode());
        pageFactory.setPageWidget(bookpage);

        try {
            pageFactory.openBook(bookList);

        } catch (IOException e) {
            e.printStackTrace();
            UIHelper.ToastMessage(mContext, "打开电子书失败");

        }
        initDayOrNight();

        setOnclick(new View[]{tv_pre, tv_next, tv_directory//
                , tv_dayornight, tv_pagemode, tv_setting});
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
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            float pro;

            // 触发操作，拖动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pro = (float) (progress / 10000.0);
                showProgress(pro);

            }

            // 表示进度条刚开始拖动，开始拖动时候触发的操作
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // 停止拖动时候
            @Override
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

    public static void openBook(final BookList bookList, Activity context) {
        if (bookList == null) {

            throw new NullPointerException("BookList can not be null");
        }

        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_BOOK, bookList);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        context.startActivity(intent);

    }

    //显示书本进度
    public void showProgress(float progress) {
        if (rl_progress.getVisibility() != View.VISIBLE) {
            rl_progress.setVisibility(View.VISIBLE);

        }
        setProgress(progress);
    }

    //隐藏书本进度
    public void hideProgress() {
        rl_progress.setVisibility(View.GONE);
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

    private void setProgress(float progress) {
        DecimalFormat decimalFormat = new DecimalFormat("00.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(progress * 100.0);//format 返回的是字符串
        tv_progress.setText(String.valueOf(p + "%"));

    }

    public void setSeekBarProgress(float progress) {
        sb_progress.setProgress((int) (progress * 10000));
    }

    private void showReadSetting() {
        isShow = true;
        rl_progress.setVisibility(View.GONE);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_enter);
        rl_bottom.startAnimation(topAnim);
        mainView.startAnimation(topAnim);
        rl_bottom.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.VISIBLE);

    }

    private void hideReadSetting() {
        isShow = false;
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.dialog_top_exit);
        if (rl_bottom.getVisibility() == View.VISIBLE) {
            rl_bottom.startAnimation(topAnim);

        }
        if (mainView.getVisibility() == View.VISIBLE) {
            mainView.startAnimation(topAnim);

        }
        rl_bottom.setVisibility(View.GONE);
        mainView.setVisibility(View.GONE);

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
                UIHelper.ToastMessage(mContext, "目录");

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
