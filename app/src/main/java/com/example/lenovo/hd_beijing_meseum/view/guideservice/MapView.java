package com.example.lenovo.hd_beijing_meseum.view.guideservice;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.defiteview.HDialogBuilder;
import com.example.lenovo.hd_beijing_meseum.utils.CommonUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.ILoadListener;
import me.yokeyword.fragmentation.SupportActivity;

public class MapView extends SupportActivity implements View.OnClickListener {
    private ImageView toBack;
    private RadioGroup radioGroup;
    private RelativeLayout container;
    private TextView title;
    FragmentTransaction ft;
    MapFragment mapFloorOne;
    MapFragment mapFloorTwo;
    RadioButton rbBai;
    private RadioButton rbTai;
    RadioButton rbXIPEI;
    HDialogBuilder hDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_map_view);
        initView();
        if (!HResourceUtil.isMapResExist()) {
            new AlertDialog.Builder(MapView.this).setTitle("资源下载")
                    .setIcon(R.mipmap.lanch)
                    .setView(R.layout.alert_layout)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initData();
                        }
                    }).show();
        }

        toBack.setOnClickListener(this);
        setListner();
    }

    @Override
    protected int setContainerId() {
        return 0;
    }

    private void setListner() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.baidian:

                        rbBai.setTextColor(Color.WHITE);
                        rbTai.setTextColor(Color.BLACK);
                        rbXIPEI.setTextColor(Color.BLACK);
                        showMapFloorOne();

                        break;
                    case R.id.taisui:
                        rbTai.setTextColor(Color.WHITE);
                        rbBai.setTextColor(Color.BLACK);
                        rbXIPEI.setTextColor(Color.BLACK);
                        showMapFloorTwo();
                        break;
                    case R.id.xipei:

                        rbTai.setTextColor(Color.BLACK);
                        rbBai.setTextColor(Color.BLACK);
                        rbXIPEI.setTextColor(Color.WHITE);
                        Toast.makeText(MapView.this, "开发中", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        });
        showMapFloorOne();
    }


    private void initData() {
        if (CommonUtil.isOnline(MapView.this)) {


            if (!HdAppConfig.isLoading()) {
                HdAppConfig.setIsLoading(true);
                HResourceUtil.showDownloadDialog(MapView.this);
                HResourceUtil.downloadRes(MapView.this, new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        HdAppConfig.setIsLoading(false);
                        HdAppConfig.setIsFirstUse(false);
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(MapView.this, getString(R.string
                                .load_succeed));
                    }

                    @Override
                    public void onLoadFailed() {
                        HdAppConfig.setIsLoading(false);
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(MapView.this, getString(R.string
                                .load_failed));
                    }
                });
            }

        } else {
            showNetworkConnFailedDialog();
        }
    }

    /**
     * 提示网络不可用
     */
    private void showNetworkConnFailedDialog() {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(MapView.this);
        hDialogBuilder.withIcon(R.mipmap.ic_launcher)
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

    private void initView() {
        toBack = (ImageView) findViewById(R.id.toback);
        radioGroup = (RadioGroup) findViewById(R.id.rg_map);
        container = (RelativeLayout) findViewById(R.id.fragment_container);
        toBack = (ImageView) findViewById(R.id.toback);
        title = (TextView) findViewById(R.id.title);
        rbBai = (RadioButton) findViewById(R.id.baidian);
        rbTai = (RadioButton) findViewById(R.id.taisui);
        rbXIPEI = (RadioButton) findViewById(R.id.xipei);
        title.setText("地图导览");
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

    @Override
    public void onClick(View v) {
        finish();
    }

    /**
     * 显示一层地图
     */
    private void showMapFloorOne() {
        ft = getSupportFragmentManager().beginTransaction();
        if (mapFloorTwo != null) {
            ft.hide(mapFloorTwo);
        }
        if (mapFloorOne == null) {
            mapFloorOne = MapFragment.getInstance(1);
            startBrotherFragment(R.id.fragment_container, mapFloorOne, false);
        }
        ft.show(mapFloorOne);
        ft.commitAllowingStateLoss();
    }

    /**
     * 显示二层地图
     */
    private void showMapFloorTwo() {
        ft = getSupportFragmentManager().beginTransaction();
        if (mapFloorOne != null) {
            ft.hide(mapFloorOne);
        }
        if (mapFloorTwo == null) {
            mapFloorTwo = MapFragment.getInstance(2);
            startBrotherFragment(R.id.fragment_container, mapFloorTwo, false);
        }
        ft.show(mapFloorTwo);
        ft.commitAllowingStateLoss();
    }

    public void startBrotherFragment(int brotherContainer, Fragment brotherFragment, boolean addToBack) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(brotherContainer, brotherFragment, brotherFragment.getClass().getName())
                .show(brotherFragment);
        if (addToBack) {
            ft.addToBackStack(brotherFragment.getClass().getName());
        }
        ft.commit();
    }
}
