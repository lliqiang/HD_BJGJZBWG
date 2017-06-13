package com.example.lenovo.hd_beijing_meseum.Constant;

import android.os.Environment;

/**
 * Created by $ shiwei.bai on 2016/9/18.
 */

public class Constant {
    public static final String DB_FILE_NAME = "Filemanage.sqlite";
    public static final String IS_LOADING = "IS_LOADING";
    public static final String DATABASE_STR="http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HangZhou_Res%2FDATABASE.zip";
    public static String getDefaultFileDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/HD_BJ_Meseum/";
    }
    public static String getDbFilePath() {
        return getDefaultFileDir() + DB_FILE_NAME;
    }
     //  获取地图文件路径
    public static String getDefaultMapFilePath() {
        return getDefaultFileDir() + "CHINESE"+"/"+"map" ;
    }

    //    获取数据库文件路径
    public static String getDefaultDbFilePath() {
        return getDefaultFileDir() + DB_FILE_NAME;
    }
}






