package com.old.time.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class AnimUtil {

    public static AnimationSet getAnimSet(AnimationListener listener, Animation... anims) {
        AnimationSet animSet = new AnimationSet(false);
        for (Animation anim : anims) {
            animSet.addAnimation(anim);

        }
        if (listener != null) animSet.setAnimationListener(listener);
        return animSet;
    }

    /**
     * 缩放: 0~1, 以中心点缩放
     */
    public static ScaleAnimation getScaleAnim() {
        ScaleAnimation scale = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(5000);
        scale.setFillAfter(true);
        return scale;
    }

    /**
     * 旋转动画: 0~360, 中心点
     */
    public static RotateAnimation getRotateAnim() {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, //
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setFillAfter(true); // 动画执行完毕后, 停留在动画结束的状态下.
        return rotate;
    }

    /**
     * 渐变动画: 从没有到有 0~1
     */
    public static AlphaAnimation getAlphaAnim() {
        AlphaAnimation alphaAnima = new AlphaAnimation(0.5f, 1);
        alphaAnima.setDuration(3000);
        alphaAnima.setFillAfter(true);
        return alphaAnima;
    }
}
