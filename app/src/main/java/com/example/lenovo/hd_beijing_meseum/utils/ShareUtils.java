package com.example.lenovo.hd_beijing_meseum.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lenovo on 2016/11/2.
 */

public class ShareUtils {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public ShareUtils(Context context, String fileName) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sp.edit();
    }
    public void setPrefBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getPrefBoolean(String key) {
        return sp.getBoolean(key, false);
    }
}
