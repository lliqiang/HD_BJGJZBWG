package com.example.lenovo.hd_beijing_meseum.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.lenovo.hd_beijing_meseum.Constant.Constant;

/**
 * Created by lenovo on 2016/11/2.
 */

public class Exhibition implements Parcelable {
    private int AutoNum;
    private String FileNo;
    private int FileType;
    private String Name;
    private double x;
    private double y;
    private int floor;
    private int UnitNo;
    private int PicCount;
private String PicPath;
    public static final Creator<Exhibition> CREATOR = new Creator<Exhibition>() {
        @Override
        public Exhibition createFromParcel(Parcel in) {
            return new Exhibition(in);
        }

        @Override
        public Exhibition[] newArray(int size) {
            return new Exhibition[size];
        }
    };

    public String getPicPath(int index) {
        if (index == 0) {
            return PicPath + ".png";
        } else {
            return PicPath + "_" + index + ".png";
        }
    }

   public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    protected Exhibition(Parcel in) {
        AutoNum = in.readInt();
        FileNo = in.readString();
        FileType = in.readInt();
        x = in.readDouble();
        y = in.readDouble();
        floor = in.readInt();
        Name = in.readString();
        UnitNo = in.readInt();
        PicCount = in.readInt();
        PicPath = in.readString();


    }

    public Exhibition() {
    }

    public int getAutoNum() {
        return AutoNum;
    }

    public void setAutoNum(int autoNum) {
        AutoNum = autoNum;
    }

    public String getFileNo() {
        return FileNo;
    }

    public void setFileNo(String fileNo) {
        FileNo = fileNo;
    }

    public int getFileType() {
        return FileType;
    }

    public void setFileType(int fileType) {
        this.FileType = fileType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(int unitNo) {
        UnitNo = unitNo;
    }

    public int getPicCount() {
        return PicCount;
    }

    public void setPicCount(int picCount) {
        PicCount = picCount;
    }

    public String getPicPath() {
        return PicPath;
    }

    public static Exhibition Cursor2Model(Cursor cursor) {
        Exhibition exhibition = new Exhibition();
        exhibition.setAutoNum(cursor.getInt(0));
        exhibition.setFileNo(cursor.getString(1));
        exhibition.setFileType(cursor.getInt(3));
        exhibition.setName(cursor.getString(4));
        exhibition.setX(cursor.getDouble(5));
        exhibition.setY(cursor.getDouble(6));
        exhibition.setFloor(cursor.getInt(7));
        exhibition.setUnitNo(cursor.getInt(8));
        exhibition.setPicCount(cursor.getInt(9));
        exhibition.setPicPath(Constant.getDefaultFileDir() + "CHINESE" + "/" + cursor
                .getString(1) + "/" + cursor.getString(1));
        return exhibition;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(AutoNum);
        dest.writeString(FileNo);
        dest.writeInt(FileType);
        dest.writeDouble(x);
        dest.writeDouble(y);
        dest.writeInt(floor);
        dest.writeString(Name);
        dest.writeInt(UnitNo);
        dest.writeInt(PicCount);



        dest.writeString(PicPath);
    }
}
