package com.example.lenovo.hd_beijing_meseum.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wenda on 15/10/20.
 */
public class ToastUtils {
    public static void toast(Context cxt, int id) {
        Toast.makeText(cxt, id, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context cxt, String str) {
        Toast.makeText(cxt, str, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context ctx, Exception e) {
        if (e != null) {
            toast(ctx, e.getMessage());
        }
    }
}
