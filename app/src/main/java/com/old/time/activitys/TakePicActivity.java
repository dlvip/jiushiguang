package com.old.time.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.old.time.constants.Code;
import com.old.time.R;
import com.old.time.glideUtils.GlideUtils;
import com.old.time.permission.PermissionUtil;
import com.old.time.utils.ActivityUtils;
import com.old.time.utils.CameraUtil;
import com.old.time.utils.EasyPhotos;
import com.old.time.utils.SaveTakePicAsyncTask;
import com.old.time.utils.ScreenTools;
import com.old.time.utils.UIHelper;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TakePicActivity extends BaseActivity implements SurfaceHolder.Callback {

    private static final int PIC_COUNT_SIZE = 9;

    private RecyclerView recycler_view_pics;
    private List<String> picPaths = new ArrayList<>();
    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;
    private int width;
    private LinearLayout linear_layout_pics;
    private TextView tv_pic_detail;
    private SurfaceView mSurfaceView;
    private SurfaceHolder holder;
    private ImageView img_btn_lights, img_btn_reverse, img_take_pic;
    private TextView tv_pic_upload;

    public static void startCameraActivity(Activity mContext, int requestCode) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)) {

            return;
        }
        Intent intent = new Intent(mContext, TakePicActivity.class);
        ActivityUtils.startActivityForResult(mContext, intent, requestCode);

    }

    public static void startCameraActivity(Activity mContext, List<String> picPaths, int requestCode) {
        if (!PermissionUtil.checkAndRequestPermissionsInActivity(mContext, CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)) {

            return;
        }
        Intent intent = new Intent(mContext, TakePicActivity.class);
        intent.putExtra(PhotoPickActivity.SELECT_PHOTO_LIST, (Serializable) picPaths);
        ActivityUtils.startActivityForResult(mContext, intent, requestCode);

    }


    @Override
    protected void initView() {
        CameraUtil.init(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
        picPaths.clear();
        width = (ScreenTools.instance(this).getScreenWidth() - 5 * UIHelper.dip2px(10)) * 10 / 45;
        tv_pic_detail = (TextView) findViewById(R.id.tv_pic_detail);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this); // 回调接口
        findViewById(R.id.tv_btn_back).setOnClickListener(this);
        findViewById(R.id.tv_btn_pics).setOnClickListener(this);
        img_btn_lights = (ImageView) findViewById(R.id.img_btn_lights);
        img_btn_lights.setOnClickListener(this);
        img_btn_reverse = (ImageView) findViewById(R.id.img_btn_reverse);
        img_btn_reverse.setOnClickListener(this);
        img_take_pic = (ImageView) findViewById(R.id.img_take_pic);
        img_take_pic.setOnClickListener(this);
        tv_pic_upload = (TextView) findViewById(R.id.tv_pic_upload);
        tv_pic_upload.setOnClickListener(this);
        linear_layout_pics = (LinearLayout) findViewById(R.id.linear_layout_pics);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linear_layout_pics.getLayoutParams();
        params.height = width + ScreenTools.instance(this).dip2px(20);
        linear_layout_pics.setLayoutParams(params);
        recycler_view_pics = (RecyclerView) findViewById(R.id.recycler_view_pics);
        recycler_view_pics.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        List<String> pics = (List<String>) getIntent().getSerializableExtra(PhotoPickActivity.SELECT_PHOTO_LIST);
        if (pics != null) {
            picPaths.addAll(pics);

        }
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_record_pic_video, picPaths) {
            @Override
            protected void convert(final BaseViewHolder helper, final String item) {
                ImageView img_right_btn = helper.getView(R.id.img_right_btn);
                img_right_btn.setImageResource(R.mipmap.btn_close);
                ConstraintLayout constraint_layout_parent = helper.getView(R.id.constraint_layout_parent);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) constraint_layout_parent.getLayoutParams();
                params.width = width;
                params.height = width;
                constraint_layout_parent.setLayoutParams(params);
                ImageView img_record_pic = helper.getView(R.id.img_record_pic);
                GlideUtils.getInstance().setImageViewWH(mContext, item, img_record_pic, width);
                img_right_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.remove(helper.getLayoutPosition());
                        showPicDetail();

                    }
                });
                img_record_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = helper.getLayoutPosition();
                        PhotoPagerActivity.startPhotoPagerActivity((Activity) mContext, (Serializable) picPaths, position);

                    }
                });
            }
        };
        recycler_view_pics.setAdapter(mAdapter);

        showPicDetail();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_btn_back:
                ActivityUtils.finishActivity(mContext);

                break;
            case R.id.tv_btn_pics:
                PhotoPickActivity.startPhotoPickActivity(mContext, false, PIC_COUNT_SIZE//
                        , (Serializable) picPaths, Code.REQUEST_CODE_30);

                break;
            case R.id.img_btn_lights:
                turnLight(mCamera);

                break;
            case R.id.img_btn_reverse:
                switchCamera();

                break;
            case R.id.img_take_pic:
                takePhoto();

                break;
            case R.id.tv_pic_upload:
                updatePicToLine();

                break;
        }
    }

    /**
     * 上传图片
     */
    private void updatePicToLine() {
        if (picPaths.size() == 0) {

            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EasyPhotos.RESULT_PHOTOS, (Serializable) picPaths);
        setResult(Activity.RESULT_OK, intent);
        ActivityUtils.finishActivity(mContext);
    }

    /**
     * 闪光灯开关
     *
     * @param camera
     */
    private void turnLight(Camera camera) {
        if (camera == null || camera.getParameters() == null || camera.getParameters().getSupportedFlashModes() == null) {

            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        String flashMode = camera.getParameters().getFlashMode();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            camera.setParameters(parameters);
            img_btn_lights.setImageResource(R.mipmap.icon_lights);

        } else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            img_btn_lights.setImageResource(R.mipmap.icon_lights_pressed);
            camera.setParameters(parameters);

        }
    }

    /**
     * 切换前后置摄像头
     */
    private void switchCamera() {
        releaseCamera();
        mCurrentCameraId = (mCurrentCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = getCamera(mCurrentCameraId);
        if (holder != null) {
            startPreview(mCamera, holder);

        }
    }

    private ProgressDialog pd;

    /**
     * 拍照
     */
    private void takePhoto() {
        if (PIC_COUNT_SIZE == picPaths.size()) {
            UIHelper.ToastMessage(mContext, "一次最多上传" + PIC_COUNT_SIZE + "张");

            return;
        }
        if (mCamera == null) {

            return;
        }
        pd = UIHelper.showProgressMessageDialog(mContext, getString(R.string.please_wait));
        mCamera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                new SaveTakePicAsyncTask(data, mCurrentCameraId, new SaveTakePicAsyncTask.OnTakePicCallBackListener() {
                    @Override
                    public void savePicPath(String picPath) {
                        UIHelper.dissmissProgressDialog(pd);
                        if (mAdapter != null && !TextUtils.isEmpty(picPath)) {
                            mAdapter.addData(picPath);
                            showPicDetail();

                        }
                        mCamera.stopPreview();
                        mCamera.startPreview();
                    }
                }).execute();
            }
        });
    }

    /**
     * 显示拍照描述
     */
    private void showPicDetail() {
        if (picPaths.size() == 0) {
            tv_pic_upload.setVisibility(View.GONE);
            tv_pic_detail.setVisibility(View.VISIBLE);

        } else {
            tv_pic_detail.setVisibility(View.GONE);
            tv_pic_upload.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

            return;
        }
        switch (requestCode) {
            case Code.REQUEST_CODE_30:
                List<String> pathStrs = data.getStringArrayListExtra(PhotoPickActivity.SELECT_PHOTO_LIST);
                picPaths.clear();
                picPaths.addAll(pathStrs);
                mAdapter.notifyDataSetChanged();
                showPicDetail();

                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_camer_take;
    }

    private Camera mCamera;
    private int mCurrentCameraId;
    private Camera.Parameters parameters;

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            int numCams = Camera.getNumberOfCameras();
            if (numCams > 0) {
                mCurrentCameraId = 0;

            }
            mCamera = getCamera(mCurrentCameraId);
            if (holder != null) {
                startPreview(mCamera, holder);

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();

    }

    /**
     * 获取Camera实例
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);

        } catch (Exception e) {
            UIHelper.ToastMessage(mContext, "未检测到相机");

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera, mSurfaceView);
            camera.setPreviewDisplay(holder);
            CameraUtil.setCameraDisplayOrientation(this, mCurrentCameraId, camera);
            camera.startPreview();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;

        }
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera, SurfaceView mSurfaceView) {
        if (camera == null) return;
        Camera.Parameters parameters = camera.getParameters();

        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {//自动对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        }

        Camera.Size previewSize = CameraUtil.findBestPreviewResolution(camera);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = CameraUtil.getPropPictureSize(parameters.getSupportedPictureSizes(), 1000);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

        if (Build.VERSION.SDK_INT >= 8) {//控制图像的正确显示方向
            setDisplayOrientation(camera, 90);

        } else {
            parameters.setRotation(90);
            camera.setParameters(parameters);

        }
        camera.setParameters(parameters);

        int picHeight = CameraUtil.screenWidth * previewSize.width / previewSize.height;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CameraUtil.screenWidth, picHeight);
        mSurfaceView.setLayoutParams(params);

        pointFocus(100, 100);

    }

    //定点对焦的代码
    private void pointFocus(int x, int y) {
        try {
            mCamera.cancelAutoFocus();
            parameters = mCamera.getParameters();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                showPoint(x, y);

            }
            mCamera.setParameters(parameters);
            autoFocus();
        } catch (Exception e) {


        }
    }

    private void showPoint(int x, int y) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> areas = new ArrayList<>();
            int rectY = -x * 2000 / CameraUtil.screenWidth + 1000;
            int rectX = y * 2000 / CameraUtil.screenHeight - 1000;

            int left = rectX < -900 ? -1000 : rectX - 100;
            int top = rectY < -900 ? -1000 : rectY - 100;
            int right = rectX > 900 ? 1000 : rectX + 100;
            int bottom = rectY > 900 ? 1000 : rectY + 100;
            Rect area1 = new Rect(left, top, right, bottom);
            areas.add(new Camera.Area(area1, 800));
            parameters.setMeteringAreas(areas);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    //实现自动对焦
    private void autoFocus() {
        new Thread() {
            @Override
            public void run() {
                if (mCamera == null) {

                    return;
                }
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            setupCamera(camera, mSurfaceView);//实现相机的参数初始化

                        }
                    }
                });
            }
        };
    }

    //实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
        autoFocus();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();

    }
}
