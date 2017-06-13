package com.example.lenovo.hd_beijing_meseum.view.collect;

import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.model.Exhibition;
public class CollectDetail extends BaseActivity implements View.OnClickListener,GestureDetector.OnGestureListener{
private TextView title;
    private ViewFlipper viewFlipper;
    private WebView webView;
    private ImageView toBack;
    private boolean flag;
    private Exhibition exhibition;
    private GestureDetector detector;
    private boolean playOrStop;
    private WebSettings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_detail);
        Bundle bundle=getIntent().getBundleExtra("bundle");
        if (bundle!=null){
            flag=bundle.getBoolean("flag");
        }
        if (flag){
            exhibition=bundle.getParcelable("exhibition");
        }else {
            exhibition = getIntent().getParcelableExtra("exhibition");
        }

        initView();
        initFilerView();
        toBack.setOnClickListener(this);
        initWebView();


    }

    private void initWebView() {
        String url = Constant.getDefaultFileDir() + "/" + "CHINESE" + "/" + exhibition.getFileNo() + "/" + exhibition.getFileNo() + ".html";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocus();
        // 设置背景色
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///" + url);
    }

    private void initView() {
        title= (TextView) findViewById(R.id.title);
        title.setSelected(true);
        webView= (WebView) findViewById(R.id.webview_collect_detail_item);
        settings=webView.getSettings();
        settings.setTextSize(WebSettings.TextSize.LARGER);
        toBack= (ImageView) findViewById(R.id.toback);
        viewFlipper= (ViewFlipper) findViewById(R.id.flipper_collect);
        title.setText(exhibition.getName());
        detector = new GestureDetector(this);
    }
    private void initFilerView() {

        viewFlipper.removeAllViews();
        for (int i = 0; i <exhibition.getPicCount(); i++) {
            ImageView imageView = new ImageView(CollectDetail.this);
            Glide.with(CollectDetail.this)
                    .load(exhibition.getPicPath(i))

                    .placeholder(R.mipmap.down)
                    .into(imageView);
            viewFlipper.addView(imageView);
        }
        if (exhibition.getPicCount()>1){
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.zoomin));
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.zoomout));
            viewFlipper.startFlipping();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将触屏事件交给手势识别类处理
        return this.detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {

        playOrStop=!playOrStop;
        if (playOrStop){
            viewFlipper.startFlipping();

            return  true;
        }else  {
            viewFlipper.stopFlipping();
            return  true;
        }

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (exhibition!=null){
            if (exhibition.getPicCount()>1){
                if (e1.getX() - e2.getX() > 100) {
                    //设置View进入和退出的动画效果
                    this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.zoomin));
                    this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.zoomout));
                    this.viewFlipper.showNext();
                    return true;
                }

                if (e1.getX() - e2.getX() < -100) {
                    this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.right_in));
                    this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                            R.anim.right_out));
                    this.viewFlipper.showPrevious();
                    return true;
                }
            }
        }

        return false;

    }
}
