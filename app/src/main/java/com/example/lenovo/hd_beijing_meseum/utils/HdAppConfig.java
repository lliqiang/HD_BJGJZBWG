package com.example.lenovo.hd_beijing_meseum.utils;
import android.text.TextUtils;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;


/**
 * 作者：Tailyou
 * 时间：2016/1/11 10:05
 * 邮箱：tailyou@163.com
 * 描述：恒达App配置文件
 */
public class HdAppConfig {
    //    数据库文件名称
    public static final String DB_FILE_NAME = "filemanage.s3db";
    //    SharedPrf配置文件名称
    public static final String DEFAULT_SHARED_PREF_NAME = "HZ_BWG_PREF";
    public static final String IS_FIRST_USE = "IS_FIRST_USE";
    public static final String POWER_PERMI = "POWER_PERMI";//禁止关机下是否获取到关机权限：0无，1有
    public static final String POWER_MODE = "POWER_MODE";//关机权限：0禁止，1允许
    public static final String RECEIVE_NO_MODE = "RECEIVE_NO_MODE";//收号方式：0蓝牙，1RFID，2混合
    public static final String STC_MODE = "STC_MODE";//报警方式：0直接报警，1间接报警
    public static final String PASSWORD = "PASSWORD";//管理员密码
    public static final String IS_LOADING = "IS_LOADING";
    public static final String DEFAULT_DISTANCE = "DEFAULT_DISTANCE";
    public static final String AUTO_MODE = "AUTO_MODE";//讲解方式：0隔一，1连续
    public static final String AUTO_FLAG = "AUTO_FLAG";//自动讲解：0关闭，1开启
    public static final String SCREEN_MODE = "SCREEN_MODE";//节能模式：0关闭，1开启
    public static final String SMART_SERVICE = "SMART_SERVICE";//智慧服务：0关闭，1开启
    public static final String IP_PORT = "IP_PORT";//服务器IP和端口
    public static final String RSSI = "RSSI";//RSSI门限
    public static final String LANGUAGE = "LANGUAGE";//当前语种
    //    万能登录密码
    public static final String LOGIN_PWD = "666666";
    //    默认管理员密码
    public static final String DEFAULT_PWD = "9999";
    //    默认设备号
    public static final String DEFAULT_DEVICE_NO = "AG10000000000";

    private static SharedPrefUtil appConfigShare = new SharedPrefUtil(HdApplication.mContext,
            DEFAULT_SHARED_PREF_NAME);
//
    public static void setIsFirstUse(boolean isFirstUse) {
        appConfigShare.setPrefBoolean(IS_FIRST_USE, isFirstUse);
    }

    public static boolean isFirstUse() {
        return appConfigShare.getPrefBoolean(IS_FIRST_USE, true);
    }
//
//    public static void setLanguage(String language) {
//        appConfigShare.setPrefString(LANGUAGE, language);
//    }

    public static void setIsLoading(boolean isLoading) {
        appConfigShare.setPrefBoolean(IS_LOADING, isLoading);
    }
    public static void setSTCMode(int flag) {
        appConfigShare.setPrefInt(STC_MODE, flag);
    }

    public static int getSTCMode() {
        return appConfigShare.getPrefInt(STC_MODE, 1);
    }
    public static boolean isLoading() {
        return appConfigShare.getPrefBoolean(IS_LOADING, false);
    }

    public static void setReceiveNoMode(int receiveNoMode) {
        appConfigShare.setPrefInt(RECEIVE_NO_MODE, receiveNoMode);
    }

    public static int getReceiveNoMode() {
        return appConfigShare.getPrefInt(RECEIVE_NO_MODE, 1);
    }

    public static void setDeviceNo(String deviceNo) {
        FileUtils.writeStringToFile(Constant.getDefaultFileDir() + "DeviceNo.txt",
                deviceNo, false);
    }

    public static String getDeviceNo() {
        StringBuilder deviceNo = FileUtils.readStringFromFile(Constant.getDefaultFileDir()
                + "DeviceNo.txt", "UTF-8");
        return TextUtils.isEmpty(deviceNo) ? HdConstants.DEFAULT_DEVICE_NO : deviceNo.toString();
    }


    public static void setPowerPermi(int flag) {
        appConfigShare.setPrefInt(POWER_PERMI, flag);
    }

    public static int getPowerPermi() {
        return appConfigShare.getPrefInt(POWER_PERMI, 0);
    }


    public static void setPowerMode(int flag) {
        appConfigShare.setPrefInt(POWER_MODE, flag);
    }

    public static int getPowerMode() {
        return appConfigShare.getPrefInt(POWER_MODE, 1);
    }

    public static void clear() {
        appConfigShare.clearPreference();
    }



    public static void setPassword(String password) {
        appConfigShare.setPrefString(PASSWORD, password);
    }

    public static String getPassword() {
        return appConfigShare.getPrefString(PASSWORD, HdConstants.DEFAULT_PWD);
    }
    public static void setAutoMode(int autoMode) {
        appConfigShare.setPrefInt(AUTO_MODE, autoMode);
    }

    public static int getAutoMode() {
        return appConfigShare.getPrefInt(AUTO_MODE, 0);
    }

    public static void setAutoFlag(int autoFlag) {
        appConfigShare.setPrefInt(AUTO_FLAG, autoFlag);
    }

    public static int getAutoFlag() {
        return appConfigShare.getPrefInt(AUTO_FLAG, 1);
    }
    public static void setScreenMode(int flag) {
        appConfigShare.setPrefInt(SCREEN_MODE, flag);
    }

    public static int getScreenMode() {
        return appConfigShare.getPrefInt(SCREEN_MODE, 1);
    }

    public static void setSmartService(int smartService) {
        appConfigShare.setPrefInt(SMART_SERVICE, smartService);
    }

    public static int getSmartService() {
        return appConfigShare.getPrefInt(SMART_SERVICE, 1);
    }
    public static void setRssi(int rssi) {
        appConfigShare.setPrefInt(RSSI, rssi);
    }

    public static int getRssi() {
        return appConfigShare.getPrefInt(RSSI, HdConstants.BLE_RSSI_THRESHOLD);
    }

    public static void setDefaultIpPort(String ipPort) {
        appConfigShare.setPrefString(IP_PORT, ipPort);
    }

    public static String getDefaultIpPort() {
        return appConfigShare.getPrefString(IP_PORT, HdConstants.DEFAULT_IP_PORT);
    }

    public static void setLanguage(String language) {
        appConfigShare.setPrefString(LANGUAGE, language);
    }

    public static String getLanguage() {
        return appConfigShare.getPrefString(LANGUAGE, HdConstants.LANG_DEFAULT);
    }
}
