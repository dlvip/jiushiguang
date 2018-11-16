package com.pic.lib.glideUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.pic.lib.PicCode;
import com.pic.lib.task.CallBackTask;
import com.pic.lib.task.TaskManager;
import com.pic.lib.R;

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
    public static void onDownLoad(final Context mContext, final String imgUrl, final ImageDownLoadCallBack downLoadCallBack) {
        TaskManager.getInstance().delTask(PicCode.IMAGEDOWNLOAD_THREAD_NAME);
        TaskManager.getInstance().addTask(new CallBackTask(PicCode.IMAGEDOWNLOAD_THREAD_NAME) {
            @Override
            protected void doTask() {
                Bitmap bitmap = null;//glide的缓存图片
                try {
                    bitmap = GlideUtils.getInstance().getBitmap(mContext, imgUrl);
                    if (bitmap != null) {
                        saveImageToGallery(mContext, bitmap);

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

    /**
     * 保存图片
     *
     * @param bmp
     */
    public static void saveImageToGallery(Context mContext, Bitmap bmp) {
        //注意小米手机必须这样获得public绝对路径
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        String picFileName = mContext.getResources().getString(R.string.app_name);
        File appDir = new File(file, picFileName);
        if (!appDir.exists()) {
            appDir.mkdirs();

        }
        String fileName = System.currentTimeMillis() + ".jpg";
        currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
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
            // 最后通知图库更新---刷新手机相册
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(currentFile.getPath()))));
        }
    }

    public interface ImageDownLoadCallBack {
        void onDownLoadSuccess(Bitmap bitmap);

    }
}
