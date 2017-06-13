package com.example.lenovo.hd_beijing_meseum.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.Constant.Constant;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.dialog.DialogCenter;
import com.example.lenovo.hd_beijing_meseum.dialog.DialogClickListener;
import com.example.lenovo.hd_beijing_meseum.event.DeviceNoEvent;
import com.example.lenovo.hd_beijing_meseum.rxbus.RxBus;
import com.example.lenovo.hd_beijing_meseum.setting.LCommonAdapter;
import com.example.lenovo.hd_beijing_meseum.update.CheckCallback;
import com.example.lenovo.hd_beijing_meseum.update.CheckResponse;
import com.example.lenovo.hd_beijing_meseum.update.CheckUpdateActivity;
import com.example.lenovo.hd_beijing_meseum.utils.AppUtil;
import com.example.lenovo.hd_beijing_meseum.utils.FileUtils;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.HdApplication;
import com.example.lenovo.hd_beijing_meseum.utils.HdConstants;
import com.example.lenovo.hd_beijing_meseum.utils.ToastUtils;
import com.example.lenovo.hd_beijing_meseum.view.setting.HSetting;
import com.example.lenovo.hd_beijing_meseum.view.setting.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingActivity extends CheckUpdateActivity {

    @Bind(R.id.imgBack)
    ImageView imgBack;
    @Bind(R.id.txtTitle)
    TextView txtTitle;
    @Bind(R.id.listView)
    ListView listView;
    LCommonAdapter<SettingItem> adapter;
    List<SettingItem> settingItems = new ArrayList<>();
    String[] settingNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initData();
        initUI();
    }
    /**
     * 设置Item，名字+ICON
     */
    class SettingItem {
        String name;
        int imgId;

        public SettingItem(String name, int imgId) {
            this.name = name;
            this.imgId = imgId;
        }

        public String getName() {
            return name;
        }

        public int getImgId() {
            return imgId;
        }
    }
    /**
     * 初始化设置数据
     */
    private void initData() {
        settingNames = getResources().getStringArray(R.array.settings);
        int[] iconId = {R.mipmap.icon_set_1, R.mipmap.icon_set_2,
                R.mipmap.icon_set_3, R.mipmap.icon_set_4, R.mipmap.icon_set_5,
                R.mipmap.icon_set_6, R.mipmap.icon_set_7, R.mipmap.icon_set_8,
                R.mipmap.icon_set_9, R.mipmap.icon_set_10, R.mipmap.icon_set_11,
                R.mipmap.icon_set_12, R.mipmap.icon_set_13, R.mipmap.icon_set_14,
                R.mipmap.icon_set_15, R.mipmap.icon_set_16, R.mipmap.icon_set_17};
        for (int i = 0; i < iconId.length; i++) {
            settingItems.add(new SettingItem(settingNames[i], iconId[i]));
        }
    }

    private void initUI() {
        txtTitle.setTypeface(HdApplication.typeface);
        txtTitle.setText(R.string.set);
        imgBack.setOnClickListener(v -> finish());
        adapter = new LCommonAdapter<SettingItem>(SettingActivity.this, R.layout.item_setting_o,
                settingItems) {
            @Override
            public void convert(ViewHolder holder, SettingItem settingItem) {
                holder.setText(R.id.txtName, settingItem.getName());
                holder.setTypeface(HdApplication.typeface, R.id.txtName);
                holder.setImageResource(R.id.imgIcon, settingItem.getImgId());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            SettingItem settingItem = (SettingItem) parent.getItemAtPosition(position);
            switch (position) {
                case 0://自动讲解
                    showDialogBySetType(HSetting.AUTO_FLAG, settingItem.getName());
                    break;
                case 1://智慧服务
                    showDialogBySetType(HSetting.SMART_SERVICE, settingItem.getName());
                    break;
                case 2://节能模式
                    showDialogBySetType(HSetting.SCREEN_MODE, settingItem.getName());
                    break;
                case 3://公众关机
                    showDialogBySetType(HSetting.POWER_MODE, settingItem.getName());
                    break;
                case 4://自动模式
                    showDialogBySetType(HSetting.AUTO_MODE, settingItem.getName());
                    break;
                case 5://感应模式
                    showDialogBySetType(HSetting.RECEIVE_NO_MODE, settingItem.getName());
                    break;
                case 6://预警模式
                    showDialogBySetType(HSetting.STC_MODE, settingItem.getName());
                    break;
                case 7://网络设置
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    break;
                case 8://服务器设置
                    editIpPort(settingItem);
                    break;
                case 9://RSSI设置
                    editDefaultRssi(settingItem);
                    break;
                case 10://工程模式
                    //TODO 工程模式
                    break;
                case 11://测试平台
                    openTestPlatform();
                    break;
                case 12://应用更新
                    checkNewVersion(new CheckCallback() {
                        @Override
                        public void hasNewVersion(CheckResponse checkResponse) {
                            showHasNewVersionDialog(checkResponse);
                        }

                        @Override
                        public void isAlreadyLatestVersion() {
                            showVersionInfoDialog();
                        }
                    });
                    break;
                case 13://清除资源
                    clearRes();
                    break;
                case 14://管理员密码
                    editPwd(settingItem);
                    break;
                case 15://设备编号
                    editDeviceNo(settingItem);
                    break;
                case 16://版本信息
                    showVersionInfoDialog();
                    break;
            }
        });
    }


    /**
     * 编辑默认Rssi门限值
     *
     * @param settingItem
     */
    private void editDefaultRssi(SettingItem settingItem) {
        View rootRssi = View.inflate(SettingActivity.this, R.layout
                .dialog_custom_view_edt, null);
        EditText edtRssi = (EditText) rootRssi.findViewById(R.id.editText);
        edtRssi.setTypeface(HdApplication.typeface);
        edtRssi.setHint("请输入RSSI强度值，30-100");
        Selection.setSelection(edtRssi.getText(), edtRssi.getText().length());
        DialogCenter.showDialog(SettingActivity.this, rootRssi, new
                DialogClickListener() {
                    @Override
                    public void p() {
                        String rssi = edtRssi.getText().toString();
                        if (isValidRssi(rssi)) {
                            HdAppConfig.setRssi(Integer.valueOf(rssi));
                        } else {
                            ToastUtils.toast(SettingActivity.this,getString(R.string.fromat));
                        }
                        DialogCenter.hideDialog();
                    }

                    @Override
                    public void n() {
                    }
                }, new String[]{settingItem.getName(), getString(R.string.submit)});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            listView.setAdapter(null);
            adapter = null;
            listView = null;
        }
    }

    /**
     * 修改服务器IP + PORT
     *
     * @param settingItem
     */
    private void editIpPort(SettingItem settingItem) {
        View rootIp = View.inflate(SettingActivity.this, R.layout
                .dialog_custom_view_edt, null);
        EditText edtIp = (EditText) rootIp.findViewById(R.id.editText);
        edtIp.setTypeface(HdApplication.typeface);
        edtIp.setText(HdAppConfig.getDefaultIpPort());
        Selection.setSelection(edtIp.getText(), edtIp.getText().length());
        DialogCenter.showDialog(SettingActivity.this, rootIp, new
                DialogClickListener() {
                    @Override
                    public void p() {
                        String ipPort = edtIp.getText().toString();
                        if (TextUtils.isEmpty(ipPort)) {

                            ToastUtils.toast(SettingActivity.this,getString(R.string.ip_nospace));
                        } else {
                            DialogCenter.hideDialog();
                            if (!TextUtils.equals(HdAppConfig.getDefaultIpPort(),
                                    ipPort)) {
                                HdAppConfig.setDefaultIpPort(ipPort);
                                RxBus.getDefault().post(new DeviceNoEvent());
                            }
                        }
                    }

                    @Override
                    public void n() {
                        DialogCenter.hideDialog();
                    }
                }, new String[]{settingItem.getName(), getString(R.string.submit),
                getString(R.string.close)});
    }

    /**
     * 根据设置类型显示Dialog
     *
     * @param setType
     * @param title
     */
    private void showDialogBySetType(int setType, String title) {
        DialogCenter.showDialog(SettingActivity.this, HSetting.getInstance().getSetListView
                (SettingActivity.this, setType), new
                DialogClickListener() {
                    @Override
                    public void p() {
                        HSetting.getInstance().destroyAdapter();
                        DialogCenter.hideDialog();
                    }

                    @Override
                    public void n() {

                    }
                }, new String[]{title, getString(R.string.submit)});
    }

    /**
     * 判断是否是有效的Rssi（强度值）
     *
     * @param str
     * @return
     */
    private boolean isValidRssi(String str) {
        boolean is = false;
        try {
            int i = Integer.parseInt(str);
            if (i > 100 || i < 30) {
                is = false;
            } else {
                is = true;
            }
        } catch (Exception e) {
        }

        return is;
    }
    /**
     * 打开测试平台
     */
    private void openTestPlatform() {
        if (AppUtil.checkInstallByPackageName(SettingActivity.this, HdConstants
                .TEST_APP_PACKAGE_NAME)) {
            Intent intent = new Intent();
            intent.setClassName(HdConstants.TEST_APP_PACKAGE_NAME,
                    HdConstants.TEST_APP_LAUNCHER_ACT);
            startActivity(intent);
        } else {

            ToastUtils.toast(SettingActivity.this,getString(R.string.install_text));
        }
    }

    /**
     * 清除资源
     */
    private void clearRes() {
        DialogCenter.showDialog(SettingActivity.this, new DialogClickListener() {
            @Override
            public void p() {
                DialogCenter.hideDialog();
                DialogCenter.showProgressDialog(SettingActivity.this, "请稍后...", false);
                new Handler().postDelayed(() -> {
                    FileUtils.deleteFile(Constant.getDefaultFileDir());
                    DialogCenter.hideProgressDialog();

                    ToastUtils.toast(SettingActivity.this,getString(R.string.delete_success));
                }, 2000L);
            }

            @Override
            public void n() {
                DialogCenter.hideDialog();
            }
        }, new String[]{"温馨提示", "确定要删除导览资源吗？", "删除", "取消"});
    }

    /**
     * 修改管理员密码
     *
     * @param settingItem
     */
    private void editPwd(SettingItem settingItem) {
        View rootPwd = View.inflate(SettingActivity.this, R.layout
                .dialog_custom_view_edt, null);
        EditText edtPwd = (EditText) rootPwd.findViewById(R.id.editText);
        edtPwd.setTypeface(HdApplication.typeface);
        edtPwd.setText(HdAppConfig.getPassword());
        Selection.setSelection(edtPwd.getText(), edtPwd.getText().length());
        DialogCenter.showDialog(SettingActivity.this, rootPwd, new
                DialogClickListener() {
                    @Override
                    public void p() {
                        String password = edtPwd.getText().toString();
                        if (isPassword(password)) {
                            HdAppConfig.setPassword(password);
                            DialogCenter.hideDialog();
                        } else {

                            ToastUtils.toast(SettingActivity.this,"格式错误，请输入1-8位数字！");
                        }
                    }

                    @Override
                    public void n() {
                    }
                }, new String[]{settingItem.getName(), getString(R.string.submit)});
    }
    /**
     * 判断是否是有效密码（格式）
     *
     * @param str
     * @return
     */
    private boolean isPassword(String str) {
        Pattern pattern = Pattern.compile("^\\d{1,8}$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 修改设备号
     *
     * @param settingItem
     */
    private void editDeviceNo(SettingItem settingItem) {
        View rootDeviceNo = View.inflate(SettingActivity.this, R.layout
                .dialog_custom_view_edt, null);
        EditText edtDeviceNo = (EditText) rootDeviceNo.findViewById(R.id.editText);
        edtDeviceNo.setTypeface(HdApplication.typeface);
        edtDeviceNo.setText(HdAppConfig.getDeviceNo());
        Selection.setSelection(edtDeviceNo.getText(), edtDeviceNo.getText().length());
        DialogCenter.showDialog(SettingActivity.this, rootDeviceNo, new
                DialogClickListener() {
                    @Override
                    public void p() {
                        String deviceNo = edtDeviceNo.getText().toString();
                        if (TextUtils.isEmpty(deviceNo)) {
                            ToastUtils.toast(SettingActivity.this,"请输入设备编号！");
                        } else {
                            if (!TextUtils.equals(HdAppConfig.getDeviceNo(),
                                    deviceNo)) {
                                ToastUtils.toast(SettingActivity.this,"修改成功");
                                HdAppConfig.setDeviceNo(deviceNo);
                                RxBus.getDefault().post(new DeviceNoEvent());
                            }
                            DialogCenter.hideDialog();
                        }
                    }

                    @Override
                    public void n() {
                        DialogCenter.hideDialog();
                    }
                }, new String[]{settingItem.getName(), getString(R.string.submit),
                getString(R.string.close)});
    }
}
