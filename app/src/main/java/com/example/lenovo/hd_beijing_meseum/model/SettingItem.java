package com.example.lenovo.hd_beijing_meseum.model;

/**
 * Created by lenovo on 2017/2/28.
 */

public class SettingItem {
    String name;
    int imgId;

    public SettingItem(String name, int imgId) {
        this.name = name;
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
