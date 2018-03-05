package com.old.time.utils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.old.time.constants.Key;

/**
 * 讯飞语音工具类
 * Created by NING on 2018/2/27.
 */

public class ASRUtil {

    public static final int ASRUtil_CONDE_INIT_ERROR = -1;//异常
    public static final int ASRUTIL_CONDE_SPEECH_RESULT = 0;//文字结果返回
    public static final int ASRUtil_CONDE_BEGIN_Of_SPEECH = 1;//可以开始说话
    public static final int ASRUtil_CONDE_VOLUME_CHANGED = 2;//正在说话
    public static final int ASRUtil_CONDE_END_OF_SPEECH = 3;//结束说话
    private OnStartAsrCallBack mOnStartAsrCallBack;
    private SpeechRecognizer mAsr;
    private static final String TAG = "ASRUtil";

    public static ASRUtil mASRUtil;

    public static ASRUtil getInstanceASR() {
        if (mASRUtil != null) {

            return mASRUtil;
        }
        mASRUtil = new ASRUtil();

        return mASRUtil;
    }

    /**
     * 讯飞语音初始化
     *
     * @param mContext
     */
    public static void setAppID(Context mContext) {
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=" + Key.XFYUN_APP_ID);

    }

    /**
     * 初始化语音转换类
     */
    public ASRUtil initSpeech(Context mContext, final OnStartAsrCallBack mOnStartAsrCallBack) {
        this.mOnStartAsrCallBack = mOnStartAsrCallBack;
        mAsr = SpeechRecognizer.createRecognizer(mContext, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUtil_CONDE_INIT_ERROR, "初始化失败,错误码：" + code);

                }
            }
        });
        setAsrParam();

        return mASRUtil;
    }

    private int ret;

    /**
     * 开启录音转换
     */
    public void startAsr() {
        if (mAsr == null) {

            return;
        }
        mAsr.stopListening();
        ret = mAsr.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUtil_CONDE_INIT_ERROR, "识别失败,错误码: " + ret);

        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setAsrParam() {
        if (mAsr == null) {

            return;
        }
        //设置识别引擎
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //设置返回结果为json格式
        mAsr.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mAsr.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

    }

    private String mEngineType = null;

    /**
     * 识别监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUtil_CONDE_VOLUME_CHANGED, "当前正在说话，音量大小：" + volume);

        }

        @Override
        public void onResult(final RecognizerResult result, boolean isLast) {
            if (isLast) {

                return;
            }
            if (null != result) {
                String text;
                if ("cloud".equalsIgnoreCase(mEngineType)) {
                    text = JsonParser.parseGrammarResult(result.getResultString());

                } else {
                    text = JsonParser.parseLocalGrammarResult(result.getResultString());

                }
                if (TextUtils.isEmpty(text)) {
                    mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUtil_CONDE_INIT_ERROR, "没有识别出结果");

                } else {
                    mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUTIL_CONDE_SPEECH_RESULT, text);


                }
            } else {
                mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUtil_CONDE_INIT_ERROR, "没有结果");

            }
        }

        @Override
        public void onEndOfSpeech() {
            mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUtil_CONDE_END_OF_SPEECH, "结束说话");

        }

        @Override
        public void onBeginOfSpeech() {
            mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUtil_CONDE_BEGIN_Of_SPEECH, "可以开始说话");

        }

        @Override
        public void onError(SpeechError error) {
            mOnStartAsrCallBack.onClickRankManagerCallBack(ASRUtil_CONDE_BEGIN_Of_SPEECH, "onError Code：" + error.getErrorCode());

        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                showDebugLog("onError Code：" + sid);

            }
        }
    };

    /**
     * 打印log
     *
     * @param s
     */
    private void showDebugLog(String s) {
        DebugLog.i(TAG, s);

    }


    /**
     * 退出时释放连接
     */
    public void destroyASRUtil() {
        if (null != mAsr) {
            mAsr.cancel();
            mAsr.destroy();

        }
    }

    /**
     * 录音转换回调
     */
    public interface OnStartAsrCallBack {
        void onClickRankManagerCallBack(int type, String str);

    }
}
