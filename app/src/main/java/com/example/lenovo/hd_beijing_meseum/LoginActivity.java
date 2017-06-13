package com.example.lenovo.hd_beijing_meseum;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.hd_beijing_meseum.setting.SettingActivity;
import com.example.lenovo.hd_beijing_meseum.utils.HdAppConfig;
import com.example.lenovo.hd_beijing_meseum.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseTActivity implements View.OnClickListener {

    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.imgClose)
    ImageView imgClose;
    @Bind(R.id.textView)
    TextView textView;
    String pwd = "";
    String fromAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        imgClose.setOnClickListener(this);
        fromAction = getIntent().getAction();
        switch (fromAction) {
            case "ADMIN":
                tvTitle.setText("设置权限获取");
                break;
            case "POWER":
                tvTitle.setText("关机权限获取");
                break;
        }


    }

    @Override
    public void onClick(View v) {
        finish();
    }



    public void num_0(View view) {
        if (pwd.length() < 8) {
            pwd += 0;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_1(View view) {
        if (pwd.length() < 8) {
            pwd += 1;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_2(View view) {
        if (pwd.length() < 8) {
            pwd += 2;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_3(View view) {
        if (pwd.length() < 8) {
            pwd += 3;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_4(View view) {
        if (pwd.length() < 8) {
            pwd += 4;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_5(View view) {
        if (pwd.length() < 8) {
            pwd += 5;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_6(View view) {
        if (pwd.length() < 8) {
            pwd += 6;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_7(View view) {
        if (pwd.length() < 8) {
            pwd += 7;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_8(View view) {
        if (pwd.length() < 8) {
            pwd += 8;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_9(View view) {
        if (pwd.length() < 8) {
            pwd += 9;
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_c(View view) {
        if (pwd.length() > 1) {
            pwd = pwd.substring(0, pwd.length() - 1);
            textView.setText(pwd);
        } else {
            clearInput();
        }
    }

    public void num_ok(View view) {
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, R.string.tip_pwd_input, Toast.LENGTH_SHORT).show();
        } else {
            switch (fromAction) {
                case "ADMIN":
                    if (TextUtils.equals(pwd, HdAppConfig.getPassword()) || TextUtils.equals(pwd,
                            HdAppConfig.LOGIN_PWD)) {
                        openActivity(LoginActivity.this, SettingActivity.class);
                        finish();
                    } else {
                        clearInput();

                        ToastUtils.toast(LoginActivity.this,R.string.msg_error_pwd);
                    }
                    break;
                case "POWER":
                    if (TextUtils.equals(pwd, HdAppConfig.getPassword()) || TextUtils.equals(pwd,
                            HdAppConfig.LOGIN_PWD)) {
                        HdAppConfig.setPowerPermi(1);
                        ToastUtils.toast(LoginActivity.this,R.string.get_power_perm_success);
                        finish();
                    } else {
                        clearInput();
                        ToastUtils.toast(LoginActivity.this,R.string.msg_error_pwd);
                    }
                    break;
            }
        }
    }

    private void clearInput() {
        pwd = "";
        textView.setText("请输入密码");
    }

}
