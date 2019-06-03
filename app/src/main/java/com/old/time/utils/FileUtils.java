package com.old.time.utils;

import android.content.Context;
import android.os.Environment;

import com.old.time.MyApplication;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by NING on 2018/3/21.
 */

public class FileUtils {

    public static String SDPATH = Environment.getExternalStorageDirectory().toString() + "/olderTime/";

    /**
     * sd卡的根目录
     */
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

    /**
     * 手机的缓存根目录
     */
    private static String mDataRootPath = MyApplication.getInstance().getCacheDir().getPath();

    /**
     * 保存Image的目录名
     */
    private final static String FOLDER_NAME = "/AndroidImage";

    /**
     * 图文详情图片缓存
     */
    private final static String IMAGE_RICHTEXT = "/RichTextImage";

    /**
     * 获取视频保存的文件
     *
     * @return
     */
    public static String getVideoFilePath() {

        return SDPATH + "AndroidVideo";
    }


    public static String getRichTextImageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + IMAGE_RICHTEXT : mDataRootPath + IMAGE_RICHTEXT;
    }

    /**
     * 生成图片路径
     *
     * @param fileName
     * @return 图片文件
     */
    public static File createPicturePath(String fileName) {
        fileName = fileName.replaceAll("[^\\w]", "");
        String path = getRichTextImageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        File file = new File(path + File.separator + fileName + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getSDPath(Context mContext) {
        String mCacheRoot = "";
        File dir = mContext.getExternalFilesDir("cache");
        if (dir == null) {
            mCacheRoot = mContext.getFilesDir().toString() + "/cache";

        } else {
            mCacheRoot = dir.toString();

        }
        return mCacheRoot;
    }

    /**
     * 获取sd卡下的data/data/包名/cache
     *
     * @return
     */
    public static String getSDdataCachePath(Context mContext) {
        String mCacheRoot = "";
        File dir = mContext.getExternalCacheDir();
        if (dir == null) {
            mCacheRoot = mContext.getCacheDir().toString();

        } else {
            mCacheRoot = dir.toString();

        }
        return mCacheRoot;
    }

    /**
     * 根据路径获取文件名
     *
     * @param pathandname
     * @return
     */
    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {

            return pathandname.substring(start + 1, end);
        } else {

            return "";
        }
    }

    /**
     * 获取文件编码
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String getCharset(String fileName) throws IOException{
        String charset;
        FileInputStream fis = new FileInputStream(fileName);
        byte[] buf = new byte[4096];
        // (1)
        UniversalDetector detector = new UniversalDetector(null);
        // (2)
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        // (3)
        detector.dataEnd();
        // (4)
        charset = detector.getDetectedCharset();
        // (5)
        detector.reset();
        return charset;
    }

}
