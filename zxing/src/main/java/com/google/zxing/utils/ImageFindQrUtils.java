package com.google.zxing.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by 芮勤 on 18-10-17 08:57
 */
public class ImageFindQrUtils {

    private static final String TAG = "ImageFindQrUtils";

    /**
     * 从图片中取出二维码
     *
     * @param bitmap
     * @param threshold
     * @return
     */
    public static Bitmap ImageFindQR(Bitmap bitmap, int threshold) {
        int[] oldPx = Image2SigleColor(bitmap, threshold); //图片二值化

        List<Point> leftTopPoints = new ArrayList<>(); //用来保存特征点左上角
        List<Point> rightBottomPoints = new ArrayList<>(); //用来保存特征点右上角

        List<Markpoint> findLeftTopPoints = new ArrayList<>();   //用来保存找到的二维码的左上角点
        List<Markpoint> findRightBottomPoints = new ArrayList<>();   //用来保存找到的二维码的右下角点
        int pointSize = 0;
        int qrLeftSize = bitmap.getWidth() / 150 < bitmap.getHeight() / 150 ? bitmap.getWidth() / 150 : bitmap.getHeight() / 150;               //二维码左上角宽度阈值
        Log.d(TAG, "ImageFindQR: qrLeftSize" + qrLeftSize);
        int qrDvalue = 4;    //二维码的每个标志点直接的差值

        int black = Color.argb(0xff, 0x00, 0x00, 0x00);
        int red = Color.argb(0xff, 0xff, 0x00, 0x00);
        int transparent = Color.argb(0xff, 0x00, 0xff, 0x00);

        for (int y = 3; y < bitmap.getHeight(); y++) {
            for (int x = 3; x < bitmap.getWidth(); x++) {

                int px = oldPx[x + bitmap.getWidth() * y];

                if (px == black) {
                    int x1Size = 0; //特征点上宽
                    int y1Size = 0; //特征点左高

                    int x2Size = 0; //特征点下宽
                    int y2Size = 0; //特征点右高

                    int pointx1 = x; //特征点最右边的点的x坐标
                    int pointy1 = y; //特征点最下边点的y坐标

                    //如果这个点太靠边，则不算起始点
                    if (bitmap.getWidth() - x <= qrLeftSize || bitmap.getHeight() - y <= qrLeftSize) {

                        continue;
                    }

                    //如果在最小阈值内，都没有连贯点
                    if (oldPx[x + qrLeftSize + bitmap.getWidth() * y] != black //
                            || oldPx[x + bitmap.getWidth() * (y + qrLeftSize)] != black) {

                        continue;
                    }
                    for (int x1 = x; x1 < bitmap.getWidth(); x1++) { //找到二维码左标志的最右边点
                        int tmpPx = oldPx[x1 + bitmap.getWidth() * y];
                        if (tmpPx == black) {
                            x1Size++;
                            pointx1 = x1;
                        } else {
                            break;
                        }
                    }
                    //如果最右边的点头上有黑色，则基本确定不为矩形
                    if (oldPx[pointx1 + bitmap.getWidth() * (y - 1)] //
                            == black && oldPx[pointx1 + bitmap.getWidth() * (y - 2)] //
                            == black && oldPx[pointx1 + bitmap.getWidth() * (y - 3)] == black) {

                        continue;
                    }
                    //二维码左标志的横大于阈值
                    if (x1Size > qrLeftSize && x1Size < bitmap.getWidth() / 5) {
                        //找到二维码左标志的最下边的点
                        for (int y1 = y; y1 < bitmap.getHeight(); y1++) {
                            int tmpPx = oldPx[x + bitmap.getWidth() * y1];
                            if (tmpPx == black) {
                                y1Size++;
                                pointy1 = y1;

                            } else {

                                break;
                            }
                        }
                        //如果该点到最右边和到最左边的距离差不多
                        if (Math.abs(x1Size - y1Size) < qrDvalue) {
                            //如果最左下边的点左边有黑色，则基本确定不为矩形
                            if (oldPx[x - 1 + bitmap.getWidth() * pointy1] //
                                    == black && oldPx[x - 2 + bitmap.getWidth() * pointy1] //
                                    == black && oldPx[x - 3 + bitmap.getWidth() * pointy1] == black) {

                                continue;
                            }

                            boolean isBreak = false;
                            for (int a = 0; a < 10; a++) {
                                y1Size = y1Size - a;
                                x2Size = 0;
                                //找到二维码左标志的右下角点的到左下角的距离
                                for (int x1 = x; x1 < bitmap.getWidth(); x1++) {
                                    int tmpPx = oldPx[x1 + bitmap.getWidth() * (pointy1 - a)];
                                    if (tmpPx == black) {
                                        x2Size++;

                                    } else {

                                        break;
                                    }
                                }
                                //二维码左标志的右下角到左下角的距离大于阈值
                                if (x2Size > qrLeftSize) {
                                    for (int b = 0; b < 10; b++) {
                                        x1Size = x1Size - b;
                                        y2Size = 0;
                                        for (int y1 = y; y1 < bitmap.getHeight(); y1++) { //找到二维码右下角到右上角的距离
                                            int tmpPx = oldPx[pointx1 - b + bitmap.getWidth() * y1];
                                            if (tmpPx == black) {
                                                y2Size++;

                                            } else {

                                                break;
                                            }
                                        }
                                        if (Math.abs(x1Size - y1Size) < qrDvalue && Math.abs(x1Size - y2Size) //
                                                < qrDvalue && Math.abs(x2Size - y1Size) < qrDvalue && Math.abs(x2Size - y2Size)//
                                                < qrDvalue) {   //如果差不多为矩形

                                            int dSize = x1Size < y1Size ? x1Size : y1Size;
                                            //给标志点上色，将标志点附近10px都上色
                                            for (int i = -10; i < dSize + 10; i++) {
                                                for (int j = -10; j < dSize + 10; j++) {
                                                    int tmpx = x + i;
                                                    int tmpy = y + j;
                                                    tmpx = tmpx < 0 ? 0 : tmpx;
                                                    tmpx = tmpx > bitmap.getWidth() - 1 ? bitmap.getWidth() - 1 : tmpx;

                                                    tmpy = tmpy < 0 ? 0 : tmpy;
                                                    tmpy = tmpy > bitmap.getHeight() - 1 ? bitmap.getHeight() - 1 : tmpy;

                                                    if (i == -10 && j == -10) {
                                                        leftTopPoints.add(new Point(tmpx, tmpy));
                                                    }

                                                    if (i == dSize + 9 && j == dSize + 9) {
                                                        rightBottomPoints.add(new Point(tmpx, tmpy));
                                                    }
                                                    oldPx[tmpx + tmpy * bitmap.getWidth()] = red;
                                                }
                                            }
                                            isBreak = true;
                                            pointSize++;
                                        }
                                        if (isBreak) {
                                            break;
                                        }
                                    }
                                }
                                if (isBreak) {

                                    break;
                                }
                            }
                        }
                    }
                } else {
                    //如果既不是黑色的待查点，或者红色的标志点，则将颜色变为透明
                    if (oldPx[x + bitmap.getWidth() * y] != red) {
                        oldPx[x + bitmap.getWidth() * y] = transparent;

                    }
                }
            }
        }
        //遍历找到符合二维码特征的三个点
        for (int i = 0; i < leftTopPoints.size(); i++) { //二维码左上角的特征点
            for (int j = i + 1; j < leftTopPoints.size(); j++) { //二维码右上角的特征点
                if (Math.abs(leftTopPoints.get(i).y - leftTopPoints.get(j).y) < qrDvalue) {
                    for (int k = 0; k < leftTopPoints.size(); k++) { //二维码左下角的特征点
                        if (k != i && k != j) {    //排除自己
                            if ((Math.abs(leftTopPoints.get(i).x - leftTopPoints.get(k).x)//
                                    < qrDvalue) && ((Math.abs(leftTopPoints.get(k).y - leftTopPoints.get(i).y) //
                                    - Math.abs(leftTopPoints.get(i).x - leftTopPoints.get(j).x)) < qrDvalue)) {
                                Markpoint markpoint = new Markpoint(leftTopPoints.get(i), leftTopPoints.get(j)//
                                        , leftTopPoints.get(k));
                                findLeftTopPoints.add(markpoint);

                                Markpoint markpoint2 = new Markpoint(rightBottomPoints.get(i)//
                                        , rightBottomPoints.get(j), rightBottomPoints.get(k));
                                findRightBottomPoints.add(markpoint2);

                            }
                        }
                    }
                }
            }
        }
        if (findLeftTopPoints.size() == 0) {
            Log.e(TAG, "ImageFindQR: 没有找到对应的二维码点");

            return null;
        }
        Point leftPoint = findLeftTopPoints.get(0).p1;
        Point rightPoint = findLeftTopPoints.get(0).p1;
        Point topPoint = findLeftTopPoints.get(0).p1;
        Point bottomPoint = findLeftTopPoints.get(0).p1;


        for (Markpoint m : findLeftTopPoints) {
            leftPoint = leftPoint.x < m.p1.x ? leftPoint : m.p1;
            topPoint = topPoint.y < m.p1.y ? topPoint : m.p1;

        }

        for (Markpoint m : findRightBottomPoints) {
            rightPoint = rightPoint.x > m.p2.x ? rightPoint : m.p2;
            bottomPoint = bottomPoint.y > m.p3.y ? bottomPoint : m.p3;

        }

        int finalLeft = leftPoint.x - 10 < 0 ? 0 : leftPoint.x - 10;
        int finalTop = topPoint.y - 10 < 0 ? 0 : topPoint.y - 10;
        int finalRight = rightPoint.x + 10 > bitmap.getWidth() ? bitmap.getWidth() : rightPoint.x + 10;
        int finalBottom = bottomPoint.y + 10 > bitmap.getHeight() ? bitmap.getHeight() : bottomPoint.y + 10;
        int finalWidth = finalRight - finalLeft;
        int finalHeight = finalBottom - finalTop;

        int[] fatherPx = new int[bitmap.getHeight() * bitmap.getWidth()];
        bitmap.getPixels(fatherPx, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int[] finalPx = new int[finalWidth * finalHeight];
        for (int y = finalTop; y < finalBottom; y++) {
            for (int x = finalLeft; x < finalRight; x++) {
                int realx = x - finalLeft;
                int realy = y - finalTop;
                int finalIndex = realx + realy * finalWidth;
                int oldIndex = x + y * bitmap.getWidth();

                finalPx[finalIndex] = fatherPx[oldIndex];

            }
        }

        Bitmap finalBitmap = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888);
        finalBitmap.setPixels(finalPx, 0, finalWidth, 0, 0, finalWidth, finalHeight);
        return finalBitmap;
    }

    static class Markpoint {
        private Point p1, p2, p3; //左上，右上，左下

        public Markpoint(Point p1, Point p2, Point p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }


    }

    public static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }
    }

    /**
     * 获取图片二值化的数组
     *
     * @param bitmap
     * @param threshold 二值化阈值
     * @return
     */
    public static int[] Image2SigleColor(Bitmap bitmap, int threshold) {

        StringBuffer stringBuffer = new StringBuffer();
        int[] oldPx = new int[bitmap.getHeight() * bitmap.getWidth()];
        bitmap.getPixels(oldPx, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int[] newPx = new int[bitmap.getHeight() * bitmap.getWidth()];
        int size = 0;
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {

                int px = oldPx[x + bitmap.getWidth() * y];
                int b = Color.blue(px);
                int g = Color.green(px);
                int r = Color.red(px);

                int light = (r + g + b) / 3;
                //二值化
                if (light > threshold) {
                    newPx[x + bitmap.getWidth() * y] = Color.argb(0xff, 0xff, 0xff, 0xff);

                } else {
                    newPx[x + bitmap.getWidth() * y] = Color.argb(0xff, 0x00, 0x00, 0x00);

                }
            }
            size++;
        }
        return newPx;
    }

    public static final String SPLIT_KEY = "postCard###";

    /**
     * 生成二维码
     *
     * @param text 需要生成二维码的文字、网址等
     * @param size 需要生成二维码的大小
     * @return bitmap
     */
    public static Bitmap createQRCode(String text, int size) {
        text = "https://www.baidu.com" + "?" + SPLIT_KEY + text;
        Bitmap bitmap = null;
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;

                    } else {
                        pixels[y * size + x] = 0xffffffff;

                    }
                }
            }
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);

        } catch (WriterException e) {
            e.printStackTrace();

        }
        return bitmap;
    }
}
