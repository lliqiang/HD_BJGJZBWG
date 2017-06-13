package com.example.lenovo.hd_beijing_meseum.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lenovo.hd_beijing_meseum.Constant.Constant;

/**
 * 作者：Tailyou
 * 时间：2016/1/12 15:12
 * 邮箱：tailyou@163.com
 * 描述：资源数据库工具类-单例模式
 */
public class HResourceDB {

    private SQLiteDatabase db = null;

    /**
     * 单例模式
     */
    private static volatile HResourceDB instance = null;

    /**
     * 获取实例
     *
     * @return
     */
    public static HResourceDB getInstance() {
        if (instance == null) {
            synchronized (HResourceDB.class) {
                if (instance == null) {
                    instance = new HResourceDB();
                }
            }
        }
        return instance;
    }


    /**
     * 私有构造方法
     */
    private HResourceDB() {
    }


    /**
     * 打开指定文件路径对应的数据库
     *
     * @param dbFilePath
     */
    public void openDB(String dbFilePath) {
        try {
            db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase
                    .NO_LOCALIZED_COLLATORS);
        } catch (Exception e) {
            e.printStackTrace();
            closeDB();
        }
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (db != null)
            db = null;
    }


    /**
     * 加载展厅数据
     *
     * @return
     */
    public Cursor loadExhibitions() {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(Constant.getDbFilePath());
            String sql = String.format("SELECT * FROM %s_EXHIBITION  ORDER BY UnitNo",
                    "CHINESE");
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }


    /**
     * 加载参观攻略
     *
     * @return
     */
    public Cursor loadRaiders() {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(Constant.getDbFilePath());
            String sql = String.format("SELECT * FROM %s_RAIDERS", "CHINESE");
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

    /**
     * 加载全部资源
     *
     * @return
     */
    public Cursor loadAllRes() {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(Constant.getDbFilePath());
            String sql = "SELECT * FROM " + "CHINESE" + " WHERE FileType = 0 ";

            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

    /**
     * 加载精品
     *
     * @return
     */
    public Cursor loadResByIsBoutique(int isBoutique) {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(Constant.getDbFilePath());
            String sql = String.format("SELECT * FROM %s WHERE FileType = %d","CHINESE", isBoutique);

            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

    /**
     * 根据展厅或展区，加载资源
     *
     * @param unitNo
     * @return
     */
    public Cursor loadResListByUnitNo(int unitNo) {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(Constant.getDbFilePath());
            String sql = String.format("SELECT * FROM %s WHERE UnitNo = %s","CHINESE", unitNo);
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

    /**
     * 根据关键字加载资源列表
     *
     * @param keyWord
     * @return
     */
    public Cursor loadResListByKeyWord(String keyWord) {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(Constant.getDbFilePath());
            String sql = "SELECT * FROM " + "CHINESE" + " WHERE FileType = 0 " +
                    "AND ByName LIKE '%" + keyWord + "%'";
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

    /**
     * 根据自动编号加载资源
     *
     * @param autoNo
     * @return
     */
    public Cursor loadResByAutoNo(int autoNo) {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(Constant.getDbFilePath());
            String sql = String.format("SELECT * FROM %s WHERE AutoNum = %d","CHINESE", autoNo);
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }



    /**
     * 根据输入的数字编号File_De加载资源
     *
     * @param fileNo
     * @return
     */
    public Cursor loadResByDE(String fileNo) {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(Constant.getDbFilePath());
            String sql = "SELECT * FROM " + "CHINESE" + " WHERE FileType = 0 AND FileNo LIKE '%" + fileNo + "%'";
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

    /**
     * 根据楼层下载位置信息
     *
     * @param floor
     * @return
     */


    public Cursor loadLocationListByFloor(int floor) {
        Cursor cursor = null;
        try {
            openDB(Constant.getDbFilePath());
//            String sql = String.format("SELECT * FROM %s WHERE Floor = %d", str, floor);
            String sql="SELECT * FROM CHINESE WHERE Floor= "+floor;
//
//            String sql=String.format("SELECT * FROM CHINESE WHERE Floor=%d="+floor +" AND FileType=%d"+filetype);
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

    /**
     * 根据自动编号下载位置信息
     *
     * @param autoNo
     * @return
     */
//    public Cursor loadLocationByAutoNo(String autoNo) {
//        Cursor cursor = null;
//        try {
//            openDB(HdAppConfig.getDefaultDbFilePath());
//            String sql = String.format("SELECT * FROM %s_LOCATION WHERE AutoNum = %s",
//                    HdAppConfig.getLanguage(), autoNo);
//            cursor = db.rawQuery(sql, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return cursor;
//    }

}
