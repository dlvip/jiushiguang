package com.old.time.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aliyun.recorder.AliyunRecorderCreator;
import com.aliyun.recorder.supply.AliyunIRecorder;
import com.aliyun.recorder.supply.RecordCallback;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.MediaInfo;
import com.old.time.R;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.DebugLog;
import com.old.time.utils.FileUtils;
import com.old.time.views.VideoGlSurfaceView;

public class RecorderVideoActivity extends BaseActivity {

    public static void startRecorderVideoActivity(Context mContext) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity((Activity) mContext
                , new String[]{Manifest.permission.CAMERA
                        , Manifest.permission.RECORD_AUDIO
                        , Manifest.permission.READ_EXTERNAL_STORAGE})) {

            return;
        }
        Intent intent = new Intent(mContext, RecorderVideoActivity.class);
        ActivityUtils.startActivity((Activity) mContext, intent);

    }

    private static String TAG = "RecorderVideoActivity";

    private AliyunIRecorder recorder;
    private VideoGlSurfaceView mSurfaceView;
    private ImageView img_take_pic;

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
        img_take_pic = findViewById(R.id.img_take_pic);
        recorder = AliyunRecorderCreator.getRecorderInstance(mContext);
        mSurfaceView = findViewById(R.id.gl_surface_view);
        recorder.setDisplayView(mSurfaceView);
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.setVideoWidth(480);
        mediaInfo.setVideoHeight(854);
        mediaInfo.setEncoderFps(4);//硬编时自适应宽高为16的倍数
        recorder.setMediaInfo(mediaInfo);
        recorder.setOutputPath(FileUtils.getVideoFilePath());
        recorder.setRecordCallback(new RecordCallback() {

            @Override
            public void onComplete(boolean b, long l) {
                DebugLog.d(TAG, "onComplete:::" + "b=" + b + ":::l=" + l);
            }

            @Override
            public void onFinish(String s) {
                DebugLog.d(TAG, "onFinish:::" + "s=" + s);
            }

            @Override
            public void onProgress(long l) {
                DebugLog.d(TAG, "onProgress:::l=" + l);
            }

            @Override
            public void onMaxDuration() {
                DebugLog.d(TAG, "onMaxDuration");
            }

            @Override
            public void onError(int i) {
                DebugLog.d(TAG, "onError:::i=" + i);
            }

            @Override
            public void onInitReady() {
                DebugLog.d(TAG, "onInitReady");
            }

            @Override
            public void onDrawReady() {
                DebugLog.d(TAG, "onDrawReady");
            }

            @Override
            public void onPictureBack(Bitmap bitmap) {
                DebugLog.d(TAG, "onPictureBack");
            }

            @Override
            public void onPictureDataBack(byte[] bytes) {
                DebugLog.d(TAG, "onPictureDataBack");
            }
        });
    }

    private boolean isStartRecording;

    @Override
    protected void initEvent() {
        super.initEvent();
        img_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStartRecording) {
                    recorder.stopRecording();
                    recorder.finishRecording();

                } else {
                    recorder.setRotation(90);
                    recorder.startRecording();

                }
                isStartRecording = !isStartRecording;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recorder.setCamera(CameraType.BACK);
        recorder.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recorder.stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorder.destroy();
        AliyunRecorderCreator.destroyRecorderInstance();

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_aliyun_video;
    }
}
