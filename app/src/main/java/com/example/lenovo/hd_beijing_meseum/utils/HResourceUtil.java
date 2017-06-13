package com.example.lenovo.hd_beijing_meseum.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.defiteview.HDialogBuilder;

import com.example.lenovo.hd_beijing_meseum.defiteview.HProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/4/25 08:36
 * 邮箱：tailyou@163.com
 * 描述：资源下载工具类
 */
public class HResourceUtil {

    static HProgressDialog progressDialog;
    static HDialogBuilder hDialogBuilder;
    static TextView textView;


    /**
     * 判断数据库是否存在
     *
     * @return
     */
    public static boolean isDbExist() {
        File file = new File(Constant.getDefaultFileDir());
        return file.exists();
    }

//    /**
//     * 判断地图资源是否存在
//     *
//     * @return
//     */
    public static boolean isMapResExist() {
        File file = new File(Constant.getDefaultMapFilePath());
        return file.exists();
    }

    /**
     * 判断当前语种某展品资源是否存在
     *
     * @return
     */
    public static boolean isExhibitExist(String fileNo) {
        File file = new File(Constant.getDefaultFileDir() +"CHINESE" + "/" +
                fileNo);
        return file.exists();
    }

    /**
     * 下载数据库
     *
     * @param iLoadListener
     */
    public static void downloadDb(final ILoadListener iLoadListener) {

        String url ="http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD_BJGJZBWG_RES/DATABASE.zip";
        String destFileDir = Constant.getDefaultFileDir();
        String destFileName = "DB.zip";

        FileUtils.makeDirs(destFileDir);
        OkHttpUtils.getInstance().cancelTag("LOAD_RES");

        OkHttpUtils.get()
                .tag("LOAD_RES")
                .url(url)
                .build()
                .execute(new HFileCallBack(destFileDir, destFileName) {
                    @Override
                    public void inProgress(float progress, long total) {

                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        iLoadListener.onLoadFailed();
                    }

                    @Override
                    public void onResponse(File response) {
                        iLoadListener.onLoadSucceed();
                    }
                });
    }

    /**
     * 下载资源
     *
     * @param context
     * @param iLoadListener
     */
    public static void downloadRes(final Activity context,
                                   final ILoadListener iLoadListener) {
//        String url = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HangZhou_Res%2FCHINESE.zip";
        String url = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD_BJGJZBWG_RES/CHINESE.zip";
        String destFileDir = Constant.getDefaultFileDir() + "CHINESE";
        String destFileName = "RES.zip";

        FileUtils.makeDirs(destFileDir);
        OkHttpUtils.getInstance().cancelTag("LOAD_RES");

        OkHttpUtils.get()
                .tag("LOAD_RES")
                .url(url)
                .build()
                .execute(new HFileCallBack(destFileDir, destFileName) {
                    @Override
                    public void inProgress(float progress, long total) {
                        if (progress > 0.95) {
                            textView.setText(context.getString(R.string.unzipping));
                        } else {
                            textView.setText(context.getString(R.string.downloading_res) +
                                    String.format("(%s/%s)",
                                            CommonUtil.getFormatSize(progress * total),
                                            CommonUtil.getFormatSize(total)));
                        }
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        iLoadListener.onLoadFailed();
                    }

                    @Override
                    public void onResponse(final File file) {
                        iLoadListener.onLoadSucceed();
                    }
                });
    }

    public static void downMapRes(final Activity context,
                                   final ILoadListener iLoadListener) {
        String url = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD_BJGJZ_RES%2Fmap.zip";
        String destFileDir = Constant.getDefaultMapFilePath();
        String destFileName = "Map.zip";

        FileUtils.makeDirs(destFileDir);
        OkHttpUtils.getInstance().cancelTag("MAP_RES");

        OkHttpUtils.get()
                .tag("MAP_RES")
                .url(url)
                .build()
                .execute(new HFileCallBack(destFileDir, destFileName) {
                    @Override
                    public void inProgress(float progress, long total) {
                        if (progress > 0.95) {
                            textView.setText(context.getString(R.string.unzipping));
                        } else {
                            textView.setText(context.getString(R.string.downloading_res) +
                                    String.format("(%s/%s)",
                                            CommonUtil.getFormatSize(progress * total),
                                            CommonUtil.getFormatSize(total)));
                        }
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        iLoadListener.onLoadFailed();
                    }

                    @Override
                    public void onResponse(final File file) {
                        iLoadListener.onLoadSucceed();
                    }
                });
    }
    /**
     * 同时下载数据库和资源
     *
     * @param context
     */
    public static void loadDbRes(final Activity context,
                                 final ILoadListener iLoadListener) {
        String url = Constant.DATABASE_STR;
        String destFileDir = Constant.getDefaultFileDir();
        String destFileName = "DB.zip";

        FileUtils.makeDirs(destFileDir);
        OkHttpUtils.getInstance().cancelTag("LOAD_RES");

        OkHttpUtils
                .get()
                .tag("LOAD_RES")
                .url(url)
                .build()
                .execute(new HFileCallBack(destFileDir, destFileName) {
                    @Override
                    public void inProgress(float progress, long total) {
                        textView.setText(context.getString(R.string.downloading_db) +
                                String.format("(%s/%s)",
                                        CommonUtil.getFormatSize(progress * total),
                                        CommonUtil.getFormatSize(total)));
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        iLoadListener.onLoadFailed();
                    }

                    @Override
                    public void onResponse(File response) {
                        downloadRes(context, iLoadListener);
                    }
                });
    }


//    /**
//     * 单包下载
//     *
//     * @param fileNo
//     */
//    public static void loadExhibit(final ILoadListener iLoadListener, String fileNo) {
////        http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HangZhou_Res%2FCHINESE%2F1404.zip
//
//        String url = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HangZhou_Res%2FCHINESE%2F" + fileNo + ".zip";
//        String destFileDir = Constant.getDefaultFileDir() + "CHINESE";
//        String destFileName = "EXHIBIT.zip";
//
//        FileUtils.makeDirs(destFileDir);
//        OkHttpUtils.getInstance().cancelTag("LOAD_RES");
//
//        OkHttpUtils.get()
//                .tag("LOAD_RES")
//                .url(url)
//                .build()
//                .execute(new HFileCallBack(destFileDir, destFileName) {
//                    @Override
//                    public void inProgress(float progress, long total) {
//
//                    }
//
//                    @Override
//                    public void onError(okhttp3.Call call, Exception e) {
//                        iLoadListener.onLoadFailed();
//                    }
//
//                    @Override
//                    public void onResponse(File file) {
//                        iLoadListener.onLoadSucceed();
//                    }
//                });
//    }


    /**
     * 显示加载 ProgressDialog，圆形进度
     *
     * @param context
     */
    public static void showDownloadProgressDialog(Activity context, String msg) {
        hideDownloadProgressDialog();
        progressDialog = new HProgressDialog(context);
        progressDialog
//                .setTypeface(HdApplication.getTypeface())
                .message(msg)
                .tweenAnim(R.mipmap.progress_roate, R.anim.progress_rotate)
                .outsideCancelable(false)
                .cancelable(false)
                .show();
    }


    /**
     * 隐藏 ProgressDialog
     */
    public static void hideDownloadProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    /**
     * 显示下载 Dialog，文字进度
     *
     * @param context
     */
    public static void showDownloadDialog(Activity context) {
        hideDownloadDialog();
        hDialogBuilder = new HDialogBuilder(context);
        textView = (TextView) context.getLayoutInflater().inflate(R.layout
                .layout_hd_dialog_custom_tv, null);
//        textView.setTypeface(HdApplication.getTypeface());
        textView.setText(context.getString(R.string.download_res) + "...");
        hDialogBuilder
                .withIcon(R.mipmap.icon_photo)
//                .setTypeface(HdApplication.getTypeface())
                .title(context.getString(R.string.download_res))
                .setCustomView(textView)
                .nBtnText(context.getString(R.string.cancel))
                .nBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        HdAppConfig.setIsLoading(false);
                        OkHttpUtils.getInstance().cancelTag("LOAD_RES");
                        hideDownloadDialog();
                    }
                })
                .cancelable(false)
                .show();
    }


    /**
     * 隐藏下载Dialog，带下载进度
     */
    public static void hideDownloadDialog() {
        if (hDialogBuilder != null) {
            hDialogBuilder.dismiss();
            hDialogBuilder = null;
        }
    }


}
