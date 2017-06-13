package com.example.lenovo.hd_beijing_meseum.model;

import android.database.Cursor;

import java.io.Serializable;

/**
 * 作者：Tailyou
 * 时间：2016/3/11 11:06
 * 邮箱：tailyou@163.com
 * 描述：实体-地图点位
 */
public class Location implements Serializable {

    /**
     * 点位X坐标
     */
    private double x;
    /**
     * 点位Y坐标
     */
    private double y;
    /**
     * 点位多模卡号
     */
    private int autoNo;
    /**
     * 点位楼层
     */
    private int floor;
    /**
     * 点位对应展品文件编号
     */
    private String exhibitFileNo;
    /**
     * 点位对应展品名称
     */
    private String exhibitName;

    private int FileType;



    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getAutoNo() {
        return autoNo;
    }

    public int getFileType() {
        return FileType;
    }

    public void setFileType(int fileType) {
        FileType = fileType;
    }

    public void setAutoNo(int autoNo) {
        this.autoNo = autoNo;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getExhibitFileNo() {
        return exhibitFileNo;
    }

    public void setExhibitFileNo(String exhibitFileNo) {
        this.exhibitFileNo = exhibitFileNo;
    }

    public String getExhibitName() {
        return exhibitName;
    }

    public void setExhibitName(String exhibitName) {
        this.exhibitName = exhibitName;
    }

    /**
     * Cursor to Location
     *
     * @param cursor
     * @return
     */
    public static Location cursorToLocationInfo(Cursor cursor) {
        Location location = new Location();
        location.setAutoNo(cursor.getInt(0));
        location.setX(cursor.getDouble(5));
        location.setY(cursor.getDouble(6));
        location.setFloor(cursor.getInt(7));
        location.setFileType(cursor.getInt(3));
        location.setExhibitFileNo(cursor.getString(1));
        location.setExhibitName(cursor.getString(4));
        return location;
    }

}
