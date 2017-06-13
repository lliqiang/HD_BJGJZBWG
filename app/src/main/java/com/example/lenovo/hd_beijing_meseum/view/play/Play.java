package com.example.lenovo.hd_beijing_meseum.view.play;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.model.Exhibition;
import com.example.lenovo.hd_beijing_meseum.view.guideservice.MapFragment;

import java.io.File;
import java.io.IOException;

public class Play extends BaseActivity implements View.OnClickListener,GestureDetector.OnGestureListener {
    private WebView webView;
    private ImageView toggle;
    private TextView startTxt;
    private TextView totalTxt;
    private SeekBar seekBar;
    private TextView title;
    private boolean isPause;
    private ImageView toBack;
    private boolean isFirst;
    private boolean playOrStop;
    private ViewFlipper viewFlipper;
    private GestureDetector detector;
    private MediaPlayer mediaPlayer;
    private Exhibition exhibition;
private WebSettings settings;
    private boolean flag=false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    int current = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(current);
                    current /= 1000;
                    int minute = current / 60;
                    int second = current % 60;
                    startTxt.setText(String.format("%02d:%02d", minute, second));
                    //每隔500ms通过handler回传一次数据
                    sendEmptyMessageDelayed(1, 500);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
        Bundle bundle=getIntent().getBundleExtra("bundle");
        if (bundle!=null){
            flag=bundle.getBoolean("flag");
        }

        if (flag){
            exhibition= (Exhibition) bundle.getParcelable("exhibition");
        }else {
            exhibition = getIntent().getParcelableExtra("exhibition");


        }

//        initFilerView();
//        initUI();
//        title.setText(exhibition.getName());
//        loadExhibit(exhibition, handler);
//        toBack.setOnClickListener(this);
//        toggle.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFilerView();
        initUI();
        title.setText(exhibition.getName());
        loadExhibit(exhibition, handler);
        toBack.setOnClickListener(this);
        toggle.setOnClickListener(this);
    }

    private void initFilerView() {

        viewFlipper.removeAllViews();
        for (int i = 0; i <exhibition.getPicCount(); i++) {
            ImageView imageView = new ImageView(Play.this);
            Glide.with(Play.this)
                    .load(exhibition.getPicPath(i))
                    .into(imageView);
            viewFlipper.addView(imageView);
        }
        if (exhibition.getPicCount()>1){
            isFirst=true;
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.zoomin));
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.zoomout));
            viewFlipper.startFlipping();
        }


    }



    private void initView() {
        toggle = (ImageView) findViewById(R.id.toggle);
        webView = (WebView) findViewById(R.id.webview_ListGuide_detail_item);
        settings=webView.getSettings();
        settings.setTextSize(WebSettings.TextSize.LARGER);
        startTxt = (TextView) findViewById(R.id.play_time);
        totalTxt = (TextView) findViewById(R.id.total_time);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        title = (TextView) findViewById(R.id.title);
        title.setSelected(true);
        toBack = (ImageView) findViewById(R.id.toback);
        viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        detector = new GestureDetector(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle:
                if (isPause) {
                    doPlay();
                } else {
                    doPause();
                }
                isPause = !isPause;

                break;
            case R.id.toback:

                finish();
                break;

        }


    }

    private void initUI() {
        //设置WebView的背景颜色

        //设置播放的进度图片
//        seekbar.setThumb(getResources().getDrawable(R.drawable.progress_one));
        //设置进度条图片
//        seekbar.setProgressDrawable(getResources().getDrawable(R.drawable
//                .progress_two));
        addSeekbarChangeListener(seekBar);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playPrepare();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Play.this.finish();
            }
        });

    }

    public void playPrepare() {
        //设置播放时长
        int duration = mediaPlayer.getDuration();
        seekBar.setMax(duration);
        int minute = duration / 1000 / 60;
        int second = (duration / 1000) % 60;
        //将分秒格式化
        totalTxt.setText(String.format("%02d:%02d", minute, second));
        startTxt.setText(String.format("%02d:%02d", 0, 0));
    }

    //添加seekBar的监听事件
    private void addSeekbarChangeListener(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //开始播并且设置暂停图片
    public void doPlay() {
        mediaPlayer.start();
        toggle.setImageResource(R.mipmap.dao_pause);
    }

    //暂停播放并设置播放图片
    public void doPause() {
        mediaPlayer.pause();
        toggle.setImageResource(R.mipmap.dao_play);
    }

    public void loadExhibit(Exhibition exhibition, Handler handler) {
        //获取当前展品的信息后，设置展品的title、图片和对应的WebView，播放视频

        String url = Constant.getDefaultFileDir() + "/" + "CHINESE" + "/" + exhibition.getFileNo() + "/" + exhibition.getFileNo() + ".html";
//
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocus();
        // 设置背景色
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///" + url);
        String path = Constant.getDefaultFileDir() + "/" + "CHINESE" + "/" + exhibition.getFileNo() + File.separator + exhibition.getFileNo() + ".mp3";


//        mediaPlayer.reset();
        try {
            //设置播放资源
            mediaPlayer.setDataSource(path);
            //准备播放
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doPlay();
        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        toggle.setImageResource(R.mipmap.dao_play);
        mediaPlayer.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
        if (mediaPlayer!=null){

            mediaPlayer.release();
            mediaPlayer=null;
        }


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将触屏事件交给手势识别类处理
        return this.detector.onTouchEvent(event)&&isFirst;
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