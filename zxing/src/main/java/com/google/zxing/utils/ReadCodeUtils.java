package com.google.zxing.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by shaotongyu on 17/7/17.
 * 识别图中二维码
 */
public class ReadCodeUtils {
    /**
     * 扫描二维码图片的方法，word线程
     *
     * @param path 图片本地地址
     * @return
     */
    public static String scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {

            return null;
        }
        Result result;
        RGBLuminanceSource source;
        MultiFormatReader multiFormatReader = new MultiFormatReader();

        Bitmap scanBitmap = getBitmap(path);
        try {
            Hashtable<DecodeHintType, Object> hints = new Hashtable<>(2);
            // 可以解析的编码类型
            Vector<BarcodeFormat> decodeFormats = new Vector<>();
            if (decodeFormats.isEmpty()) {
                Vector<BarcodeFormat> PRODUCT_FORMATS = new Vector<>(5);
                PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
                PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
                PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
                PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
                // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
                Vector<BarcodeFormat> ONE_D_FORMATS = new Vector<>(PRODUCT_FORMATS.size() + 4);
                ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
                ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
                ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
                ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
                ONE_D_FORMATS.add(BarcodeFormat.ITF);
                Vector<BarcodeFormat> QR_CODE_FORMATS = new Vector<>(1);
                QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
                Vector<BarcodeFormat> DATA_MATRIX_FORMATS = new Vector<>(1);
                DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);

                // 这里设置可扫描的类型，我这里选择了都支持
                decodeFormats.addAll(ONE_D_FORMATS);
                decodeFormats.addAll(QR_CODE_FORMATS);
                decodeFormats.addAll(DATA_MATRIX_FORMATS);
            }
            hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
            //设置二维码内容的编码
            hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
            multiFormatReader.setHints(hints);

            int width = scanBitmap.getWidth();
            int height = scanBitmap.getHeight();
            int[] pixels = new int[width * height];
            scanBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            source = new RGBLuminanceSource(width, height, pixels);
            result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)));
            if (!scanBitmap.isRecycled()) scanBitmap.recycle();
            if(result == null){

                return "";
            }
            return result.getText();

        } catch (NotFoundException e) {
            e.printStackTrace();

        }

        result = syncDecodeQRCode(scanBitmap);
        if (!scanBitmap.isRecycled()) scanBitmap.recycle();
        if(result == null){

            return "";
        }
        return result.getText();
    }

    /**
     * 更具图片文件地址获取图片
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outWidth / (float) 400);
        if (sampleSize <= 0) sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        return scanBitmap;
    }

    /**
     * 同步解析bitmap二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param bitmap 要解析的二维码图片
     * @return 返回二维码图片里的内容 或 null
     */
    public static Result syncDecodeQRCode(Bitmap bitmap) {
        System.out.println(bitmap.getHeight() + ":" + bitmap.getWidth());
        try {
            if (bitmap.getHeight() > 600 || bitmap.getWidth() > 600) {
                bitmap = big(ImageFindQrUtils.ImageFindQR(bitmap, 50));

            } else if (bitmap.getHeight() < 400 || bitmap.getWidth() < 400) {
                bitmap = big(bitmap);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Result result;
        RGBLuminanceSource source = null;
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            source = new RGBLuminanceSource(width, height, pixels);
            result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)));
            if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if (source != null) {
                try {
                    result = new MultiFormatReader().decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)));
                    return result;
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
            if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
            return null;
        }
    }

    private static Bitmap big(Bitmap bitmap) {
        if (bitmap.getHeight() < 400 || bitmap.getWidth() < 400) {
            Matrix matrix = new Matrix();
            int scale = 600 / (bitmap.getHeight() > bitmap.getWidth() ? bitmap.getWidth() : bitmap.getHeight());
            matrix.postScale(scale, scale); //长和宽放大缩小的比例
            Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            return resizeBmp;
        } else {

            return bitmap;
        }
    }
}
