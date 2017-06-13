package com.example.lenovo.hd_beijing_meseum;


import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.ar.UnityPlayerActivity;
import com.example.lenovo.hd_beijing_meseum.defiteview.HDialogBuilder;
import com.example.lenovo.hd_beijing_meseum.utils.CommonUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.ILoadListener;
import com.example.lenovo.hd_beijing_meseum.utils.ShareUtils;
import com.example.lenovo.hd_beijing_meseum.utils.ToastUtils;
import com.example.lenovo.hd_beijing_meseum.view.CollectView;
import com.example.lenovo.hd_beijing_meseum.view.GuideService;
import com.example.lenovo.hd_beijing_meseum.view.IntroWeb;
import com.example.lenovo.hd_beijing_meseum.view.VisitView;

import java.io.File;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView introImg;
    private ImageView guideImg;
    private ImageView collectImg;
    private ImageView visitImg;
    private ImageView ARImg;
    private Boolean isCanUse;
    private HDialogBuilder hDialogBuilder;
    private ShareUtils shareUtils;
    private long mExitTime;
    private File file;
    private ImageButton toBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);


        String path = Constant.getDefaultFileDir() + "CHINESE";
        file = new File(path);
        initView();
        downDB();
        initLisenter();

    }

    private void downDB() {
        if (!HResourceUtil.isDbExist()) {
            if (CommonUtil.isOnline(MainActivity.this)) {
                if (!HdAppConfig.isLoading()) {
                    HdAppConfig.setIsLoading(true);
                    HResourceUtil.showDownloadProgressDialog(MainActivity.this, getString(R
                            .string.initializing));
                    HResourceUtil.downloadDb(new ILoadListener() {
                        @Override
                        public void onLoadSucceed() {
                            ToastUtils.toast(MainActivity.this, "下载成功");
                            HdAppConfig.setIsLoading(false);
                            HResourceUtil.hideDownloadProgressDialog();

                        }

                        @Override
                        public void onLoadFailed() {
                            HdAppConfig.setIsLoading(false);
                            HResourceUtil.hideDownloadProgressDialog();
                            ToastUtils.toast(MainActivity.this, "下载失败");
                        }
                    });

                }
            } else {

                showNetworkConnFailedDialog();

            }
        }
    }


    public boolean cameraIsCanUse() {
        isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /**
     * 提示网络不可用
     */
    private void showNetworkConnFailedDialog() {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(MainActivity.this);
        hDialogBuilder.withIcon(R.mipmap.lanch)

                .title(getString(R.string.warm_tip))
                .message(getString(R.string.net_not_available))
                .nBtnText(getString(R.string.close))
                .pBtnText(getString(R.string.submit))
                .pBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideDialog();
                        Intent intent;
                        if (Build.VERSION.SDK_INT > 10) {
                            intent = new Intent(Settings
                                    .ACTION_WIRELESS_SETTINGS);
                        } else {
                            intent = new Intent();
                            ComponentName component = new ComponentName("com.android" +
                                    ".settings", "com" +
                                    ".android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        startActivity(intent);
                    }
                })
                .nBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideDialog();
                        finish();
                    }
                }).show();
    }

    /**
     * 隐藏Dialog
     */
    private void hideDialog() {
        if (hDialogBuilder != null) {
            hDialogBuilder.dismiss();
            hDialogBuilder = null;
        }
    }

    private void initLisenter() {
        introImg.setOnClickListener(this);
        guideImg.setOnClickListener(this);
        collectImg.setOnClickListener(this);
        visitImg.setOnClickListener(this);
        ARImg.setOnClickListener(this);
        toBack.setOnClickListener(this);
    }

    private void initView() {
        introImg = (ImageView) findViewById(R.id.introduce_meseum);
        guideImg = (ImageView) findViewById(R.id.guide_meseum);
        collectImg = (ImageView) findViewById(R.id.collect_meseum);
        visitImg = (ImageView) findViewById(R.id.visit_meseum);
        toBack = (ImageButton) findViewById(R.id.toback_main);
        ARImg = (ImageView) findViewById(R.id.AR_meseum);
        shareUtils = new ShareUtils(MainActivity.this, "");
        shareUtils.setPrefBoolean("flag", true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.introduce_meseum:
                intent = new Intent(MainActivity.this, IntroWeb.class);
                startActivity(intent);
                break;
            case R.id.guide_meseum:
                intent = new Intent(MainActivity.this, GuideService.class);
                startActivity(intent);
                break;
            case R.id.collect_meseum:
                intent = new Intent(MainActivity.this, CollectView.class);
                startActivity(intent);
                break;
            case R.id.visit_meseum:
                intent = new Intent(MainActivity.this, VisitView.class);
                startActivity(intent);
                break;
            case R.id.AR_meseum:
                if (!cameraIsCanUse()) {
                    Toast.makeText(MainActivity.this, "请打开相机权限", Toast.LENGTH_SHORT).show();

                } else {
                    intent = new Intent(MainActivity.this, UnityPlayerActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.toback_main:
                finish();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }


    }

    /**
     * 双击返回退出
     */
    private void exitBy2Click() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再次点击退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }


    }

}
