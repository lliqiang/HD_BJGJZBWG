package com.example.lenovo.hd_beijing_meseum;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.defiteview.HDialogBuilder;
import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;
import com.example.lenovo.hd_beijing_meseum.serveinterface.HeartInterface;
import com.example.lenovo.hd_beijing_meseum.serveinterface.StrInterface;
import com.example.lenovo.hd_beijing_meseum.utils.CommonUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HResourceUtil;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.HdApplication;
import com.example.lenovo.hd_beijing_meseum.utils.ILoadListener;
import com.example.lenovo.hd_beijing_meseum.utils.SharedPrefUtil;
import com.example.lenovo.hd_beijing_meseum.utils.StatusBarUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class BaseActivity extends AppCompatActivity {
    private HDialogBuilder hDialogBuilder;
    private SharedPrefUtil sharedPrefUtil;
    private String path;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                retrofitHeart();
                handler.sendEmptyMessageDelayed(1,60*2000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        StatusBarUtils.setWindowStatusBarColor(this,R.color.translate);
        setContentView(R.layout.activity_base);
        sharedPrefUtil = new SharedPrefUtil(this, "sp");
        if (sharedPrefUtil.getPrefString("AND").equals("and")) {
           retrofitKind();
        }else {
            handler.sendEmptyMessageDelayed(1,60*2000);

        }

        path = Constant.getDefaultFileDir() + "CHINESE" + "/" + "1304/" + "1304.png";
        File file = new File(path);
        if (!file.exists()) {
            if (Build.VERSION.SDK_INT>21){
                new AlertDialog.Builder(BaseActivity.this).setTitle("资源下载")
                        .setIcon(R.mipmap.lanch)
                        .setView(R.layout.alert_layout)
                        .setNegativeButton(getResources().getString(R.string.cancel), null)
                        .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                initData();
                            }
                        }).show();
            }else {
                new AlertDialog.Builder(BaseActivity.this).setTitle("资源下载")
                        .setIcon(R.mipmap.lanch)
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                initData();
                            }
                        }).show();
            }

        }
    }

    private void retrofitKind() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.200.234.14:8091/bjgdjzbwg/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();
        //第四步：通过retrofit获取动态服务代理对象
        StrInterface strInterface = retrofit.create(StrInterface.class);
        //第五步：通过实体调用请求方法，获取Call对象
        Call<ResponseBody> call = strInterface.getResult(1);
        //第六步： Call执行异步请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    sharedPrefUtil.setPrefString("AND", responseBody.getData());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void retrofitHeart() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.200.234.14:8091/bjgdjzbwg/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();
        HeartInterface heartInterface = retrofit.create(HeartInterface.class);
        //第五步：通过实体调用请求方法，获取Call对象
        Call<ResponseBody> call = heartInterface.getResult(1, sharedPrefUtil.getPrefString("AND"));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void initData() {
        if (CommonUtil.isOnline(BaseActivity.this)) {


            if (!HdAppConfig.isLoading()) {
                HdAppConfig.setIsLoading(true);
                HResourceUtil.showDownloadDialog(BaseActivity.this);
                HResourceUtil.downloadRes(BaseActivity.this, new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        HdAppConfig.setIsLoading(false);
                        HdAppConfig.setIsFirstUse(false);
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(BaseActivity.this, getString(R.string
                                .load_succeed));
                    }

                    @Override
                    public void onLoadFailed() {
                        HdAppConfig.setIsLoading(false);
                        HResourceUtil.hideDownloadDialog();
                        CommonUtil.showToast(BaseActivity.this, getString(R.string
                                .load_failed));
                    }
                });
            }

        } else {
            showNetworkConnFailedDialog();
        }
    }

    private void showNetworkConnFailedDialog() {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(BaseActivity.this);
        hDialogBuilder.withIcon(R.mipmap.ic_launcher)

                .title(getString(R.string.warm_tip))
                .message(getString(R.string.net_not_available))
                .nBtnText(getString(R.string.close))
                .pBtnText(getString(R.string.submit))
                .pBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideDialog();
                        Intent intent;
                        if (Build.VERSION.SDK_INT > 10) {
                            intent = new Intent(Settings
                                    .ACTION_WIRELESS_SETTINGS);
                        } else {
                            intent = new Intent();
                            ComponentName component = new ComponentName("com.android" +
                                    ".settings", "com" +
                                    ".android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        startActivity(intent);
                    }
                })
                .nBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideDialog();
                        finish();
                    }
                }).show();
    }

    private void hideDialog() {
        if (hDialogBuilder != null) {
            hDialogBuilder.dismiss();
            hDialogBuilder = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler=null;
        }
    }



}
