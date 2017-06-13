package com.example.lenovo.hd_beijing_meseum.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import android_serialport_api.SerialPort;
import com.orhanobut.logger.Logger;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;




/**
 * 作者：Tailyou
 * 时间：2016/1/8 10:42
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class HdApplication extends Application {
    /**
     * 开发状态枚举
     */
    public static DevelopStatus status = DevelopStatus.DEVELOPING;
    /**
     * 全局上下文环境
     */
    public static Context mContext;
    /**
     * 全局字体样式
     */
    public static Typeface typeface;
    private static SerialPort mSerialPort;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("HD_SMART");
        mContext = getApplicationContext();
        typeface = Typeface.createFromAsset(getAssets(), "fonts/hd.TTF");

        if (status == DevelopStatus.TEST) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
    }

    /**
     * 获取串口
     *
     * @return
     * @throws SecurityException
     * @throws IOException
     * @throws InvalidParameterException
     */
    public static SerialPort getSerialPort() throws
            SecurityException,
            IOException,
            InvalidParameterException {
        if (mSerialPort == null) {
            String path = "/dev/s3c2410_serial3";
            int baudrate = 57600;
            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    /**
     * 关闭串口
     */
    public static void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

}
