package com.example.lenovo.hd_beijing_meseum.ar;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;

public class UnityPlayerActivity extends Activity {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code


    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new UnityPlayer(this);
        if (mUnityPlayer.getSettings().getBoolean("hide_status_bar", true)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(mUnityPlayer);

            mUnityPlayer.requestFocus();

    }


    // Quit Unity
    @Override
    protected void onDestroy() {
//		mUnityPlayer.quit();
        System.gc();
        super.onDestroy();
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();


    }



    // 判断是否缺少权限
    private boolean lacksPermission() {
        int a = getApplicationContext().checkCallingPermission("android.permission.CAMERA");


        return a == -1;
    }


    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event)
//	{
//		if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
//			return mUnityPlayer.injectEvent(event);
//		return super.dispatchKeyEvent(event);
//	}

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mUnityPlayer.quit();
        finish();

        return mUnityPlayer.injectEvent(event);
    }
//	@Override
//	public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
//	/*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
