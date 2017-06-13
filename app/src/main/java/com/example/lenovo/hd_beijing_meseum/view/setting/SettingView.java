package com.example.lenovo.hd_beijing_meseum.view.setting;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.defiteview.HDialogBuilder;
import com.example.lenovo.hd_beijing_meseum.utils.CommonUtil;
import com.example.lenovo.hd_beijing_meseum.utils.FileUtils;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.HdApplication;
import com.example.lenovo.hd_beijing_meseum.utils.ILoadListener;
import com.example.lenovo.hd_beijing_meseum.utils.ToastUtils;
import com.example.lenovo.hd_beijing_meseum.view.webac.EditInfo;

public class SettingView extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout RL_sourceDown;
    private RelativeLayout RL_sourceCancel;
    private RelativeLayout RL_EditInfo;
    private TextView title;
    private ImageView toBack;
    HDialogBuilder hDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_setting_view);
        initView();
        setListner();
    }

    private void setListner() {
        RL_sourceDown.setOnClickListener(this);
        RL_sourceCancel.setOnClickListener(this);
        RL_EditInfo.setOnClickListener(this);
        toBack.setOnClickListener(this);
    }

    private void initView() {
        RL_sourceDown = (RelativeLayout) findViewById(R.id.source_download);
        RL_sourceCancel = (RelativeLayout) findViewById(R.id.source_cancel);
        RL_EditInfo = (RelativeLayout) findViewById(R.id.editinfo);
        toBack = (ImageView) findViewById(R.id.toback);
        title = (TextView) findViewById(R.id.title);
        title.setText("设置");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.source_download:
                DownResCertain();

                break;
            case R.id.source_cancel:
                new AlertDialog.Builder(SettingView.this).setTitle("资源删除")
                        .setIcon(R.mipmap.lanch)
                        .setView(R.layout.alert_delete)
                        .setNegativeButton("取消",null)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (FileUtils.deleteFile(Constant.getDefaultFileDir()+"/"+"CHINESE")){
                                    ToastUtils.toast(SettingView.this,"删除成功");
                                }else {
                                    ToastUtils.toast(SettingView.this,"删除失败");
                                }
                            }
                        }).show();




                break;
            case R.id.editinfo:
                Intent intent=new Intent(SettingView.this, EditInfo.class);
                startActivity(intent);
                break;
            case R.id.toback:
                finish();
                break;
        }
    }
    /**
     * 显示当前版本Dialog
     */
    private void showVersionDialog() {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(SettingView.this);
        hDialogBuilder
                .withIcon(R.mipmap.lanch)
                .title("版本信息")
                .message("当前版本" + CommonUtil
                        .getAppVersionName(SettingView.this)).pBtnText("关闭")
                .pBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideDialog();
                    }
                })
                .show();
    }
    private void DownResCertain() {
        if (CommonUtil.isOnline(SettingView.this)) {

            if (!HResourceUtil.isMapResExist()){

                if (!HdAppConfig.isLoading()) {
                    HdAppConfig.setIsLoading(true);
                    HResourceUtil.showDownloadDialog(SettingView.this);
                    HResourceUtil.downloadRes(SettingView.this, new ILoadListener() {
                        @Override
                        public void onLoadSucceed() {
                            HdAppConfig.setIsLoading(false);
                            HdAppConfig.setIsFirstUse(false);
                            HResourceUtil.hideDownloadDialog();
                            CommonUtil.showToast(SettingView.this, getString(R.string
                                    .load_succeed));
                        }

                        @Override
                        public void onLoadFailed() {
                            HdAppConfig.setIsLoading(false);
                            HResourceUtil.hideDownloadDialog();
                            CommonUtil.showToast(SettingView.this, getString(R.string
                                    .load_failed));
                        }
                    });
                }
        }else {
                new AlertDialog.Builder(SettingView.this).setTitle("资源下载")
                    .setIcon(R.mipmap.lanch)
                    .setView(R.layout.alert_exist)
                    .setNegativeButton("取消",null)
                    .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (FileUtils.deleteFile(Constant.getDefaultFileDir() + "/" + "CHINESE" + "/" + "map")) {
                                if (!HdAppConfig.isLoading()) {
                                    HdAppConfig.setIsLoading(true);
                                    HResourceUtil.showDownloadDialog(SettingView.this);
                                    HResourceUtil.downloadRes(SettingView.this, new ILoadListener() {
                                        @Override
                                        public void onLoadSucceed() {
                                            HdAppConfig.setIsLoading(false);
                                            HdAppConfig.setIsFirstUse(false);
                                            HResourceUtil.hideDownloadDialog();
                                            CommonUtil.showToast(SettingView.this, getString(R.string
                                                    .load_succeed));
                                        }

                                        @Override
                                        public void onLoadFailed() {
                                            HdAppConfig.setIsLoading(false);
                                            HResourceUtil.hideDownloadDialog();
                                            CommonUtil.showToast(SettingView.this, getString(R.string
                                                    .load_failed));
                                        }
                                    });
                                }
                            }
                        }
                    }).show();

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
        hDialogBuilder = new HDialogBuilder(SettingView.this);
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
}
