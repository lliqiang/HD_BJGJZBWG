package com.example.lenovo.hd_beijing_meseum.update;

import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.BaseTActivity;
import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.dialog.DialogCenter;
import com.example.lenovo.hd_beijing_meseum.dialog.DialogClickListener;
import com.example.lenovo.hd_beijing_meseum.http.FileApi;
import com.example.lenovo.hd_beijing_meseum.http.FileCallback;
import com.example.lenovo.hd_beijing_meseum.http.RequestApi;
import com.example.lenovo.hd_beijing_meseum.http.RequestSubscriber;
import com.example.lenovo.hd_beijing_meseum.utils.AppUtil;
import com.example.lenovo.hd_beijing_meseum.utils.DataManager;
import com.example.lenovo.hd_beijing_meseum.utils.HdApplication;
import com.example.lenovo.hd_beijing_meseum.utils.HdConstants;
import com.example.lenovo.hd_beijing_meseum.utils.NetUtil;
import com.example.lenovo.hd_beijing_meseum.utils.ViewUtil;
import com.orhanobut.logger.Logger;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/10/9 10:27
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class CheckUpdateActivity extends BaseTActivity {

    TextView txtProgress;
    TextView txtUpdateLog;

    /**
     * 检查新版本
     */
    public void checkNewVersion(CheckCallback callback) {
        if (NetUtil.isConnected(CheckUpdateActivity.this)) {
            RequestApi.getInstance(HdConstants.APP_UPDATE_URL)
                    .checkVersion(new RequestSubscriber<CheckResponse>() {
                        @Override
                        public void succeed(CheckResponse checkResponse) {
                            Logger.e(checkResponse.getMsg());
                            switch (checkResponse.getStatus()) {
                                case "2001":
                                    callback.isAlreadyLatestVersion();
                                    break;
                                case "2002":
                                    callback.hasNewVersion(checkResponse);
                                    break;
                                case "4041":
                                    break;
                            }
                        }
                    });
        }
    }

    /**
     * 有新版本时显示Dialog
     *
     * @param checkResponse
     */
    public void showHasNewVersionDialog(final CheckResponse checkResponse) {
        ScrollView scrollView = (ScrollView) View.inflate(CheckUpdateActivity.this, R.layout
                .dialog_custom_view_scroll_txt, null);
        txtUpdateLog = ViewUtil.getView(scrollView, R.id.tvUpdateLog);
        txtUpdateLog.setText("检查到新版本：" + checkResponse.getVersionInfo().getVersionName() + "\n更新日志：\n"
                + checkResponse.getVersionInfo().getVersionLog());
        DialogCenter.showDialog(CheckUpdateActivity.this, scrollView, new DialogClickListener() {
            @Override
            public void p() {
                showDownloadingDialog();
                loadAndInstall(checkResponse);
            }

            @Override
            public void n() {
                DialogCenter.hideDialog();
            }
        }, new String[]{"版本更新", "更新", "取消"});
    }

    /**
     * 显示下载Apk Dialog
     */
    private void showDownloadingDialog() {
        txtProgress = (TextView) View.inflate(CheckUpdateActivity.this, R.layout
                .dialog_custom_view_txt, null);
        txtProgress.setTypeface(HdApplication.typeface);
        txtProgress.setText("下载安装包...");
        DialogCenter.showDialog(CheckUpdateActivity.this, txtProgress, new DialogClickListener() {
            @Override
            public void p() {
                DialogCenter.hideDialog();
                FileApi.cancelLoading();
            }

            @Override
            public void n() {
            }
        }, new String[]{"下载更新", "取消"});
    }

    /**
     * Dialog-显示当前版本信息
     */
    public void showVersionInfoDialog() {
        DialogCenter.showDialog(CheckUpdateActivity.this, new DialogClickListener() {
            @Override
            public void p() {
                DialogCenter.hideDialog();
            }
        }, new String[]{"版本更新", "当前已是最新版：" + AppUtil.getVersionName(CheckUpdateActivity.this), "取消"});
    }

    /**
     * 下载并安装新版Apk
     *
     * @param checkResponse
     */
    private void loadAndInstall(CheckResponse checkResponse) {
        String apkUrl = checkResponse.getVersionInfo().getVersionUrl();
        String baseUrl = apkUrl.substring(0, apkUrl.lastIndexOf("/") + 1);
        String fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        String fileStoreDir = Constant.getDefaultFileDir();
        FileApi.getInstance(baseUrl).loadFileByName(fileName,
                new FileCallback(fileStoreDir, fileName) {
                    @Override
                    public void progress(long progress,
                                         long total) {
                        txtProgress.setText(String.format("正在下载(%s/%s)",
                                DataManager.getFormatSize(progress),
                                DataManager.getFormatSize(total)));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call,
                                          Throwable t) {
                        DialogCenter.hideDialog();
                    }

                    @Override
                    public void onSuccess(File file) {
                        DialogCenter.hideDialog();
                        AppUtil.installApk(CheckUpdateActivity.this, file.getAbsolutePath());
                    }
                });
    }

}
