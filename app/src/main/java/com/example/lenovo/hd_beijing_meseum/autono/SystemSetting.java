package com.example.lenovo.hd_beijing_meseum.autono;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

public class SystemSetting {

    // -----------OPEN_FM----------
    public static void open_FM(OutputStream mOutputStream) {
        byte[] buffer = new byte[6];
        buffer[0] = (byte) 0xAA;
        buffer[1] = (byte) 0x55;
        buffer[2] = (byte) 0x01;
        buffer[3] = (byte) 0x01;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[5] = (byte) (buffer[0] ^ buffer[1] ^ buffer[2] ^ buffer[3] ^ buffer[4]);
        try {
            mOutputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --------CLOSE_FM---------------
    public static void close_FM(OutputStream mOutputStream) {
        byte[] buffer = new byte[6];
        buffer[0] = (byte) 0xAA;
        buffer[1] = (byte) 0x55;
        buffer[2] = (byte) 0x01;
        buffer[3] = (byte) 0x02;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[5] = (byte) (buffer[0] ^ buffer[1] ^ buffer[2] ^ buffer[3] ^ buffer[4]);
        try {
            mOutputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ------------SET FM CHANNEL-------
    public static void set_FM_Channel(OutputStream mOutputStream, int channel) {
        byte[] buffer = new byte[6];
        buffer[0] = (byte) 0xAA;
        buffer[1] = (byte) 0x55;
        buffer[2] = (byte) 0x02;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x88;

        if (channel > 760 && channel < 1080) {
            buffer[3] = (byte) (channel / 256);
            buffer[4] = (byte) (channel % 256);
            buffer[5] = (byte) (buffer[0] ^ buffer[1] ^ buffer[2] ^ buffer[3] ^ buffer[4]);
            try {
                mOutputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // -------SET Alarm_Mode---------------
    public static void set_STC_Mode(OutputStream mOutputStream, byte para) {
        byte[] buffer = new byte[6];
        buffer[0] = (byte) 0xAA;
        buffer[1] = (byte) 0x55;
        buffer[2] = (byte) 0x09;
        buffer[3] = para;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[5] = (byte) (buffer[0] ^ buffer[1] ^ buffer[2] ^ buffer[3] ^ buffer[4]);
        try {
            mOutputStream.write(buffer);
            Log.e("SET", "SUCCESS");
        } catch (IOException e) {
            Log.e("SET", "ERROR");
            e.printStackTrace();
        }
    }

}
