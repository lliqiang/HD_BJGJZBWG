package com.example.lenovo.hd_beijing_meseum.autono;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.lenovo.hd_beijing_meseum.event.BestBleNoEvent;
import com.example.lenovo.hd_beijing_meseum.rxbus.RxBus;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.orhanobut.logger.Logger;


import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/5 13:53
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class BleReceiver extends Service implements BeaconConsumer {

    // 连续 LINKED_LIST_SIZE 个AutoNo，
    // 出现 LINKED_LIST_THRESHOLD 个一样的AutoNo 为收号成功
    public static final int LINKED_LIST_SIZE = 5;
    public static final int LINKED_LIST_THRESHOLD = 3;
    public static final int DISTANCE_THRESHOLD = 6;

    BeaconManager mBeaconManager;
    BleStatusMonitor mBleStatusMonitor;
    AutoNoUtil autoNoUtil;

    // BLE
    List<Beacon> mSortedBeaconList = Collections.emptyList();
    List<Beacon> mValidBeaconList = Collections.emptyList();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        autoNoUtil = AutoNoUtil.getInstance(LINKED_LIST_SIZE, LINKED_LIST_THRESHOLD);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        initBeaconManager();
        Logger.e("启动收号服务！");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e("关闭收号服务！");
        if (mBeaconManager != null)
            mBeaconManager.unbind(this);
        if (mBleStatusMonitor != null)
            unregisterReceiver(mBleStatusMonitor);
    }

    /**
     * 初始化 BeaconManager
     */
    private void initBeaconManager() {
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215," +
                "i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.setForegroundScanPeriod(800);
        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter.class);
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(3200L);
        mBeaconManager.bind(this);
        //注册蓝牙状态变化监听器
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mBleStatusMonitor = new BleStatusMonitor();
        registerReceiver(mBleStatusMonitor, filter);
    }

    /**
     * BLE收号
     */
    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.setRangeNotifier((collection, region) -> {
            if (collection.size() > 0 && HdAppConfig.getAutoMode() != 1) {
                mSortedBeaconList = sortBeacons(collection);
                mValidBeaconList = filterBeacons(mSortedBeaconList);
                if (mValidBeaconList.size() > 0) {
                    //发送最好的一个AutoNo
                    int autoNo = mValidBeaconList.get(0).getId2().toInt();
                    autoNoUtil.addAutoNo(autoNo);
                    int bestBleNo = autoNoUtil.getBestAutoNo();
                    if (bestBleNo != 0) {
                        RxBus.getDefault().post(new BestBleNoEvent(bestBleNo));
                    }
                }
            }
        });
        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("UniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * BeaconList 排序
     *
     * @author 祝文飞（Tailyou）
     * @time 2016/11/3 12:15
     */
    private List<Beacon> sortBeacons(Collection<Beacon> collection) {
        List<Beacon> beaconList = new ArrayList<>();
        beaconList.addAll(collection);
        Collections.sort(beaconList, (lhs, rhs) -> lhs.getDistance() < rhs.getDistance() ? -1 : 1);
        return beaconList;
    }

    /**
     * BeaconList 过滤
     *
     * @author 祝文飞（Tailyou）
     * @time 2016/11/3 10:05
     */
    public List<Beacon> filterBeacons(List<Beacon> beaconList) {
        List<Beacon> validBeaconList = new ArrayList<>();
        Observable.from(beaconList)
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(beacon -> beacon.getDistance() < DISTANCE_THRESHOLD)
                .subscribe(beacon -> {
                    validBeaconList.add(beacon);
                });
        return validBeaconList;
    }

    /**
     * 监听蓝牙状态变化
     *
     * @author 祝文飞（Tailyou）
     * @time 2016/11/3 9:42
     */
    private class BleStatusMonitor extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Logger.e("关闭蓝牙");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            break;
                    }
                    break;
            }
        }
    }

}
