package com.example.lenovo.hd_beijing_meseum.tileview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * This is just a random View used to show callouts.
 * It's old and is probably chock-full of errors and
 * bad-practice, but was handy and looks decent.
 */
public class HCallOut extends RelativeLayout {

    private TextView title;
    private TextView subtitle;
    private ImageView icon;

    private static int getDIP(Context context, int pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, context
                .getResources().getDisplayMetrics());
    }

    public HCallOut(Context context) {
        super(context);

        //泡泡主体部分-水平方向的LinearLayout
        LinearLayout bubble = new LinearLayout(context);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(0xFF313231);
        drawable.setCornerRadius(getDIP(context, 4));
        bubble.setBackgroundDrawable(drawable);
        bubble.setId(new Random().nextInt());
        int padding = getDIP(context, 17);
        bubble.setPadding(padding, padding, padding, padding);
        LayoutParams bubbleLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        addView(bubble, bubbleLayout);

        //泡泡下面的小三角形
        Nub nub = new Nub(context);
        LayoutParams nubLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        nubLayout.addRule(RelativeLayout.BELOW, bubble.getId());
        nubLayout.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(nub, nubLayout);

        //泡泡主体部分左侧-标题、副标题
        LinearLayout labels = new LinearLayout(context);
        labels.setOrientation(LinearLayout.VERTICAL);
        LayoutParams labelsLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        bubble.addView(labels, labelsLayout);
        int maxWidth = getDIP(context, 230);

        //标题
        title = new TextView(context);
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
        title.setMaxWidth(maxWidth);
        title.setVisibility(GONE);
        LinearLayout.LayoutParams titleLayout = new LinearLayout.LayoutParams(LayoutParams
                .WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        labels.addView(title, titleLayout);

        //副标题
        subtitle = new TextView(context);
        subtitle.setTextColor(0xFF88afca);
        subtitle.setTypeface(Typeface.SANS_SERIF);
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        subtitle.setMaxWidth(maxWidth);
        subtitle.setVisibility(GONE);
        LinearLayout.LayoutParams subtitleLayout = new LinearLayout.LayoutParams(LayoutParams
                .WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        labels.addView(subtitle, subtitleLayout);

        //泡泡主体部分右侧-icon
        RelativeLayout iconColumn = new RelativeLayout(context);
        LayoutParams iconColumnLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .MATCH_PARENT);
        bubble.addView(iconColumn, iconColumnLayout);

        //icon
        icon = new ImageView(context);
        icon.setId(new Random().nextInt());
        icon.setScaleType(ImageView.ScaleType.MATRIX);
        LayoutParams iconLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        iconLayout.addRule(RelativeLayout.CENTER_VERTICAL);
        iconLayout.leftMargin = getDIP(context, 10);
        iconColumn.addView(icon, iconLayout);

    }

    public void setTitle(CharSequence text) {
        title.setText(text);
        title.setVisibility(VISIBLE);
    }

    public void setSubtitle(CharSequence text) {
        subtitle.setText(text);
        subtitle.setVisibility(VISIBLE);
    }

    public void setIcon(int resId) {
        icon.setImageResource(resId);
    }

    public ImageView getIcon(){
        return icon;
    }
    public void transitionIn() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation
                .RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        scaleAnimation.setInterpolator(new OvershootInterpolator(1.2f));
        scaleAnimation.setDuration(250);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1f);
        alphaAnimation.setDuration(200);

        AnimationSet animationSet = new AnimationSet(false);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        startAnimation(animationSet);

    }

    //泡泡下面的小三角形
    private static class Nub extends View {

        private Paint paint = new Paint();
        private Path path = new Path();

        public Nub(Context context) {
            super(context);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFF313231);
            paint.setAntiAlias(true);

            path.lineTo(getDIP(context, 20), 0);
            path.lineTo(getDIP(context, 10), getDIP(context, 15));
            path.close();

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getDIP(getContext(), 20), getDIP(getContext(), 15));
        }

        @Override
        public void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
            super.onDraw(canvas);
        }
    }

}