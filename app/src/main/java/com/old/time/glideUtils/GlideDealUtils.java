package com.old.time.glideUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.old.time.BuildConfig;
import com.old.time.MyApplication;
import com.old.time.R;
import com.old.time.constants.Constant;
import com.old.time.interfaces.ImageDownLoadCallBack;
import com.old.time.task.CallBackTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by diliang on 2016/12/30.
 */
public class GlideDealUtils {

    /**
     * 使用glide读取缓存图片 并保存到本地
     */
    public static void onDownLoad(final String imgUrl, final ImageDownLoadCallBack downLoadCallBack) {
        MyApplication.getInstance().getTaskManager().delTask(Constant.IMAGEDOWNLOAD_THREAD_NAME);
        MyApplication.getClient().getTaskManager().addTask(new CallBackTask(Constant.IMAGEDOWNLOAD_THREAD_NAME) {
            @Override
            protected void doTask() {
                Bitmap bitmap = null;//glide的缓存图片
                try {
                    bitmap = GlideUtils.getInstance().getBitmap(MyApplication.getInstance(), imgUrl);
                    if (bitmap != null) {
                        // 在这里执行图片保存方法
                        saveImageToGallery(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    if (bitmap != null && currentFile.exists()) {
                        downLoadCallBack.onDownLoadSuccess(bitmap);

                    } else {
                        downLoadCallBack.onDownLoadSuccess(null);

                    }
                }
            }
        });
    }

    private static File currentFile;

    public static Uri saveImageToGallery(Bitmap bmp) {
        //注意小米手机必须这样获得public绝对路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        File appDir = new File(file, MyApplication.getInstance().getResources().getString(R.string.app_name));
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        Uri uri = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            File photoFile = new File(currentFile.getPath());
            // 7.0和以上版本的系统要通过 FileProvider 创建一个 content 类型的 Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String packageName = BuildConfig.APPLICATION_ID + ".fileProvider";
                uri = FileProvider.getUriForFile(MyApplication.getInstance(), packageName, photoFile);

            } else {
                uri = Uri.fromFile(photoFile);

            }
            // 最后通知图库更新---刷新手机相册
            MyApplication.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        }

        return uri;
    }
}
