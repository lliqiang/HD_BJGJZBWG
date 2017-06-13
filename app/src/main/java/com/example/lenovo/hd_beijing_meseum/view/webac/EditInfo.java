package com.example.lenovo.hd_beijing_meseum.view.webac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.utils.CommonUtil;
import com.example.lenovo.hd_beijing_meseum.view.setting.SettingView;

import org.w3c.dom.Text;

public class EditInfo extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private TextView editInfo;
    private ImageView toBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_edit_info);
        title = (TextView) findViewById(R.id.title);
        editInfo = (TextView) findViewById(R.id.edit_info);
        toBack = (ImageView) findViewById(R.id.toback);
        title.setText("版本信息");
        editInfo.setText("北京古代建筑博物馆V" + CommonUtil.getAppVersionName(EditInfo.this));
        toBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
