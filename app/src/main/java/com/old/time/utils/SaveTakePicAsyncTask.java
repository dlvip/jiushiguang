package com.old.time.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.old.time.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by NING on 2018/1/26.
 */

public class SaveTakePicAsyncTask extends AsyncTask<Void, Void, String> {

    private byte[] data;//图片字节
    private int mCurrentCameraId;//前后摄像头
    private OnTakePicCallBackListener mOnTakePicCallBackListener;

    public SaveTakePicAsyncTask(byte[] data, int mCurrentCameraId, OnTakePicCallBackListener mOnTakePicCallBackListener) {
        this.data = data;
        this.mCurrentCameraId = mCurrentCameraId;
        this.mOnTakePicCallBackListener = mOnTakePicCallBackListener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... params) {

        return saveToSDCard(data);
//        return saveByteToBitmap(data);
    }


    @Override
    protected void onPostExecute(String path) {
        super.onPostExecute(path);
        if (mOnTakePicCallBackListener != null) {
            mOnTakePicCallBackListener.savePicPath(path);

        }
        // 最后通知图库更新---刷新手机相册
        MyApplication.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
    }

    private int PHOTO_SIZE_W, PHOTO_SIZE_H;

    /**
     * 将拍下来的照片存放在SD卡中
     */
    public String saveToSDCard(byte[] data) {
        Bitmap croppedImage = decodeRegionCrop(data);
        if (croppedImage == null) {

            return "";
        }
        String imagePath = saveToFile(croppedImage);
        if (croppedImage != null) {
            croppedImage.recycle();

        }
        return imagePath;
    }

    private String saveByteToBitmap(byte[] bytes) {
        String picPath = "";
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/longbei/Camera/");
            if (!dir.exists()) {
                dir.mkdirs();

            }
            String fileName = getCameraPath();
            File outFile = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(bytes, 0, data.length);
            fos.flush();
            fos.close();
            picPath = outFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return picPath;
    }


    // 保存图片文件
    public String saveToFile(Bitmap croppedImage) {
        if (croppedImage == null) {

            return "";
        }
        String picPath = "";
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/longbei/Camera/");
            if (!dir.exists()) {
                dir.mkdirs();

            }
            String fileName = getCameraPath();
            File outFile = new File(dir, fileName);
            FileOutputStream outputStream = new FileOutputStream(outFile);
            croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            picPath = outFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return picPath;
    }

    private static String getCameraPath() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("IMG");
        sb.append(calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH) + 1; // 0~11
        sb.append(month < 10 ? "0" + month : month);
        int day = calendar.get(Calendar.DATE);
        sb.append(day < 10 ? "0" + day : day);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        sb.append(hour < 10 ? "0" + hour : hour);
        int minute = calendar.get(Calendar.MINUTE);
        sb.append(minute < 10 ? "0" + minute : minute);
        int second = calendar.get(Calendar.SECOND);
        sb.append(second < 10 ? "0" + second : second);
        if (!new File(sb.toString() + ".jpg").exists()) {
            return sb.toString() + ".jpg";
        }
        StringBuilder tmpSb = new StringBuilder(sb);
        int indexStart = sb.length();
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            tmpSb.append('(');
            tmpSb.append(i);
            tmpSb.append(')');
            tmpSb.append(".jpg");
            if (!new File(tmpSb.toString()).exists()) {
                break;
            }
            tmpSb.delete(indexStart, tmpSb.length());
        }
        return tmpSb.toString();
    }


    private Bitmap decodeRegionCrop(byte[] data) {
        Bitmap rotatedImage = null;
        try {
            System.gc();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            PHOTO_SIZE_W = options.outWidth;
            PHOTO_SIZE_H = options.outHeight;
            options.inJustDecodeBounds = false;
            float ww = 1080f;
            float hh = 1920f;
            int be = 1;
            if (PHOTO_SIZE_W > PHOTO_SIZE_H && PHOTO_SIZE_W > ww) {
                be = (int) (options.outWidth / ww);

            } else if (PHOTO_SIZE_W < PHOTO_SIZE_H && PHOTO_SIZE_H > hh) {
                be = (int) (options.outHeight / hh);

            }
            if (be <= 0) {
                be = 1;

            }
            options.inSampleSize = be;//设置缩放比例
            Rect rect = new Rect(0, 0, PHOTO_SIZE_W, PHOTO_SIZE_H);
            InputStream is = new ByteArrayInputStream(data);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
            Bitmap croppedImage = decoder.decodeRegion(rect, options);
            Matrix m = new Matrix();
            m.setRotate(90, PHOTO_SIZE_W / 2, PHOTO_SIZE_H / 2);
            if (mCurrentCameraId == 1) {
                m.postScale(1, -1);

            }
            rotatedImage = Bitmap.createBitmap(croppedImage, 0, 0, PHOTO_SIZE_W / be, PHOTO_SIZE_H / be, m, true);
            if (rotatedImage != croppedImage) {
                croppedImage.recycle();

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return rotatedImage;
    }

    public interface OnTakePicCallBackListener {
        void savePicPath(String picPath);

    }
}
