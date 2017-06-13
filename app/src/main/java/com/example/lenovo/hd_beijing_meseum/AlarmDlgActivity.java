package com.example.lenovo.hd_beijing_meseum;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.HdApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlarmDlgActivity extends Activity  {

    @Bind(R.id.tvWarmTip)
    TextView tvWarmTip;
    @Bind(R.id.tvTipInfo)
    TextView tvTipInfo;
    @Bind(R.id.tvSure)
    TextView tvSure;
    AudioManager mAudioManager;
    MediaPlayer mMediaPlayer;
    public static AlarmDlgActivity mInstance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        mInstance = AlarmDlgActivity.this;
        setFinishOnTouchOutside(false);
        initAudioManager();
        initPlayer();
        tvWarmTip.setTypeface(HdApplication.typeface);
        tvTipInfo.setTypeface(HdApplication.typeface);
        tvSure.setTypeface(HdApplication.typeface);
        tvSure.setOnClickListener(v -> finish());
    }

    protected void onDestroy() {
        super.onDestroy();
        mInstance = null;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return false;
    }

    private void initAudioManager() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    public void initPlayer() {
        switch (HdAppConfig.getLanguage()) {
            case "CHINESE":
                mMediaPlayer = MediaPlayer.create(this, R.raw.chinese);
                break;

        }




    mMediaPlayer.setLooping(true);
    mMediaPlayer.start();
}


}
