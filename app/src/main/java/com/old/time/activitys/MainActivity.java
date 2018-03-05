package com.old.time.activitys;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.old.time.constants.Code;
import com.old.time.R;
import com.old.time.utils.ASRUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.EasyPhotos;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ASRUtil mASRUtil;
    private TextView isr_text;

    @Override
    protected void initView() {
        findViewById(R.id.relative_layout_take).setOnClickListener(this);
        findViewById(R.id.tv_xfyun).setOnClickListener(this);
        isr_text = findViewById(R.id.isr_text);
        mASRUtil = ASRUtil.getInstanceASR().initSpeech(mContext, new ASRUtil.OnStartAsrCallBack() {
            @Override
            public void onClickRankManagerCallBack(int type, String str) {
                DebugLog.i(TAG, str);
                if (type == ASRUtil.ASRUTIL_CONDE_SPEECH_RESULT) {
                    isr_text.setText(str);

                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    private boolean isOnclick = false;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.relative_layout_take:
                CamerTakeActivity.startCamerActivity(mContext, Code.REQUEST_CODE_30);

                break;
            case R.id.tv_xfyun:
                if (isOnclick) {
                    isOnclick = false;

                    return;
                }
                isr_text.setText("");
                isOnclick = true;
                mASRUtil.startAsr();

                break;
        }
    }

    /**
     * 选择的图片集
     */
    private ArrayList<String> selectedPhotoList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        switch (requestCode) {
            case Code.REQUEST_CODE_30:
                ArrayList<String> resultPhotos = data.getStringArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() == 1) {
                    resultPhotos.add(resultPhotos.get(0));

                }
                selectedPhotoList.clear();
                selectedPhotoList.addAll(resultPhotos);
                EasyPhotos.startPuzzleWithPaths(mContext, selectedPhotoList, Environment.getExternalStorageDirectory().getAbsolutePath(), "AlbumBuilder", Code.REQUEST_CODE_40, false);

                break;
            case Code.REQUEST_CODE_40:
                selectedPhotoList.clear();
                selectedPhotoList.add(data.getStringExtra(EasyPhotos.RESULT_PATHS));
                Intent intent = new Intent(mContext, PhotoPagerActivity.class);
                intent.putExtra(PhotoPagerActivity.EXTRA_IMAGE_URLS, selectedPhotoList);
                intent.putExtra(PhotoPagerActivity.EXTRA_IMAGE_INDEX, 0);
                ActivityUtils.startPicActivity(mContext, intent);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mASRUtil != null) {
            mASRUtil.destroyASRUtil();

        }
    }
}
