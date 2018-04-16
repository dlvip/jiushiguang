package com.old.time.utils;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.old.time.beans.PhotoInfoBean;
import com.old.time.constants.Constant;
import com.old.time.interfaces.AliyPhotoCallbackInterface;
import com.old.time.interfaces.UploadImagesCallBack;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 上传图片到阿里云
 *
 * @author WindowY
 */
public class AliyPostUtil {

    public OSS oss;

    private AliyPostUtil() {

    }

    private static AliyPostUtil aliyPostUtil = null;

    //静态工厂方法   、加同步
    public static synchronized AliyPostUtil getInstance(Context context) {
        if (aliyPostUtil == null) {
            aliyPostUtil = new AliyPostUtil();
            aliyPostUtil.initUtil(context);

        }
        return aliyPostUtil;
    }

    private void initUtil(Context mContext) {
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.accessKeyId, Constant.accessKeySecret);
        oss = new OSSClient(mContext, Constant.endpoint, credentialProvider);

    }

    private void postFileonAliy(String dir, String fileOnlineName, final AliyPhotoCallbackInterface callback) {
        PutObjectRequest put = new PutObjectRequest(Constant.bucketNameOut, fileOnlineName, dir);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg");// 设置content-type
        put.setMetadata(metadata);
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                DebugLog.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);

            }
        });
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                callback.requestCode(Constant.ALIYPHOTO_CALLBACK_SUCCESS, null);

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (clientExcepion != null) {
                    clientExcepion.printStackTrace();

                }
                if (serviceException != null) {
                    DebugLog.e("ErrorCode", serviceException.getErrorCode());
                    DebugLog.e("RequestId", serviceException.getRequestId());
                    DebugLog.e("HostId", serviceException.getHostId());
                    DebugLog.e("RawMessage", serviceException.getRawMessage());

                }
                callback.requestCode(Constant.ALIYPHOTO_CALLBACK_FILED, null);
            }
        });
    }

    /**
     * 返回到阿里云上图片路径
     * 参数 ： 头像 ，非头像
     */
    private String getAliyPicName(boolean isAvatar, int i, String uid) {
        String data = "sns/";
        if (isAvatar) {
            data = "avatar/";

        }
        Calendar now = Calendar.getInstance();
        data += now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "/";  //阿里云上文件路径
        String fileName = uid + System.currentTimeMillis() + i + ".jpg";
        return (data + fileName).trim();
    }

    private int sucessNum;

    /**
     * 上传图片到阿里云
     *
     * @param dirs
     * @param imagesCallBack
     */
    public void uploadCompresImgsToAliyun(final List<String> dirs, final UploadImagesCallBack imagesCallBack) {
        final List<PhotoInfoBean> mPhotoInfoBeans = new ArrayList<>();
        mPhotoInfoBeans.clear();
        sucessNum = 0;
        for (int i = 0; i < dirs.size(); i++) {
            String onlineFileName = aliyPostUtil.getAliyPicName(false, i, UserLocalInfoUtils.instance().getUserId());
            // 计算sampleSize  获取图片信息
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(dirs.get(i), options);
            PhotoInfoBean photoWHBean = new PhotoInfoBean();
            photoWHBean.picKey = onlineFileName;
            photoWHBean.with = options.outWidth;
            photoWHBean.height = options.outHeight;
            mPhotoInfoBeans.add(photoWHBean);
            aliyPostUtil.postFileonAliy(dirs.get(i), onlineFileName, new AliyPhotoCallbackInterface() {

                @Override
                public void requestCode(int code, JSONObject resultData) {
                    DebugLog.e("图片上传成功:::" + sucessNum, "resultData.toString()");
                    if (code == Constant.ALIYPHOTO_CALLBACK_SUCCESS) {
                        synchronized (this) {
                            sucessNum++;

                        }
                        if (sucessNum >= dirs.size()) {
                            DebugLog.e("图片上传阿里云完毕:::", mPhotoInfoBeans.toString());
                            imagesCallBack.getImagesPath(mPhotoInfoBeans);

                        }
                    } else {
                        imagesCallBack.getImagesPath(null);

                    }
                }
            });
        }
    }
}
