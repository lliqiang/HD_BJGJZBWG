package com.example.lenovo.hd_beijing_meseum.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.BaseActivity;
import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.view.webac.AcWeb;
import com.example.lenovo.hd_beijing_meseum.view.webac.EnviromentWeb;
import com.example.lenovo.hd_beijing_meseum.view.webac.TicketInfoWeb;
import com.example.lenovo.hd_beijing_meseum.view.webac.TransInfoWeb;
import com.example.lenovo.hd_beijing_meseum.view.webac.VisitInstructWeb;

public class VisitView extends AppCompatActivity implements View.OnClickListener{
private RelativeLayout VisitInstruct;
    private RelativeLayout TicketInfo;
    private RelativeLayout TransportInfo;
    private RelativeLayout AcInfo;
    private ImageView toBack;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_visit_view);
        initView();
        setlistner();
    }

    private void setlistner() {
VisitInstruct.setOnClickListener(this);
        TicketInfo.setOnClickListener(this);
        TransportInfo.setOnClickListener(this);
        AcInfo.setOnClickListener(this);
        toBack.setOnClickListener(this);
    }

    private void initView() {
        VisitInstruct= (RelativeLayout) findViewById(R.id.RL_Instruct);
        TicketInfo= (RelativeLayout) findViewById(R.id.TicketInfo);
        TransportInfo= (RelativeLayout) findViewById(R.id.TransInfo);
        AcInfo= (RelativeLayout) findViewById(R.id.RL_BallAc);
        toBack= (ImageView) findViewById(R.id.toback);
        title= (TextView) findViewById(R.id.title);
        title.setText("参观攻略");
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.RL_Instruct:
                intent=new Intent(VisitView.this,VisitInstructWeb.class);
                startActivity(intent);
                break;
            case R.id.TicketInfo:
                intent=new Intent(VisitView.this,TicketInfoWeb.class);
                startActivity(intent);
                break;
            case R.id.TransInfo:
                intent=new Intent(VisitView.this,TransInfoWeb.class);
                startActivity(intent);
                break;

            case R.id.RL_BallAc:
                intent=new Intent(VisitView.this,AcWeb.class);
                startActivity(intent);
                break;
            case R.id.toback:
                finish();
                break;
        }
    }
}
