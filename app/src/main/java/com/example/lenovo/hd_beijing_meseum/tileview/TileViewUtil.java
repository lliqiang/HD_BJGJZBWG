package com.example.lenovo.hd_beijing_meseum.tileview;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.qozix.tileview.TileView;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/4/14 16:53
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class TileViewUtil {

    /**
     * 初次定位
     *
     * @param tileView
     * @param movingImg
     * @param locX
     * @param locY
     */
    public static void firstFix(TileView tileView, ImageView movingImg, double
            locX, double locY) {
        tileView.addMarker(movingImg, locX, locY, null, null);
        tileView.slideToAndCenter(locX, locY);
    }

    /**
     * 定位图标平滑移动
     *
     * @param tileView
     * @param movingImg
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public static void move(final TileView tileView, final ImageView movingImg, double startX,
                            double startY, final double endX, final double endY) {
        if (startX == endX && startY == endY) {
            tileView.removeMarker(movingImg);
            firstFix(tileView, movingImg, endX, endY);
        } else {
            float scale = tileView.getScale();
            Animation animation = new TranslateAnimation(0, (float) (scale * (endX - startX)), 0,
                    (float) (scale * (endY - startY)));
            animation.setDuration(2000L);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tileView.slideToAndCenter(endX, endY);
                    tileView.removeMarker(movingImg);
                    tileView.addMarker(movingImg, endX, endY, null, null);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            tileView.removeMarker(movingImg);
            tileView.addMarker(movingImg, startX, startY, null, null);
            movingImg.startAnimation(animation);
        }
    }
}
