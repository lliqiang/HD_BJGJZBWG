package com.example.lenovo.hd_beijing_meseum;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.autono.AutoNoUtil;
import com.example.lenovo.hd_beijing_meseum.autono.BleReceiver;
import com.example.lenovo.hd_beijing_meseum.autono.RfidReceiver;
import com.example.lenovo.hd_beijing_meseum.autono.SystemSetting;
import com.example.lenovo.hd_beijing_meseum.event.BestBleNoEvent;
import com.example.lenovo.hd_beijing_meseum.event.DeviceNoEvent;
import com.example.lenovo.hd_beijing_meseum.rxbus.RxBusUtil;
import com.example.lenovo.hd_beijing_meseum.update.CheckCallback;
import com.example.lenovo.hd_beijing_meseum.update.CheckResponse;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.HdApplication;
import com.example.lenovo.hd_beijing_meseum.utils.NetStateMonitor;
import com.example.lenovo.hd_beijing_meseum.utils.SDCardUtil;
import com.orhanobut.logger.Logger;
import java.io.File;
import java.io.OutputStream;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
public class LauchActivity extends RfidReceiver{


    @Bind(R.id.imgAdmin)
    ImageView imgAdmin;
    @Bind(R.id.imgPower)
    ImageView imgPower;
    @Bind(R.id.imgEnter)
    ImageView imgEnter;
    @Bind(R.id.txtDisplayAutoNo)
    TextView txtDisplayAutoNo;
    AutoNoUtil autoNoUtil;
    Subscription bleNoSubscription;
    Subscription deviceNoSubscription;
    final int WHAT_CHECK_SDCARD = 1001;
    int j = 0;
    NetStateMonitor netStateMonitor;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_CHECK_SDCARD:
                    if (SDCardUtil.isSDCardEnable()) {
                        imgAdmin.setVisibility(View.VISIBLE);
                        imgEnter.setVisibility(View.VISIBLE);
                        txtDisplayAutoNo.setVisibility(View.VISIBLE);
                        txtDisplayAutoNo.setText(HdAppConfig.getDeviceNo());
                        registerBLEReceiver();
                        //SD卡挂载成功后，注册BLE收号服务
                        //重置所有展品为未读状态
                        File file = new File(Constant.getDbFilePath());
                        if (file.exists()) {
                            //TODO 重置DATABASE
                        }
                    } else {
//                        txtLoading.setText("正在加载，请稍后..." + j);
                        j++;
                        handler.sendEmptyMessageDelayed(WHAT_CHECK_SDCARD, 1000);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lauch);
        ButterKnife.bind(this);
        autoNoUtil = AutoNoUtil.getInstance(5, 3);
        handler.sendEmptyMessage(WHAT_CHECK_SDCARD);
        initUI();
        //开机后需重新获取关机权限
        HdAppConfig.setPowerPermi(0);
        //注册RFID收号器
        registerRFIDReceiver();
        //注册网络状态监听器
        registerNetStateMonitor();
        //订阅设备号修改
        subscribeDeviceNoEvent();
        txtDisplayAutoNo.setText(HdAppConfig.getDeviceNo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPowerMode();
        initStcMode();
    }
    /**
     * 注册蓝牙收号
     */
    private void registerBLEReceiver() {
        startService(new Intent(LauchActivity.this, BleReceiver.class));
        RxBusUtil.subscribeEvent(BestBleNoEvent.class, bestBleNoEvent -> {
            Logger.e("---BLE---" + bestBleNoEvent.getAutoNo());
            onReceiveAutoNo(bestBleNoEvent.getAutoNo());
        });
    }
    /**
     * 订阅设备号修改
     */
    private void subscribeDeviceNoEvent() {
        RxBusUtil.subscribeEvent(DeviceNoEvent.class,
                deviceNoEvent -> txtDisplayAutoNo.setText(HdAppConfig.getDeviceNo()));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterRFIDReceiver();
        unregisterBLEReceiver();
        unregisterReceiver(netStateMonitor);
        RxBusUtil.unsubscribe(deviceNoSubscription);
        RxBusUtil.unsubscribe(bleNoSubscription);
    }

    /**
     * 解除注册蓝牙收号
     */
    private void unregisterBLEReceiver() {
        stopService(new Intent(LauchActivity.this, BleReceiver.class));
        RxBusUtil.unsubscribe(bleNoSubscription);
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        runOnUiThread(() -> {
            if (HdAppConfig.getReceiveNoMode() != 0) {
                for (int m = 0; m < 8; m++) {
                    if (buffer[m] == (byte) 0xAA && buffer[m + 1] == (byte) 0x55) {
                        if ((buffer[m + 2] == (byte) 0x05)
                                && buffer[m + 6] == (buffer[m] ^ buffer[m + 1]
                                ^ buffer[m + 2] ^ buffer[m + 3]
                                ^ buffer[m + 4] ^ buffer[m + 5])) {
                            int rfidNum = (buffer[m + 4] & 0x0FF) * 256
                                    + (buffer[m + 5] & 0x0FF);
                            autoNoUtil.addAutoNo(rfidNum);
                            int bestRfidNo = autoNoUtil.getBestAutoNo();
                            if (bestRfidNo != 0) {
                                Logger.e("---RFID---" + bestRfidNo);
                                onReceiveAutoNo(bestRfidNo);
                            }
                        }
                    }
                }
            }
        });
    }


    /**
     * 注册网络状态监听器
     */
    private void registerNetStateMonitor() {
        netStateMonitor = new NetStateMonitor() {
            @Override
            public void onConnected() {
                checkNewVersion(new CheckCallback() {
                    @Override
                    public void hasNewVersion(CheckResponse checkResponse) {
                        showHasNewVersionDialog(checkResponse);
                    }
                });
            }

            @Override
            public void onDisconnected() {
                Toast.makeText(LauchActivity.this, "网络不可用，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateMonitor, mFilter);
    }

    /**
     * 初始化报警模式
     */
    private void initStcMode() {
        if (HdAppConfig.getSTCMode() == 1) {
            //直接报警
            SystemSetting.set_STC_Mode(mOutputStream, (byte) 0XF0);
        } else {
            //间接报警
            SystemSetting.set_STC_Mode(mOutputStream, (byte) 0XF8);
        }
    }

    /**
     * 禁用返回
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return false;
    }

    /**
     * 初始化UI
     */
    private void initUI() {

        txtDisplayAutoNo.setTypeface(HdApplication.typeface);
        imgAdmin.setOnClickListener(v -> startActivity(new Intent(LauchActivity.this,
                LoginActivity.class).setAction("ADMIN")));
        imgPower.setOnClickListener(v -> startActivity(new Intent(LauchActivity.this,
                LoginActivity.class).setAction("POWER")));
        imgEnter.setOnClickListener(v -> startActivity(new Intent(LauchActivity.this,
                MainActivity.class)));
    }




    /**
     * 初始化关机权限
     */
    private void initPowerMode() {
        if (HdAppConfig.getPowerMode() == 1) {
            sendBroadcast(new Intent("android.intent.action.ALLOW_POWERDOWN"));
            imgPower.setVisibility(View.GONE);
        } else {
            if (HdAppConfig.getPowerPermi() == 1) {
                sendBroadcast(new Intent("android.intent.action.ALLOW_POWERDOWN"));
                imgPower.setVisibility(View.GONE);
            } else {
                sendBroadcast(new Intent("android.intent.action.LIMIT_POWERDOWN"));
                imgPower.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 收号之后的操作
     *
     * @param autoNo
     */
    private void onReceiveAutoNo(int autoNo) {
        switch (autoNo) {
            case 2001:

            case 2002:
                //TODO 开启报警
                break;
            case 2003:
                //TODO 关闭报警
                break;
            default:
                //TODO 收号分发
                break;
        }
    }



}
