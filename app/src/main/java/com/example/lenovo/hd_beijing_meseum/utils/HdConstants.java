package com.example.lenovo.hd_beijing_meseum.utils;

import java.text.SimpleDateFormat;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 17:19
 * 邮箱：tailyou@163.com
 * 描述：全局常量
 */
public class HdConstants {

    public static final int LOADING_DB = 11;
    public static final int LOADING_RES = 12;

    //    App更新相关常量
    public static String APP_KEY = "75ea5fb3742aaaf433aaac4be085b2ef";
    public static String APP_SECRET = "6bccb700ea34e2288170349ef3fa31ee";
    public static String APP_UPDATE_URL = "http://101.200.234.14/APPCloud/";





    //    中文时间格式
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm");
    //    中文数字
    public static final String[] CHN_NUM = {"一", "二", "三", "四", "五", "六", "七"};

    //    SharedPrf配置文件名称
    public static final String SHARED_PREF_NAME = "HD_YQH_PREF";

    //    数据库文件名称
    public static final String DB_FILE_NAME = "filemanage.s3db";

    //    蓝牙RSSI门限
    public static final int BLE_RSSI_THRESHOLD = -69;

    //    测试平台包名
    public static final String TEST_APP_PACKAGE_NAME = "com.hengda.apptest";
    //    测试平台LauncherActivity
    public static final String TEST_APP_LAUNCHER_ACT = "com.hengda.apptest.HD_AppTestActivity";

    //    万能登录密码
    public static final String LOGIN_PWD = "666666";
    //    默认管理员密码
    public static final String DEFAULT_PWD = "9999";
    //    默认设备号
    public static final String DEFAULT_DEVICE_NO = "AG10000000000";

    public static final String LANG_DEFAULT = "CHINESE";

    //   -默认网络请求服务器地址
    public static final String DEFAULT_IP_PORT = "192.168.10.28";
    //    请求成功状态码
    public static final String HTTP_STATUS_SUCCEED = "000";

}
