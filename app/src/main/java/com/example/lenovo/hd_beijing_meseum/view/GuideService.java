package com.example.lenovo.hd_beijing_meseum.view;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.defiteview.HDialogBuilder;
import com.example.lenovo.hd_beijing_meseum.utils.CommonUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.HdApplication;
import com.example.lenovo.hd_beijing_meseum.utils.ILoadListener;
import com.example.lenovo.hd_beijing_meseum.view.digitalplay.DigitalView;
import com.example.lenovo.hd_beijing_meseum.view.guideservice.MapView;
import com.example.lenovo.hd_beijing_meseum.view.listguide.ListDetailGuide;
import com.example.lenovo.hd_beijing_meseum.view.setting.SettingView;

public class GuideService extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mapRL;
    private RelativeLayout listRL;
    private RelativeLayout numRL;
    private RelativeLayout settingRL;
    private TextView titleText;
    private ImageView toBack;
private  HDialogBuilder hDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_guide_service);
        initView();
        setListner();
    }

    private void setListner() {
        mapRL.setOnClickListener(this);
        listRL.setOnClickListener(this);
        numRL.setOnClickListener(this);
        settingRL.setOnClickListener(this);
        toBack.setOnClickListener(this);
    }

    private void initView() {
        mapRL = (RelativeLayout) findViewById(R.id.RL_Map);
        listRL = (RelativeLayout) findViewById(R.id.RL_List);
        numRL = (RelativeLayout) findViewById(R.id.RL_Num);
        toBack = (ImageView) findViewById(R.id.toback);
        settingRL = (RelativeLayout) findViewById(R.id.RL_Setting);
        titleText = (TextView) findViewById(R.id.title);
        titleText.setText("导览服务");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.RL_Map:
                //不下载地图资源，直接跳转
                 intent=new Intent(GuideService.this,MapView.class);
                startActivity(intent);
                break;
            case R.id.RL_List:
                intent = new Intent(GuideService.this, ListDetailGuide.class);
                startActivity(intent);
                break;
            case R.id.RL_Num:
                intent = new Intent(GuideService.this, DigitalView.class);
                startActivity(intent);
                break;
            case R.id.RL_Setting:
                intent = new Intent(GuideService.this, SettingView.class);
                startActivity(intent);
                break;
            case R.id.toback:
                finish();
                break;
        }
    }
    private void loadMapRes() {
        if (!HResourceUtil.isMapResExist()) {
            if (CommonUtil.isOnline(GuideService.this)) {
                if (!HdAppConfig.isLoading()) {
                    HdAppConfig.setIsLoading(true);
                    HResourceUtil.showDownloadDialog(GuideService.this);
                    HResourceUtil.downMapRes(GuideService.this, new ILoadListener() {
                        @Override
                        public void onLoadSucceed() {
                            HdAppConfig.setIsLoading(false);
                            HResourceUtil.hideDownloadDialog();
                          //执行跳转地图操作
                            HdAppConfig.setIsFirstUse(false);
                            Intent intent=new Intent(GuideService.this,MapView.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onLoadFailed() {
                            HdAppConfig.setIsLoading(false);
                            HResourceUtil.hideDownloadDialog();
                            CommonUtil.showToast(GuideService.this, GuideService.this.getString(R.string
                                    .load_failed));
                        }
                    });
                }
            } else {
                showNetworkConnFailedDialog();
            }
        } else {
          //执行跳转操作
            Intent intent=new Intent(GuideService.this,MapView.class);
            startActivity(intent);
        }
    }
    /**
     * 提示网络不可用
     */
    private void showNetworkConnFailedDialog() {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(GuideService.this);
        hDialogBuilder
                .withIcon(R.mipmap.ic_launcher)
//                .setTypeface(HdApplication.getTypeface())
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
                    }
                }).show();
    }

    private void hideDialog() {
        if (hDialogBuilder != null) {
            hDialogBuilder.dismiss();
            hDialogBuilder = null;
        }
    }
}
